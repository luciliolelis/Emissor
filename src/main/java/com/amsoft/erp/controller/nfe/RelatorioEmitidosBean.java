package com.amsoft.erp.controller.nfe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.util.ExecutorRelatorio;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class RelatorioEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	@Inject
	@NFeEdicao
	private Nfe nfe;

	public void emitir() {

		escritorXmlToDanfe(nfe);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("Logo", getRaizProjeto() + "relatorios/logo-nfe.png");

		String relatorio = (getRaizProjeto() + "relatorios/danfe1.jasper");

		ExecutorRelatorio executor = new ExecutorRelatorio(relatorio, this.response, parametros,
				"Pedidos emitidos.pdf");

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
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

	private void escritorXmlToDanfe(Nfe nfe) {
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

}
