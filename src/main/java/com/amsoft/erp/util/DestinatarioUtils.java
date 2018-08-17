package com.amsoft.erp.util;

import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEndereco;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TUf;

public class DestinatarioUtils {

	public static TEndereco getEnderecoDestinatario(Nfe nfe) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(nfe.getEnderecoEntrega().getLogradouro());
		endereco.setNro(nfe.getEnderecoEntrega().getNumero());
		endereco.setXCpl(AmsoftUtils.emptyToNull(nfe.getEnderecoEntrega().getComplemento()));
		endereco.setXBairro(nfe.getEnderecoEntrega().getBairro());
		endereco.setCMun(nfe.getEnderecoEntrega().getIbgeCidade());
		endereco.setXMun(nfe.getEnderecoEntrega().getCidade());
		endereco.setUF(TUf.valueOf(nfe.getEnderecoEntrega().getUf()));
		endereco.setCEP(AmsoftUtils.removerMascara(nfe.getEnderecoEntrega().getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		if (!nfe.getCliente().getTelefone().isEmpty()) {
			endereco.setFone(AmsoftUtils.removerMascara(nfe.getCliente().getTelefone()));
		}

		return endereco;
	}

	public static TEndereco getEnderecoDestinatario(NFCe nfe) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(nfe.getEnderecoEntrega().getLogradouro());
		endereco.setNro(nfe.getEnderecoEntrega().getNumero());
		endereco.setXCpl(AmsoftUtils.emptyToNull(nfe.getEnderecoEntrega().getComplemento()));
		endereco.setXBairro(nfe.getEnderecoEntrega().getBairro());
		endereco.setCMun(nfe.getEnderecoEntrega().getIbgeCidade());
		endereco.setXMun(nfe.getEnderecoEntrega().getCidade());
		endereco.setUF(TUf.valueOf(nfe.getEnderecoEntrega().getUf()));
		endereco.setCEP(AmsoftUtils.removerMascara(nfe.getEnderecoEntrega().getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		if (!nfe.getCliente().getTelefone().isEmpty()) {
			endereco.setFone(AmsoftUtils.removerMascara(nfe.getCliente().getTelefone()));
		}

		return endereco;
	}
	
	public static TEndereco getEnderecoDestinatario(Empresa empresa) {

		TEndereco endereco = new TEndereco();

		endereco.setXLgr(empresa.getLogradouro());
		endereco.setNro(empresa.getNumero());
		endereco.setXCpl(AmsoftUtils.emptyToNull(empresa.getComplemento()));
		endereco.setXBairro(empresa.getBairro());
		endereco.setCMun(empresa.getIbgeCidade());
		endereco.setXMun(empresa.getCidade());
		endereco.setUF(TUf.valueOf(empresa.getUf()));

		endereco.setCEP(AmsoftUtils.removerMascara(empresa.getCep()));
		endereco.setCPais("1058");
		endereco.setXPais("BRASIL");

		endereco.setFone(AmsoftUtils.removerMascara(empresa.getFone()));

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
