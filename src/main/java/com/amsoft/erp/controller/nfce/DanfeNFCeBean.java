package com.amsoft.erp.controller.nfce;

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

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.nfe.exception.NfeException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

@Named
@RequestScoped
public class DanfeNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfce;

	private StreamedContent file;

	public StreamedContent getFile() {
		return file;
	}

	public void danfe() throws NegocioException {
		try {
			gerarDanfeNFCe();
			AmsoftUtils.info(nfce.getChave());
			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");
			file = new DefaultStreamedContent(stream, "file/pdf", nfce.getChave() + ".pdf");

			FacesUtil.addInfoMessage("Danfe gerado com sucesso");
		} catch (IOException e) {
			throw new NegocioException("Falha ao gerar danfe");
		}

	}
	String removerMascara(String str) {

		if (str.equals("") || str == null)
			return "";

		str = str.replace(" ", "");
		str = str.replace(".", "");
		str = str.replace("/", "");
		str = str.replace("-", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		return str;
	}

	public void gerarDanfeNFCe() throws FileNotFoundException, IOException {

		try {

			AmsoftUtils.escritorXmlToDanfeNFCe(nfce);

			String arquivo = AmsoftUtils.getPastaXml() + "nfe.xml";

			String relatorio = AmsoftUtils.getPastaRelatorios() + "NFC.jasper";

			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/");

			HashMap<String, Object> params = new HashMap<String, Object>();

			params.put("UrlConsulta", "http://www.fazenda.pr.gov.br/");

			params.put("REPORT_LOCALE", new Locale("pt", "BR"));
			JasperPrint rel = JasperFillManager.fillReport(relatorio, params, xml);

			//String pdf = AmsoftUtils.getPastaRelatorios() + "nfe.pdf";
			
			String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(rel, pdf);

		} catch (JRException e) {

			e.printStackTrace();
		}
	}
	
	public void gerarDanfe() throws FileNotFoundException, IOException, NfeException {

		try {
			escritorXmlToDanfe();
			String arquivo = getRaizProjeto() + "resources/xml/nfe.xml";

			String relatorio = (getRaizProjeto() + "relatorios/DanfeNFCe.jasper");

			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/");

			HashMap<String, Object> params = new HashMap<String, Object>();

			params.put("UrlConsulta","http://www.fazenda.pr.gov.br/");

			params.put("REPORT_LOCALE", new Locale("pt", "BR"));
			JasperPrint rel = JasperFillManager.fillReport(relatorio, params, xml);

			String pdf = getRaizProjeto() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(rel, pdf);

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
			String texto = ajustaXmlParaDanfe(nfce.getXml());
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void exibirFoto() {

		byte[] someByteArray = nfce.getEmpresa().getLogo();

		try {
			FileOutputStream fos = new FileOutputStream(
					getRaizProjeto() + "relatorios/" + removerMascara(nfce.getEmpresa().getCnpj()) + ".jpg");
			fos.write(someByteArray);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
