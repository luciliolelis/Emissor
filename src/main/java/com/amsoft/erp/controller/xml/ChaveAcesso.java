package com.amsoft.erp.controller.xml;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.amsoft.erp.model.nfe.Nfe;

public class ChaveAcesso implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String gerarChave(Nfe nfe) {
		try {

			String cUF = nfe.getEmpresa().getIbgeEstado();
			String dataAAMM = this.getAnoMesEmissao(nfe.getEmissao());
			String cnpjCpf = nfe.getEmpresa().getCnpj();
			String mod = nfe.getModelo();
			String serie = nfe.getSerie();

			String tpEmis = "1";

			tpEmis = "1";

			String nNF = nfe.getNumero().toString();
			String cNF = geraCodigoRandomico(nfe);

			StringBuilder chave = new StringBuilder();
			chave.append(lpadTo(cUF, 2, '0'));
			chave.append(lpadTo(dataAAMM, 4, '0'));
			chave.append(lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
			chave.append(lpadTo(mod, 2, '0'));
			chave.append(lpadTo(serie, 3, '0'));
			chave.append(lpadTo(String.valueOf(nNF), 9, '0'));
			chave.append(lpadTo(tpEmis, 1, '0'));
			chave.append(lpadTo(cNF, 8, '0'));
			chave.append(modulo11(chave.toString()));

			chave.insert(0, "NFe");

			info("Chave NF-e: " + chave.toString());

			return chave.toString();

		} catch (Exception e) {
			error(e.toString());
		}
		return null;
	}

	private static String geraCodigoRandomico(Nfe nfe) {
		final Random random = new Random(nfe.getEmissao().getTime());
		return StringUtils.leftPad(String.valueOf(random.nextInt(100000000)), 8, "0");
	}

	private String getAnoMesEmissao(Date data) {
		String ret = "";
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
			ret = dt.format(data).substring(2, 4) + dt.format(data).substring(5, 7);
		} catch (Exception e) {
			ret = "";
		}

		return ret;

	}

	public static int modulo11(String chave) {
		int total = 0;
		int peso = 2;

		for (int i = 0; i < chave.length(); i++) {
			total += (chave.charAt((chave.length() - 1) - i) - '0') * peso;
			peso++;
			if (peso == 10)
				peso = 2;
		}

		int resto = total % 11;
		return (resto == 0 || resto == 1) ? 0 : (11 - resto);
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

	private static void error(String error) {
		System.out.println("| ERROR: " + error);
	}

	private static void info(String info) {
		System.out.println("| INFO: " + info);
	}
}
