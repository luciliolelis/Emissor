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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jpa.Transactional;
import com.amsoft.erp.util.jsf.FacesUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

@Named
@RequestScoped
public class BookRatingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Nfe nfe;

	@Inject
	private Nfes nfes;

	private String bookName;

	private StreamedContent file;

	public StreamedContent getFile() {
		return file;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			System.out.println(bookName);

		}
	}

	@Transactional
	public void enviarNFe() {
		nfe = this.nfes.porId(new Long(bookName));
		if (isNotEmailDestinatario(this.nfe.getCliente()))
			throw new NegocioException("Cliente não tem endereço de email cadastrado.");

		try {

			// escritorXml(this.nfe.getXml());
			gerarDanfe();

			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(getRaizProjeto() + "resources/xml/nfe.xml");
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("NFe");
			attachment.setName(this.nfe.getChave() + ".xml");

			EmailAttachment attachmentDanfe = new EmailAttachment();
			attachmentDanfe.setPath(getRaizProjeto() + "relatorios/nfe.pdf");
			attachmentDanfe.setDisposition(EmailAttachment.ATTACHMENT);
			attachmentDanfe.setDescription("NFe");
			attachmentDanfe.setName(this.nfe.getChave() + ".pdf");

			// Create the email message
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("projeto@amsoft.com.br", "proj1122!@1"));
			email.setSSLOnConnect(true);
			email.addTo(this.nfe.getCliente().getEmail(), this.nfe.getCliente().getNome());
			email.setFrom("projeto@amsoft.com.br", "Enviado pelo emissor de NFe AMC");
			email.setSubject(this.nfe.getChave().toString());
			email.setMsg(motarCorpoEmail(nfe));

			// add the attachment
			email.attach(attachment);

			email.attach(attachmentDanfe);

			// send the email
			email.send();

			FacesUtil.addInfoMessage("Email enviado com sucesso!");

		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	private String motarCorpoEmail(Nfe nfe) {

		String msg = "";
		msg = "Prezado Cliente " + nfe.getCliente().getNome() + ",";
		msg = msg + "\n\n" + "Segue anexada a Nota Fiscal Eletrônica nº " + nfe.getNumero() + " emitida pela empresa :";
		msg = msg + "\n\n" + "Razão Social : " + nfe.getEmpresa().getRazao_social();
		msg = msg + "\n" + "Cidade : " + nfe.getEmpresa().getCidade() + " - "
				+ nfe.getEmpresa().getUf();
		msg = msg + "\n" + "CNPJ : " + nfe.getEmpresa().getCnpj();
		return msg;
	}

	@Transactional
	public void danfe() {
		nfe = this.nfes.porId(new Long(bookName));

		try {
			gerarDanfe();
			System.out.println(nfe.getChave());
			InputStream stream = FacesContext.getCurrentInstance().getExternalContext()
					.getResourceAsStream("relatorios/nfe.pdf");
			file = new DefaultStreamedContent(stream, "file/pdf", nfe.getChave() + ".pdf");

		} catch (IOException e) {
			throw new NegocioException("Falha ao gerar danfe");

		}

	}

	@SuppressWarnings("deprecation")
	@Transactional
	public void fechar() {
		RequestContext.getCurrentInstance().closeDialog(0);
	}

	public BookRatingBean() {
		super();
	}

	@SuppressWarnings({ "deprecation" })
	public void onrate(RateEvent rateEvent) {
		RequestContext.getCurrentInstance().closeDialog(rateEvent.getRating());
	}

	@SuppressWarnings("deprecation")
	public void oncancel() {
		RequestContext.getCurrentInstance().closeDialog(0);
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	public void escritorXml(String texto) {
		try {
			FileWriter fw = new FileWriter(getRaizProjeto() + "resources/xml/nfe.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			texto = ajustaXmlParaDanfe(texto);
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void gerarDanfe() throws FileNotFoundException, IOException {

		try {
			escritorXmlToDanfe();
			String arquivo = getRaizProjeto() + "resources/xml/nfe.xml";

			String relatorio = (getRaizProjeto() + "relatorios/danfe1.jasper");
			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/nfeProc/NFe/infNFe/det");

			HashMap mapa = new HashMap();
			//mapa.put("Logo", getRaizProjeto() + "relatorios/logo-nfe.png");
			
			exibirFoto();
			mapa.put("Logo", getRaizProjeto() + "relatorios/" + removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			mapa.put("REPORT_LOCALE", new Locale("pt", "BR"));
			JasperPrint print = JasperFillManager.fillReport(relatorio, mapa, xml);

			String pdf = getRaizProjeto() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(print, pdf);

		} catch (JRException e) {

			e.printStackTrace();
		}
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

	private String ajustaXmlParaDanfe(String texto) {
		texto = texto.replaceAll("enviNFe", "nfeProc");
		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");

		return texto;
	}

	boolean isEmailDestinatario(Cliente cliente) {
		return cliente.getEmail() != null && !cliente.getEmail().equals("");
	}

	boolean isNotEmailDestinatario(Cliente cliente) {
		return !isEmailDestinatario(cliente);
	}
	
	public void exibirFoto() {

		byte[] someByteArray = nfe.getEmpresa().getLogo();

		try {
			FileOutputStream fos = new FileOutputStream(
					getRaizProjeto() + "relatorios/" + removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			fos.write(someByteArray);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
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

}