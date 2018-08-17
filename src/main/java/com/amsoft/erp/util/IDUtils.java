package com.amsoft.erp.util;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.EnderecoEntrega;
import com.amsoft.erp.model.nfe.FinalidadeOperacao;
import com.amsoft.erp.model.nfe.Nfe;

public class IDUtils {

	public static String getDestino(Nfe nfe) {

		Empresa empresa = nfe.getEmpresa();
		EnderecoEntrega endereco = nfe.getEnderecoEntrega();
		Cliente cliente = nfe.getCliente();

		if (endereco.getUf().equals(empresa.getUf())) {
			return "1";
		} else if (cliente.getEstrangeiro().equals(true)) {
			return "3";
		} else {
			return "2";
		}
	}

	public static String getDestino(NFCe nfe) {

		// Empresa empresa = nfe.getEmpresa();
		// EnderecoEntrega endereco = nfe.getEnderecoEntrega();
		// Cliente cliente = nfe.getCliente();
		//
		// if (endereco.getUf().equals(empresa.getUf())) {
		// return "1";
		// } else if (cliente.getEstrangeiro().equals(true)) {
		// return "3";
		// } else {
		// return "2";
		// }
		return "1";
	}

	public static String getProcessoEmissao(Nfe nfe) {
		return "0";
	}

	public static String getInficadorPresenca(Nfe nfe) {
		return "1";
	}

	public static String getConsumidorFinal(Nfe nfe) {

		if (nfe.isVendaConsumidorFinal()) {
			return "1";
		}

		return "0";
	}

	public static String getFinalidadeEmissao(Nfe nfe) {

		if (isNormal(nfe)) {
			return "1";
		} else if (isComplementar(nfe)) {
			return "2";
		} else if (isAjuste(nfe)) {
			return "3";
		} else if (isDevolucao(nfe)) {
			return "4";
		}

		return "";
	}

	public static String getFinalidadeEmissao(NFCe nfe) {

		return "1";

	}

	public static String getIndicadorIEDestinatario(Nfe nfe) {

		if (nfe.isVendaConsumidorFinal()) {
			return "9";
		}
		return "1";

	}

	public static String getIndicadorIEDestinatario(NFCe nfe) {

		// if (nfe.isVendaConsumidorFinal()) {
		return "9";
		// }
		// return "1";

	}

	static boolean isNormal(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.NORMAL;
	}

	static boolean isComplementar(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.COMPLEMENTA;
	}

	static boolean isDevolucao(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.DEVOLUCAO;
	}

	static boolean isAjuste(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.AJUSTE;
	}

	public static String getTipoEmissao(Nfe pedido) {
		return "1";
	}

	public static String getTipoEmissao(NFCe pedido) {
		return "1";
	}

	public static String getAmbiente(Empresa empresa) {

		if (AmsoftUtils.isHomologacao(empresa)) {
			return "2";
		} else {
			return "1";
		}

	}

	public static String retornaDataNFCe(Date dataASerFormatada) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dataASerFormatada);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
			xmlCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

			return (xmlCalendar.toString());
		} catch (DatatypeConfigurationException ex) {
			Logger.getLogger(ex.getLocalizedMessage());
		}
		return null;
	}

}
