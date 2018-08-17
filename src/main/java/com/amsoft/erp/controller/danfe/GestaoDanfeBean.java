package com.amsoft.erp.controller.danfe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

@ManagedBean
public class GestaoDanfeBean {

	private UploadedFile uploadedFile;
	private StreamedContent filePdf;

	public void upload() {

		try {

			String arquivo = AmsoftUtils.getPastaXml() + uploadedFile.getFileName();
			String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";
			String relatorio = AmsoftUtils.getPastaRelatorios() + "NFC.jasper";

			File file = new File(arquivo);

			OutputStream out = new FileOutputStream(file);
			out.write(uploadedFile.getContents());
			out.close();

			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/");

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("UrlConsulta", "http://www.fazenda.pr.gov.br/");
			params.put("REPORT_LOCALE", new Locale("pt", "BR"));

			JasperPrint rel = JasperFillManager.fillReport(relatorio, params, xml);

			JasperExportManager.exportReportToPdfFile(rel, pdf);

			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");

			filePdf = new DefaultStreamedContent(stream, "file/pdf", "nfe.pdf");

		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao gerar o danfe! verifique se o aquivo selecionado é válido.");
		}

	}

	public void upload(NFCe nfCe) {
		try {
			AmsoftUtils.info(nfCe.getId().toString());
			AmsoftUtils.escritorXml(nfCe);

			String arquivo = AmsoftUtils.getPastaXml() + nfCe.getChave() + ".xml";
			String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";

			String relatorio = "";

			if (nfCe.getXml().contains("3.10")) {
				relatorio = AmsoftUtils.getPastaRelatorios() + "NFC310.jasper";
			} else {
				relatorio = AmsoftUtils.getPastaRelatorios() + "NFC.jasper";
			}

			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/");

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("UrlConsulta", "http://www.fazenda.pr.gov.br/");
			params.put("REPORT_LOCALE", new Locale("pt", "BR"));

			JasperPrint rel = JasperFillManager.fillReport(relatorio, params, xml);

			JasperExportManager.exportReportToPdfFile(rel, pdf);

			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");

			filePdf = new DefaultStreamedContent(stream, "file/pdf", "nfe.pdf");

		} catch (Exception e) {
			FacesUtil.addErrorMessage("Erro ao gerar o danfe! verifique se o aquivo selecionado é válido.");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void upload(Nfe nfe) throws JRException {
		AmsoftUtils.info(nfe.getChave());

		AmsoftUtils.escritorXmlToDanfe(nfe);

		String arquivo = AmsoftUtils.getPastaXml() + "nfe.xml";
		String relatorio = (AmsoftUtils.getPastaRelatorios() + "danfe1.jasper");
		JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/nfeProc/NFe/infNFe/det");

		HashMap mapa = new HashMap();

		AmsoftUtils.exibirFoto(nfe);

		mapa.put("Logo",
				AmsoftUtils.getPastaRelatorios() + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
		mapa.put("REPORT_LOCALE", new Locale("pt", "BR"));

		JasperPrint print = JasperFillManager.fillReport(relatorio, mapa, xml);

		String pdf = AmsoftUtils.getRaiz() + "relatorios/nfe.pdf";
		JasperExportManager.exportReportToPdfFile(print, pdf);

		InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
				.getResourceAsStream("relatorios/nfe.pdf");

		filePdf = new DefaultStreamedContent(stream, "file/pdf", "nfe.pdf");
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public StreamedContent getFilePdf() {
		return filePdf;
	}

	public void setFilePdf(StreamedContent filePdf) {
		this.filePdf = filePdf;
	}
}