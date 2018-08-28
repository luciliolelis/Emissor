package com.amsoft.erp.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class AmsoftUtils {

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public static void error(String error) {
		System.err.println("| ERROR: " + error);
	}

	public static void info(String info) {
		System.out.println("| INFO: " + info);
	}

	public static String removerMascara(String str) {

		if (str.equals("") || str == null)
			return null;

		str = str.replace(" ", "");
		str = str.replace(".", "");
		str = str.replace("/", "");
		str = str.replace("-", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		return str;
	}

	public static String getPastaXml() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "resources/xml/";
	}

	public static void escritorXmlToDanfe(Nfe nfe) {
		try {
			FileWriter fw = new FileWriter(AmsoftUtils.getPastaXml() + "nfe.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			String texto = ajustaXmlParaDanfe(nfe.getXml());
			AmsoftUtils.info(AmsoftUtils.formataXML(texto));
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public static void escritorXmlToDanfeNFCe(NFCe nfce) {
		try {
			FileWriter fw = new FileWriter(AmsoftUtils.getPastaXml() + "nfe.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			String texto = nfce.getXml();
			AmsoftUtils.info(AmsoftUtils.formataXML(texto));
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public static String getRaiz() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	public static String getPastaRelatorios() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "relatorios/";
	}

	public static boolean isNotCPF(Cliente cliente) {
		return "00000000000".equals(removerMascara(cliente.getDocReceitaFederal()));
	}

	public static BigDecimal dif(BigDecimal x, BigDecimal y) {
		return x.subtract(y);
	}

	public static boolean isProdutoValido(ItemProduto item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	public static boolean isSimplesNacional(ItemProduto itemProjeto) {
		return AmsoftUtils.isSimplesNacional(itemProjeto.getProduto().getEmpresa().getRegimeTributario());
	}

	public static String lpadTo(String input, int width, char ch) {
		String strPad = "";

		StringBuffer sb = new StringBuffer(input.trim());
		while (sb.length() < width)
			sb.insert(0, ch);
		strPad = sb.toString();

		if (strPad.length() > width) {
			strPad = strPad.substring(0, width);
		}
		return strPad;
	}

	public static boolean isRegimeNormal(RegimeTributario regimeTributario) {
		return regimeTributario.equals(RegimeTributario.NORMAL);
	}

	public static boolean isPessoaFisica(Cliente cliente) {
		return cliente.getTipoPessoa().equals(TipoPessoa.FISICA);
	}

	public static boolean isSimplesNacional(RegimeTributario regimeTributario) {
		return regimeTributario.equals(RegimeTributario.SIMPLES);
	}

	public static String emptyToNull(String str) {

		if (str == null)
			return str;

		if (str.equals(""))
			return null;

		return str;
	}

	public static boolean isHomologacao(Nfe nfe) {
		if (nfe.getUsuario().getEmpresa().getWsAmbiente().equals(2)) {
			return true;
		}
		return false;
	}

	public static boolean isHomologacao(NFCe nfe) {
		if (nfe.getUsuario().getEmpresa().getWsAmbiente().equals(2)) {
			return true;
		}
		return false;
	}

	public static boolean isHomologacao(Empresa empresa) {
		if (empresa.getWsAmbiente().equals(2)) {
			return true;
		}
		return false;
	}

	public boolean isHomologacao() {
		if (usuarioLogado.getUsuario().getEmpresa().getWsAmbiente().equals(2)) {
			return true;
		}
		return false;
	}

	public static boolean isMaiorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) > 0);
	}

	public static boolean isMenorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) < 0);
	}

	public static boolean isNotBigDecimalZeroOrNull(BigDecimal num) {
		return num != null && isMaiorQZero(num);
	}

	public static boolean isNotZero(BigDecimal valor) {
		return !isZero(valor);
	}

	public static boolean isZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) == 0;
	}

	public static String getAnoDuasPosicoes(Date data) {
		String ret = "";
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yy");
			ret = dt.format(data);
		} catch (Exception e) {
			ret = "";
			throw new RuntimeException(e);
		}

		return ret;
	}

	public static String formataXML(String xml) {

		try {
			final InputSource src = new InputSource(new StringReader(xml));
			final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src)
					.getDocumentElement();
			final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));
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

	public static String removeCaracteresEspeciais(String text) {
		String passa = text;
		passa = passa.replaceAll("[ÂÀÁÄÃ]", "A");
		passa = passa.replaceAll("[âãàáä]", "a");
		passa = passa.replaceAll("[ÊÈÉË]", "E");
		passa = passa.replaceAll("[êèéë]", "e");
		passa = passa.replaceAll("ÎÍÌÏ", "I");
		passa = passa.replaceAll("îíìï", "i");
		passa = passa.replaceAll("í", "i");
		passa = passa.replaceAll("[ÔÕÒÓÖ]", "O");
		passa = passa.replaceAll("[ôõòóö]", "o");
		passa = passa.replaceAll("[ÛÙÚÜ]", "U");
		passa = passa.replaceAll("[ûúùü]", "u");
		passa = passa.replaceAll("Ç", "C");
		passa = passa.replaceAll("ç", "c");
		passa = passa.replaceAll("[ýÿ]", "y");
		passa = passa.replaceAll("Ý", "Y");
		passa = passa.replaceAll("ñ", "n");
		passa = passa.replaceAll("Ñ", "N");
		passa = passa.replaceAll("°", "");
		// passa = passa.replaceAll("[-+=*&amp;%$#@!_]", "");
		passa = passa.replaceAll("['\"]", "");
		passa = passa.replaceAll("[<>()\\{\\}]", "");
		passa = passa.replaceAll("['\\\\.,()|/]", "");
		passa = passa.replaceAll("[^!-ÿ]{1}[^ -ÿ]{0,}[^!-ÿ]{1}|[^!-ÿ]{1}", " ");
		return passa;
	}

	public String removeCaracteresEspeciaisDadosAdicionais(String text) {
		String passa = text;
		passa = passa.replaceAll("[ÂÀÁÄÃ]", "A");
		passa = passa.replaceAll("[âãàáä]", "a");
		passa = passa.replaceAll("[ÊÈÉË]", "E");
		passa = passa.replaceAll("[êèéë]", "e");
		passa = passa.replaceAll("ÎÍÌÏ", "I");
		passa = passa.replaceAll("îíìï", "i");
		passa = passa.replaceAll("í", "i");
		passa = passa.replaceAll("[ÔÕÒÓÖ]", "O");
		passa = passa.replaceAll("[ôõòóö]", "o");
		passa = passa.replaceAll("[ÛÙÚÜ]", "U");
		passa = passa.replaceAll("[ûúùü]", "u");
		passa = passa.replaceAll("Ç", "C");
		passa = passa.replaceAll("ç", "c");
		passa = passa.replaceAll("[ýÿ]", "y");
		passa = passa.replaceAll("Ý", "Y");
		passa = passa.replaceAll("ñ", "n");
		passa = passa.replaceAll("Ñ", "N");
		passa = passa.replaceAll("°", "");
		// passa = passa.replaceAll("[-+=*&amp;%$#@!_]", "");
		passa = passa.replaceAll("['\"]", "");
		passa = passa.replaceAll("[<>()\\{\\}]", "");
		passa = passa.replaceAll("[^!-ÿ]{1}[^ -ÿ][^!-ÿ]{1}|[^!-ÿ]{1}", " ");
		return passa;
	}

	public String removeEspacoFinal(String text) {

		text = text.replaceAll("\\s+$", "");
		return text;
	}

	public static Boolean isVogal(String s) {

		s = s.substring(s.length() - 1, s.length());
		return s.equals("a") || s.equals("e") || s.equals("i") || s.equals("o") || s.equals("u");
	}

	public static Boolean isNotVogal(String s) {

		return !isVogal(s);
	}

	public static void escritorXml(NFCe nfCe) {
		try {
			FileWriter fw = new FileWriter(AmsoftUtils.getPastaXml() + nfCe.getChave() + ".xml");
			BufferedWriter bw = new BufferedWriter(fw);
			String texto = ajustaXmlParaDanfe(nfCe.getXml());
			bw.write(texto);
			bw.close();

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private static String ajustaXmlParaDanfe(String texto) {
		texto = texto.replaceAll("enviNFe", "nfeProc");
		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");

		return texto;
	}

	public static void exibirFoto(Nfe nfe) {

		byte[] someByteArray = nfe.getEmpresa().getLogo();

		try {
			FileOutputStream fos = new FileOutputStream(
					AmsoftUtils.getRaiz() + "relatorios/" + AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj()) + ".jpg");
			fos.write(someByteArray);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
