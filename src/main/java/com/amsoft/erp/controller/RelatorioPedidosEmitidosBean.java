package com.amsoft.erp.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.primefaces.context.RequestContext;

import com.amsoft.erp.model.StatusNotas;
import com.amsoft.erp.model.TipoRelatorio;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AppZip;
import com.amsoft.erp.util.ExecutorRelatorio;
import com.amsoft.erp.util.jpa.FacesMessages;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class RelatorioPedidosEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicio;
	private Date dataFim;
	private StatusNotas[] statuses;

	public StatusNotas[] getStatuses() {
		return statuses;
	}

	public void setStatuses(StatusNotas[] statuses) {
		this.statuses = statuses;
	}

	private Integer empresa;

	private BigDecimal total;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private FacesMessages messages;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private List<Object> filtrados = new ArrayList<>();

	@Inject
	private NFCes nfces;

	@Inject
	private Nfes nfes;

	@Inject
	private AppZip appZip;

	public List<Object> getFiltrados() {
		return filtrados;
	}

	private TipoRelatorio relatorio = TipoRelatorio.NFE;

	@PostConstruct
	private void init() {

		this.setEmpresa(usuarioLogado.getUsuario().getEmpresa().getId());

		// this.filtrados = new ArrayList<>();

		// Calendar calendarData = Calendar.getInstance();
		// calendarData.setTime(this.getDataInicio());
		// int numeroDiasParaSubtrair = -15;
		// calendarData.add(Calendar.DATE, numeroDiasParaSubtrair);
		//
		// this.setDataInicio(calendarData.getTime());

	}

	public void emitir() {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("data_inicio", this.dataInicio);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.dataFim);

		calendar.add(Calendar.DAY_OF_MONTH, 1);

		this.dataFim = calendar.getTime();

		parametros.put("data_fim", this.dataFim);
		parametros.put("empresa", this.empresa);

		List<String> list = new ArrayList<>();

		for (Integer i = 0; Arrays.asList(statuses).size() > i; i++) {
			list.add(Arrays.asList(statuses).get(i).toString());
		}

		if (list.isEmpty()) {
			list.add("AUTORIZADA");
			list.add("AUTORIZADACORRECAO");
			list.add("CANCELADA");
			list.add("INUTILIZADA");
		} else {
			if (list.contains("AUTORIZADA")) {
				list.add("AUTORIZADACORRECAO");
			}
		}

		parametros.put("status", list);
		ExecutorRelatorio executor;
		if (this.getRelatorio().equals(TipoRelatorio.NFE)) {

			filtrados = this.nfes.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
					statuses);

			if (filtrados.isEmpty()) {
				throw new NegocioException("Nenhuma NF-e emitida para o período definido.");
			}

			executor = new ExecutorRelatorio("/relatorios/relatorio_pedidos_emitidos.jasper", this.response, parametros,
					"NF-e emitidas.pdf");
		} else {

			filtrados = this.nfces.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
					statuses);

			if (filtrados.isEmpty()) {
				throw new NegocioException("Nenhuma NFC-e emitida para o período definido.");
			}

			executor = new ExecutorRelatorio("/relatorios/relatorio_nfce.jasper", this.response, parametros,
					"NFC-e emitidas.pdf");
		}

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}

	}

	public void expotarXml() {
		this.appZip.init();
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	public void pesquisa() {

		BigDecimal total = BigDecimal.ZERO;

		if (this.getRelatorio().equals(TipoRelatorio.NFE)) {
			filtrados = this.nfes.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
					statuses);

			if (filtrados.isEmpty()) {
				throw new NegocioException("Nenhuma NF-e emitida para o período definido.");
			}

			for (Object item : filtrados) {

				Nfe nfe = (Nfe) item;

				total = total.add(nfe.getValorTotalNota());
			}

		} else {
			filtrados = this.nfces.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
					statuses);

			if (filtrados.isEmpty()) {
				throw new NegocioException("Nenhuma NFC-e emitida para o período definido.");
			}

			for (Object item : filtrados) {

				NFCe nfe = (NFCe) item;

				total = total.add(nfe.getValorTotal());
			}
		}

		this.setTotal(total.setScale(2));

	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}

	public TipoRelatorio getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(TipoRelatorio relatorio) {
		this.relatorio = relatorio;
	}

	public TipoRelatorio[] getTipoRelatorio() {
		return TipoRelatorio.values();
	}

	public StatusNotas[] getStatus() {
		return StatusNotas.values();
	}

	public boolean isNotRegistros() {
		return this.filtrados.isEmpty();
	}

	private void limparDiretorio(String caminho) {
		File folder = new File(caminho);
		if (folder.isDirectory()) {
			File[] sun = folder.listFiles();
			for (File toDelete : sun) {
				toDelete.delete();
			}
		}
	}

	public void escreveArquivos() {

		BigDecimal total = BigDecimal.ZERO;
		limparDiretorio(this.getRaizProjeto() + "resources/xml/xml");
		limparDiretorio(this.getRaizProjeto() + "resources/xml/xml.zip");

		try {

			if (this.getRelatorio().equals(TipoRelatorio.NFE)) {
				filtrados = this.nfes.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
						statuses);

				for (Object item : filtrados) {

					Nfe nfe = (Nfe) item;

					if (nfe.getChave() != null) {
						total = total.add(nfe.getValorTotalNota());

						System.out.println(nfe.getChave());

						FileWriter fw = new FileWriter(
								this.getRaizProjeto() + "resources/xml/xml/" + nfe.getChave() + ".xml");
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(nfe.getXml());
						bw.close();
					}


				}

			} else {
				filtrados = this.nfces.pesquisaRelatorio(usuarioLogado.getUsuario().getEmpresa(), dataInicio, dataFim,
						statuses);

				for (Object item : filtrados) {

					NFCe nfe = (NFCe) item;

					total = total.add(nfe.getValorTotal());

					System.out.println(nfe.getChave());

					FileWriter fw = new FileWriter(
							this.getRaizProjeto() + "resources/xml/xml/" + nfe.getChave() + ".xml");
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(nfe.getXml());
					bw.close();
				}
			}

			this.setTotal(total.setScale(2));

			this.appZip.init();
			downloadDicionario();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

//	private String ajustaXmlParaDanfe(String texto) {
//		texto = texto.replaceAll("enviNFe", "nfeProc");
//		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");
//
//		return texto;
//	}

	@SuppressWarnings("deprecation")
	public void downloadDicionario() {
		try {
			// OutputStream out = null;
			String filename = getRelatorio().getDescricao() + ".zip";
			File file = new File(this.getRaizProjeto() + "resources/xml/xml.zip");

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			// out = response.getOutputStream();

			response.setContentType("binary/data");
			response.addHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentLength((int) file.length());

			FileInputStream fileInputStream = new FileInputStream(file);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}

			responseOutputStream.flush();
			responseOutputStream.close();

			fileInputStream.close();

			messages.info("Download do xml realizado com sucesso!");

			RequestContext.getCurrentInstance().update(Arrays.asList("frm:msg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}