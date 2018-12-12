package com.amsoft.erp.util;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEndereco;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUf;

public class DestinatarioUtils {

	public static TEndereco getEnderecoDestinatario(Nfe nfe) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(AmsoftUtils.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(nfe.getEnderecoEntrega().getLogradouro())));
		endereco.setNro(nfe.getEnderecoEntrega().getNumero());
		

		//endereco.setXCpl(AmsoftUtils.removeCaracteresEspeciais((nfe.getEnderecoEntrega().getComplemento())));
		
		endereco.setXBairro(AmsoftUtils.removeCaracteresEspeciais(nfe.getEnderecoEntrega().getBairro()));
		endereco.setCMun(nfe.getEnderecoEntrega().getIbgeCidade());
		endereco.setXMun(AmsoftUtils
				.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(nfe.getEnderecoEntrega().getCidade())));
		endereco.setUF(TUf.valueOf(nfe.getEnderecoEntrega().getUf()));
		endereco.setCEP(AmsoftUtils.removerMascara(nfe.getEnderecoEntrega().getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		if (!nfe.getCliente().getTelefone().isEmpty()) {
			endereco.setFone(AmsoftUtils.removerMascara(nfe.getCliente().getTelefone()));
		}

		endereco.setXBairro(AmsoftUtils.removeEspacoFinal(endereco.getXBairro()));

		return endereco;
	}

	public static TEndereco getEnderecoDestinatario(NFCe nfe) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(AmsoftUtils
				.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(nfe.getEnderecoEntrega().getLogradouro())));
		endereco.setNro(nfe.getEnderecoEntrega().getNumero());
		endereco.setXCpl(AmsoftUtils.removeCaracteresEspeciais(
				AmsoftUtils.emptyToNull(AmsoftUtils.removeEspacoFinal(nfe.getEnderecoEntrega().getComplemento()))));
		endereco.setXBairro(AmsoftUtils.removeCaracteresEspeciais(nfe.getEnderecoEntrega().getBairro()));
		endereco.setCMun(nfe.getEnderecoEntrega().getIbgeCidade());
		endereco.setXMun(AmsoftUtils
				.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(nfe.getEnderecoEntrega().getCidade())));
		endereco.setUF(TUf.valueOf(nfe.getEnderecoEntrega().getUf()));
		endereco.setCEP(AmsoftUtils.removerMascara(nfe.getEnderecoEntrega().getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		if (!nfe.getCliente().getTelefone().isEmpty()) {
			endereco.setFone(AmsoftUtils.removerMascara(nfe.getCliente().getTelefone()));
		}

		endereco.setXBairro(AmsoftUtils.removeEspacoFinal(endereco.getXBairro()));

		return endereco;
	}

	public static TEndereco getEnderecoDestinatario(Empresa empresa) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(AmsoftUtils.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(empresa.getLogradouro())));
		endereco.setNro(empresa.getNumero());
		endereco.setXCpl(AmsoftUtils.removeCaracteresEspeciais(
				AmsoftUtils.emptyToNull(AmsoftUtils.removeEspacoFinal(empresa.getComplemento()))));
		endereco.setXBairro(AmsoftUtils.removeCaracteresEspeciais(empresa.getBairro()));
		endereco.setCMun(empresa.getIbgeCidade());
		endereco.setXMun(AmsoftUtils.removeCaracteresEspeciais(AmsoftUtils.removeEspacoFinal(empresa.getCidade())));
		endereco.setUF(TUf.valueOf(empresa.getUf()));

		endereco.setCEP(AmsoftUtils.removerMascara(empresa.getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		endereco.setFone(AmsoftUtils.removerMascara(empresa.getFone()));

		endereco.setXBairro(AmsoftUtils.removeEspacoFinal(endereco.getXBairro()));

		return endereco;
	}

	public static String getIE(Nfe nfe) {

		if (!nfe.getCliente().getInscricaoEstadual().equals("")
				&& !nfe.getCliente().getInscricaoEstadual().equals(null)) {
			return nfe.getCliente().getInscricaoEstadual();
		}

		return null;

	}

	public static String getIE(Empresa empresa) {

		try {
			if (!empresa.getInscricao_estadual().equals("") && !empresa.getInscricao_estadual().equals(null)) {
				return empresa.getInscricao_estadual();
			}

			return null;

		} catch (Exception e) {
			return null;
		}

	}
}
