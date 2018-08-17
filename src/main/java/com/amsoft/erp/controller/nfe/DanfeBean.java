package com.amsoft.erp.controller.nfe;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

@Named
@RequestScoped
public class DanfeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFeEdicao
	private Nfe nfe;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private StreamedContent file;

	public StreamedContent getFile() {
		return file;
	}

	public void danfe() {
		try {
			gerarDanfe();
			System.out.println(nfe.getChave());
			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");
			file = new DefaultStreamedContent(stream, "file/pdf", nfe.getChave() + ".pdf");

			FacesUtil.addInfoMessage("Danfe gerado com sucesso");
		} catch (IOException e) {

		}

	}

	public void danfe(Nfe nfe) {
		try {
			gerarDanfe(nfe);
			System.out.println(nfe.getChave());
			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");
			file = new DefaultStreamedContent(stream, "file/pdf", nfe.getChave() + ".pdf");

			FacesUtil.addInfoMessage("Danfe gerado com sucesso");
		} catch (IOException e) {
			throw new NegocioException("Falha ao gerar danfe");

		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarDanfe() throws FileNotFoundException, IOException {

		try {

			AmsoftUtils.escritorXmlToDanfe(nfe);

			String arquivo = AmsoftUtils.getPastaXml() + "nfe.xml";
			String relatorio = (AmsoftUtils.getPastaRelatorios() + "danfe1.jasper");
			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/nfeProc/NFe/infNFe/det");

			HashMap mapa = new HashMap();

			exibirFoto();

			mapa.put("Logo",
					AmsoftUtils.getPastaRelatorios() + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			mapa.put("REPORT_LOCALE", new Locale("pt", "BR"));

			JasperPrint print = JasperFillManager.fillReport(relatorio, mapa, xml);

			String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(print, pdf);

		} catch (JRException e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarDanfe(Nfe nfe) throws FileNotFoundException, IOException {

		try {

			AmsoftUtils.escritorXmlToDanfe(nfe);

			String arquivo = AmsoftUtils.getPastaXml() + "nfe.xml";
			String relatorio = (AmsoftUtils.getPastaRelatorios() + "danfe1.jasper");
			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/nfeProc/NFe/infNFe/det");

			HashMap mapa = new HashMap();

			exibirFoto(nfe);

			mapa.put("Logo",
					AmsoftUtils.getPastaRelatorios() + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			mapa.put("REPORT_LOCALE", new Locale("pt", "BR"));

			JasperPrint print = JasperFillManager.fillReport(relatorio, mapa, xml);

			String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(print, pdf);

		} catch (JRException e) {

			e.printStackTrace();
		}
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	private String ajustaXmlParaDanfe(String texto) {
		texto = texto.replaceAll("enviNFe", "nfeProc");
		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");

		return texto;
	}

	public void escritorXmlToDanfe() {
		try {
			FileWriter fw = new FileWriter(getRaizProjeto() + "resources/xml/nfe.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			String texto = ajustaXmlParaDanfe(nfe.getXml());
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void exibirFoto() {

		byte[] someByteArray = nfe.getEmpresa().getLogo();

		try {
			FileOutputStream fos = new FileOutputStream(
					getRaizProjeto() + "relatorios/" + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			fos.write(someByteArray);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exibirFoto(Nfe nfe) {

		byte[] someByteArray = nfe.getEmpresa().getLogo();

		try {
			FileOutputStream fos = new FileOutputStream(
					getRaizProjeto() + "relatorios/" + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			fos.write(someByteArray);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
