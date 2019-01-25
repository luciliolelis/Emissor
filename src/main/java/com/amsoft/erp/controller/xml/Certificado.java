package com.amsoft.erp.controller.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.faces.context.FacesContext;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.vo.DadosCertificado;

public class Certificado implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	static List<String> listarCertificadosCliente() throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException, NoSuchProviderException {

		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");

		ks.load(null, null);

		Enumeration<String> al = ks.aliases();

		List<String> certificados = new ArrayList<String>();
		while (al.hasMoreElements()) {
			String alias = al.nextElement();
			certificados.add(alias);
		}

		for (String cert : certificados) {
			System.out.println(cert);
		}

		return certificados;
	}

	public DadosCertificado getDataValidade(Empresa empresa) throws KeyStoreException, FileNotFoundException,
			IOException, NoSuchAlgorithmException, CertificateException {

		try {
			String caminhoDoCertificadoDoCliente = getCertificado(empresa);
			String senhaDoCertificadoDoCliente = empresa.getSenhaCertificado();

			DadosCertificado ret = new DadosCertificado();
			KeyStore keystore = KeyStore.getInstance(("PKCS12"));
			keystore.load(new FileInputStream(caminhoDoCertificadoDoCliente),
					senhaDoCertificadoDoCliente.toCharArray());

			Enumeration<String> eAliases = keystore.aliases();

			while (eAliases.hasMoreElements()) {
				String alias = (String) eAliases.nextElement();
				Certificate certificado = (Certificate) keystore.getCertificate(alias);

				info("Aliais: " + alias);
				X509Certificate cert = (X509Certificate) certificado;

				info(cert.getSubjectDN().getName());
				info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));
				info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));

				LocalDateTime dataInicial = LocalDateTime.ofInstant(cert.getNotBefore().toInstant(),
						ZoneId.systemDefault());

				LocalDateTime dataFinal = LocalDateTime.ofInstant(cert.getNotAfter().toInstant(),
						ZoneId.systemDefault());

				ret.setDescricao(cert.getSubjectDN().getName());
				ret.setDataInicial(dataInicial);
				ret.setDataFinal(dataFinal);
				ret.setStrDataInicial("Válido a partir de: " + dateFormat.format(cert.getNotBefore()));
				ret.setStrDataFinal("até: " + dateFormat.format(cert.getNotAfter()));

				return ret;
			}
		} catch (Exception e) {
			error(e.toString());
		}

		return new DadosCertificado();
	}

	/**
	 * Error.
	 * 
	 * @param log
	 */
	private static void error(String log) {
		System.out.println("ERROR: " + log);
	}

	/**
	 * Info
	 * 
	 * @param log
	 */
	private static void info(String log) {
		System.out.println("INFO: " + log);
	}

	public String getCertificado(Empresa empresa) {
		return getRaizProjeto() + "resources/" + empresa.getNomeCertificado();
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}
}
