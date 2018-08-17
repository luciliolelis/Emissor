package com.amsoft.erp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;

import br.com.samuelweb.nfe.util.ConstantesUtil;

public class ChaveAcesso {

	public static String gerarChave(Nfe nfe, String modelo) {
		try {

			String cUF = nfe.getEmpresa().getIbgeEstado();
			String dataAAMM = getAnoMesEmissao(nfe.getDataEntradaOuSaida());
			String cnpjCpf = AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj());

			String mod = ConstantesUtil.NFE.equals(modelo) ? "55" : "65";

			String serie = "1";
			String tpEmis = "1";

			String nNF = nfe.getNumero().toString();
			String cNF = geraCodigoRandomico(nfe);

			StringBuilder chave = new StringBuilder();
			chave.append(AmsoftUtils.lpadTo(cUF, 2, '0'));
			chave.append(AmsoftUtils.lpadTo(dataAAMM, 4, '0'));
			chave.append(AmsoftUtils.lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
			chave.append(AmsoftUtils.lpadTo(mod, 2, '0'));
			chave.append(AmsoftUtils.lpadTo(serie, 3, '0'));
			chave.append(AmsoftUtils.lpadTo(String.valueOf(nNF), 9, '0'));
			chave.append(AmsoftUtils.lpadTo(tpEmis, 1, '0'));
			chave.append(AmsoftUtils.lpadTo(cNF, 8, '0'));
			chave.append(modulo11(chave.toString()));

			chave.insert(0, "NFe");

			AmsoftUtils.info("Chave NF-e: " + chave.toString());

			return chave.toString();

		} catch (Exception e) {
			AmsoftUtils.error(e.toString());
		}
		return null;
	}
	
	
	public static String gerarChave(NFCe nfe, String modelo) {
		try {

			String cUF = nfe.getEmpresa().getIbgeEstado();
			String dataAAMM = getAnoMesEmissao(nfe.getEmissao());
			String cnpjCpf = AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj());

			String mod = ConstantesUtil.NFE.equals(modelo) ? "55" : "65";

			String serie = "1";
			String tpEmis = "1";

			String nNF = nfe.getNumero().toString();
			String cNF = geraCodigoRandomico(nfe);

			StringBuilder chave = new StringBuilder();
			chave.append(AmsoftUtils.lpadTo(cUF, 2, '0'));
			chave.append(AmsoftUtils.lpadTo(dataAAMM, 4, '0'));
			chave.append(AmsoftUtils.lpadTo(cnpjCpf.replaceAll("\\D", ""), 14, '0'));
			chave.append(AmsoftUtils.lpadTo(mod, 2, '0'));
			chave.append(AmsoftUtils.lpadTo(serie, 3, '0'));
			chave.append(AmsoftUtils.lpadTo(String.valueOf(nNF), 9, '0'));
			chave.append(AmsoftUtils.lpadTo(tpEmis, 1, '0'));
			chave.append(AmsoftUtils.lpadTo(cNF, 8, '0'));
			chave.append(modulo11(chave.toString()));

			chave.insert(0, "NFe");

			AmsoftUtils.info("Chave NF-e: " + chave.toString());

			return chave.toString();

		} catch (Exception e) {
			AmsoftUtils.error(e.toString());
		}
		return null;
	}

	static String geraCodigoRandomico(NFCe nfe) {
		final Random random = new Random(nfe.getEmissao().getTime());
		return StringUtils.leftPad(String.valueOf(random.nextInt(100000000)), 8, "0");
	}
	
	static String geraCodigoRandomico(Nfe nfe) {
		final Random random = new Random(nfe.getDataEntradaOuSaida().getTime());
		return StringUtils.leftPad(String.valueOf(random.nextInt(100000000)), 8, "0");
	}
	

	static String getAnoMesEmissao(Date data) {
		String ret = "";
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
			ret = dt.format(data).substring(2, 4) + dt.format(data).substring(5, 7);
		} catch (Exception e) {
			ret = "";
		}

		return ret;

	}

	static int modulo11(String chave) {
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

	public static String getCodigoChave(String chave) {
		return chave.substring(chave.length() - 9, chave.length() - 1).replaceAll("NFe", "");
	}

	public static String getDVChave(String chave) {
		return chave.substring(chave.length() - 1, chave.length()).replaceAll("NFe", "");
	}

}
