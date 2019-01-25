package com.amsoft.erp.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.amsoft.erp.controller.xml.Certificado;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.vo.DadosCertificado;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.repository.produtos.Produtos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.jpa.FacesMessages;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;

@Named
@RequestScoped
public class GraficoNfeEmitidasBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");
	
	@Inject
	private CertificadoInit init;

	@Inject
	private Nfes nfes;

	@Inject
	private NFCes nfces;

	@Inject
	private Clientes clientes;

	@Inject
	private Produtos produtos;

	@Inject
	private Usuarios usuarios;

	@Inject
	private Certificado certificado;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private LineChartModel model;
	private LineChartModel modelNFCe;

	private DadosCertificado dadosCertificado;

	private String totalSaida;
	private String totalSaidaNFCe;

	private String totalEntrada;

	private String numeroClientes;
	private String numeroTransportadores;
	private String numeroUsuarios;
	private String numeroProdutos;
	private String numeroNFes;
	
	private String nfeVersao;
	private String nfeStatus;
	private String nfceVersao;
	private String nfceStatus;
	
	@Inject
	private FacesMessages messages;


	public void preRender() {
		this.model = new LineChartModel();
		this.model.setTitle("NF-e emitidas");
		this.model.setLegendPosition("e");
		this.model.setAnimate(true);

		this.model.getAxes().put(AxisType.X, new CategoryAxis());

		adicionarSerie("Todos das notas", null);
		adicionarSerie("Minhas notas", this.usuarioLogado.getUsuario());

		this.modelNFCe = new LineChartModel();
		this.modelNFCe.setTitle("NFC-e emitidas");
		this.modelNFCe.setLegendPosition("e");
		this.modelNFCe.setAnimate(true);

		this.modelNFCe.getAxes().put(AxisType.X, new CategoryAxis());

		adicionarSerieNFCe("Todos das notas", null);
		adicionarSerieNFCe("Minhas notas", this.usuarioLogado.getUsuario());

		this.totalSaida = this.nfes.getTotalSaida(this.usuarioLogado.getUsuario().getEmpresa());
		this.totalEntrada = this.nfes.getTotalEntrada(this.usuarioLogado.getUsuario().getEmpresa());
		this.numeroClientes = this.clientes.getNumeroClientes();
		this.numeroTransportadores = this.clientes.getNumeroTransportadores();
		this.numeroProdutos = this.produtos.getNumeroProdutos(this.usuarioLogado.getUsuario().getEmpresa());
		this.numeroUsuarios = this.usuarios.getNumeroUsuarios(this.usuarioLogado.getUsuario().getEmpresa());
		this.numeroNFes = this.nfes.getNumeroNotasEmitidas(this.usuarioLogado.getUsuario().getEmpresa());
		this.totalSaidaNFCe = this.nfces.getTotalSaida(this.usuarioLogado.getUsuario().getEmpresa());

		this.validaValidadeCertificado();

		try {
			status();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	private void statusSefaz() throws Exception {
//		servicoWebTeste.status(ConstantesUtil.NFE);
//		servicoWebTeste.status(ConstantesUtil.NFCE);
//	}

	private void status() throws Exception {

		try {

			init.iniciaConfiguracoes();
			TRetConsStatServ retorno;
			
			retorno = Nfe.statusServico(ConstantesUtil.NFCE);
			nfceVersao = ConstantesUtil.NFCE + " " + retorno.getVersao();
			nfceStatus =  retorno.getXMotivo();

			retorno = Nfe.statusServico(ConstantesUtil.NFE);
			nfeVersao = ConstantesUtil.NFE + " " + retorno.getVersao();
			nfeStatus=  retorno.getXMotivo();
			
			//FacesUtil.addInfoMessage(tipoNota + " " + retorno.getVersao() + ": " + retorno.getXMotivo());

		} catch (CertificadoException | NfeException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

	
	public boolean isProducao() {
		return this.usuarioLogado.getUsuario().getEmpresa().getWsAmbiente() == 1;
	}

	private void validaValidadeCertificado() {

		try {

			this.dadosCertificado = certificado.getDataValidade(this.usuarioLogado.getUsuario().getEmpresa());

			String mgsCertificado = "Certificado digital A1: Faltam " + dadosCertificado.getDiasValidos()
					+ " dias para o vencimento do seu certificado";

			if (dadosCertificado.getDiasValidos() < 31) {
				messages.error(mgsCertificado);

			}

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	private void adicionarSerie(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.nfes.valoresTotaisPorDataNfe(10, criadoPor,
				usuarioLogado.getUsuario().getEmpresa());

		ChartSeries series = new ChartSeries(rotulo);

		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.model.addSeries(series);
	}

	private void adicionarSerieNFCe(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.nfes.valoresTotaisPorData(10, criadoPor,
				usuarioLogado.getUsuario().getEmpresa());

		ChartSeries series = new ChartSeries(rotulo);

		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.modelNFCe.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}

	public LineChartModel getModelNFCe() {
		return modelNFCe;
	}

	public String getTotalSaida() {
		return totalSaida;
	}

	public String getTotalEntrada() {
		return totalEntrada;
	}

	public String getNumeroClientes() {
		return numeroClientes;
	}

	public String getNumeroTransportadores() {
		return numeroTransportadores;
	}

	public String getNumeroUsuarios() {
		return numeroUsuarios;
	}

	public String getNumeroProdutos() {
		return numeroProdutos;
	}

	public String getNumeroNFes() {
		return numeroNFes;
	}

	public String getTotalSaidaNFCe() {
		return totalSaidaNFCe;
	}

	public void setTotalSaidaNFCe(String totalSaidaNFCe) {
		this.totalSaidaNFCe = totalSaidaNFCe;
	}

	public DadosCertificado getDadosCertificado() {
		return dadosCertificado;
	}

	public void setDadosCertificado(DadosCertificado dadosCertificado) {
		this.dadosCertificado = dadosCertificado;
	}

	public String getNfeVersao() {
		return nfeVersao;
	}

	public void setNfeVersao(String nfeVersao) {
		this.nfeVersao = nfeVersao;
	}

	public String getNfeStatus() {
		return nfeStatus;
	}

	public void setNfeStatus(String nfeStatus) {
		this.nfeStatus = nfeStatus;
	}

	public String getNfceVersao() {
		return nfceVersao;
	}

	public void setNfceVersao(String nfceVersao) {
		this.nfceVersao = nfceVersao;
	}

	public String getNfceStatus() {
		return nfceStatus;
	}

	public void setNfceStatus(String nfceStatus) {
		this.nfceStatus = nfceStatus;
	}

	
}
