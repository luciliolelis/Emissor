package com.amsoft.erp.controller.nfce;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.NegocioException;

import br.com.samuelweb.nfe.exception.NfeException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

@Named
@RequestScoped
public class EnvioNFCeEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfe;

	public void enviarNFe() {

		if (isNotEmailDestinatario(this.nfe.getCliente()))
			throw new NegocioException("Cliente não tem endereço de email cadastrado.");

		try {

			escritorXml(this.nfe.getXml());
			gerarDanfe();

			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(getRaizProjeto() + "resources/xml/nfe.xml");
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("NFC-e");
			attachment.setName(this.nfe.getChave() + ".xml");

			EmailAttachment attachmentDanfe = new EmailAttachment();
			attachmentDanfe.setPath(getRaizProjeto() + "relatorios/nfe.pdf");
			attachmentDanfe.setDisposition(EmailAttachment.ATTACHMENT);
			attachmentDanfe.setDescription("NFC-e");
			attachmentDanfe.setName(this.nfe.getChave() + ".pdf");

			// Create the email message
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("projeto@amsoft.com.br", "proj1122!@1"));
			email.setSSLOnConnect(true);
			email.addTo(this.nfe.getCliente().getEmail(), this.nfe.getCliente().getNome());
			email.setFrom("projeto@amsoft.com.br", "Enviado pelo emissor de NFC-e AMC");
			email.setSubject(this.nfe.getChave().toString());
			email.setMsg(motarCorpoEmail(nfe));

			// add the attachment
			email.attach(attachment);

			email.attach(attachmentDanfe);

			// send the email
			email.send();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"NFC-e enviado por e-mail com sucesso!", ""));
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	private String motarCorpoEmail(NFCe nfe) {

		String msg = "";
		msg = "Prezado Cliente " + nfe.getCliente().getNome() + ",";
		msg = msg + "\n\n" + "Segue anexada a Nota Fiscal de Consumidor Eletrônica nº " + nfe.getNumero()
				+ " emitida pela empresa :";
		msg = msg + "\n\n" + "Razão Social : " + nfe.getEmpresa().getRazao_social();
		msg = msg + "\n" + "Cidade : " + nfe.getEmpresa().getCidade() + " - "
				+ nfe.getEmpresa().getUf();
		msg = msg + "\n" + "CNPJ : " + nfe.getEmpresa().getCnpj();
		return msg;
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

	public void gerarDanfe() throws FileNotFoundException, IOException, NfeException {

		try {
			String arquivo = getRaizProjeto() + "resources/xml/nfe.xml";

			String relatorio = (getRaizProjeto() + "relatorios/DanfeNFCe.jasper");

			JRXmlDataSource xml = new JRXmlDataSource(arquivo, "/");

			HashMap<String, Object> params = new HashMap<String, Object>();

			params.put("UrlConsulta", "http://www.fazenda.pr.gov.br/");

			params.put("REPORT_LOCALE", new Locale("pt", "BR"));
			JasperPrint rel = JasperFillManager.fillReport(relatorio, params, xml);

			String pdf = getRaizProjeto() + "relatorios/nfe.pdf";
			JasperExportManager.exportReportToPdfFile(rel, pdf);

		} catch (JRException e) {

			e.printStackTrace();
		}
	}

	private String ajustaXmlParaDanfe(String texto) {
		texto = texto.replaceAll("enviNFe", "nfeProc");
		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");

		return texto;
	}

	boolean isEmailDestinatario(Cliente cliente) {
		return cliente != null && cliente.getEmail() != null && !cliente.getEmail().equals("");
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
