package com.amsoft.erp.controller.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

public class UtilXml implements Serializable {

	public static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	static String removerMascara(String str) {

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


	static void error(String error) {
		System.out.println("| ERROR: " + error);
	}

	static void info(String info) {
		System.out.println("| INFO: " + info);
	}

	String getDiretorioXml() {
		@SuppressWarnings("deprecation")
		String pathSystem = request.getRealPath(request.getContextPath());
		String path = new File(pathSystem).getParent();
		return path = path + "\\relatorios\\file.xml";
	}

	static String formataData(Date data) {
		String ret = "";
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			ret = dt.format(data);
		} catch (Exception e) {
			ret = "";
		}

		return ret;

	}

	void escritor(String path, File file, String texto) {

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(texto);
			bw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public String format(String xml) {
		try {
			final InputSource src = new InputSource(new StringReader(xml));

			final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src)
					.getDocumentElement();

			final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

			// May need this:
			System.setProperty(DOMImplementationRegistry.PROPERTY,
					"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");

			final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

			final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");

			final LSSerializer writer = impl.createLSSerializer();

			writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
			writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

			return writer.writeToString(document);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	

}
