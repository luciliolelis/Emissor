package com.amsoft.erp.util;

import com.amsoft.erp.model.Empresa;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnderEmi;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUfEmi;

public class EmitenteUtils {

	public static TEnderEmi getEnderecoEmitente(Empresa empresa) {

		TEnderEmi enderEmit = new TEnderEmi();
		enderEmit.setXLgr(empresa.getLogradouro());
		enderEmit.setNro(empresa.getNumero());
		enderEmit.setXCpl(AmsoftUtils.emptyToNull(empresa.getComplemento()));
		enderEmit.setXBairro(empresa.getBairro());
		enderEmit.setCMun(empresa.getIbgeCidade());
		enderEmit.setXMun(empresa.getCidade());
		enderEmit.setUF(TUfEmi.valueOf(empresa.getUf()));
		enderEmit.setCEP(AmsoftUtils.removerMascara(empresa.getCep()));
		enderEmit.setCPais("1058");
		enderEmit.setXPais("BRASIL");
		enderEmit.setFone(AmsoftUtils.removerMascara(empresa.getFone()));

		return enderEmit;
	}

	public static String getCodigoRegimeTributario(Empresa empresa) {

		if (AmsoftUtils.isSimplesNacional(empresa.getRegimeTributario())) {
			return "1";
		} else if (AmsoftUtils.isRegimeNormal(empresa.getRegimeTributario())) {
			return "3";
		} else {
			return "2";
		}
	}
}
