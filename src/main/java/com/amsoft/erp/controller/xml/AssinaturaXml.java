package com.amsoft.erp.controller.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.nfe.Nfe;

public class AssinaturaXml implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String NFE = "NFe";

	private PrivateKey privateKey;
	private KeyInfo keyInfo;

	public String assinarA1(File nfeProc, Nfe nfe) {
		try {
			String xmlNFe = lerXML(nfeProc);
			String caminhoDoCertificadoDoCliente = this.getCertificado(nfe.getEmpresa());
			String senhaDoCertificadoDoCliente = nfe.getEmpresa().getSenhaCertificado();
			return this.assinaEnviNFe(xmlNFe, caminhoDoCertificadoDoCliente, senhaDoCertificadoDoCliente);
		} catch (Exception e) {
			UtilXml.error("| " + e.toString());
		}
		return "";
	}

	public String assinarDevolucaoA1(String xml, Nfe nfe) {
		try {
			String caminhoDoCertificadoDoCliente = this.getCertificado(nfe.getEmpresa());
			String senhaDoCertificadoDoCliente = nfe.getEmpresa().getSenhaCertificado();
			return this.assinaEnviNFe(xml, caminhoDoCertificadoDoCliente, senhaDoCertificadoDoCliente);
		} catch (Exception e) {
			UtilXml.error("| " + e.toString());
		}
		return "";
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	public String getCertificado(Empresa empresa) {
		return getRaizProjeto() + "resources/" + empresa.getNomeCertificado();
	}

	public String assinaEnviNFe(String xml, String certificado, String senha) throws Exception {

		Document document = documentFactory(xml);

		XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

		ArrayList<Transform> transformList = signatureFactory(signatureFactory);

		loadCertificates(certificado, senha, signatureFactory);

		for (int i = 0; i < document.getDocumentElement().getElementsByTagName(NFE).getLength(); i++) {
			assinarNFe(signatureFactory, transformList, privateKey, keyInfo, document, i);
		}

		return outputXML(document);
	}

	public static Document loadXML(String xml) throws Exception {
		DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
		DocumentBuilder bldr = fctr.newDocumentBuilder();
		InputSource insrc = new InputSource(new StringReader(xml));
		return bldr.parse(insrc);
	}

	

	private void assinarNFe(XMLSignatureFactory fac, ArrayList<Transform> transformList, PrivateKey privateKey,
			KeyInfo ki, Document document, int indexNFe) throws Exception {

		NodeList elements = document.getElementsByTagName("infNFe");

		org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(indexNFe);

		String id = el.getAttribute("Id");
		el.setIdAttribute("Id", true);

		Reference ref = fac.newReference("#" + id, fac.newDigestMethod(DigestMethod.SHA1, null), transformList, null,
				null);

		SignedInfo si = fac.newSignedInfo(
				fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
				fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		XMLSignature signature = fac.newXMLSignature(si, ki);

		DOMSignContext dsc = new DOMSignContext(privateKey,
				document.getDocumentElement().getElementsByTagName(NFE).item(indexNFe));

		signature.sign(dsc);
	}

	private ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

		ArrayList<Transform> transformList = new ArrayList<Transform>();

		TransformParameterSpec tps = null;

		Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);

		Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);

		transformList.add(envelopedTransform);
		transformList.add(c14NTransform);
		return transformList;
	}

	private Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));

		return document;
	}

	private void loadCertificates(String certificado, String senha, XMLSignatureFactory signatureFactory)
			throws Exception {

		InputStream entrada = new FileInputStream(certificado);
		KeyStore ks = KeyStore.getInstance("pkcs12");

		try {
			ks.load(entrada, senha.toCharArray());
		} catch (IOException e) {
			throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
		}

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();
		while (aliasesEnum.hasMoreElements()) {
			String alias = (String) aliasesEnum.nextElement();
			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
						new KeyStore.PasswordProtection(senha.toCharArray()));
				privateKey = pkEntry.getPrivateKey();
				break;
			}
		}

		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
		UtilXml.info("SubjectDN: " + cert.getSubjectDN().toString());

		KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

		x509Content.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
		keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
	}

	public void loadCertificatesCliente(String certificado, String senha, XMLSignatureFactory signatureFactory)
			throws FileNotFoundException, KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, UnrecoverableEntryException, NoSuchProviderException {

		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI"); // MODIFICADO

		ks.load(null, null); // MODIFICADO

		KeyStore.PrivateKeyEntry pkEntry = null;
		Enumeration<String> aliasesEnum = ks.aliases();

		while (aliasesEnum.hasMoreElements()) {
			String alias = certificado; // Aqui é informado o Alias (Nome
										// amigável do certificado digital)

			if (ks.isKeyEntry(alias)) {
				pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
						new KeyStore.PasswordProtection(senha.toCharArray()));
				privateKey = pkEntry.getPrivateKey();
				break;
			}
		}

		X509Certificate cert = (X509Certificate) pkEntry.getCertificate();
		UtilXml.info("SubjectDN: " + cert.getSubjectDN().toString());
		KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
		List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

		x509Content.add(cert);
		X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
		keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
	}

	private String outputXML(Document doc) throws TransformerException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
		String xml = os.toString();
		if ((xml != null) && (!"".equals(xml))) {
			xml = xml.replaceAll("\\r\\n", "");
			xml = xml.replaceAll(" standalone=\"no\"", "");
		}

		return xml;
	}

	private static String lerXML(File fileXML) throws IOException {
		String linha = "";
		StringBuilder xml = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileXML)));

		while ((linha = in.readLine()) != null) {
			xml.append(linha);
		}

		in.close();

		UtilXml.info(xml.toString());

		return xml.toString();
	}

}
