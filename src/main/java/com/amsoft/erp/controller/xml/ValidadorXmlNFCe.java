package com.amsoft.erp.controller.xml;

import java.io.Serializable;

import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.nfce.FormaPagamentoNFCe;
import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfce.NFCe;

public class ValidadorXmlNFCe implements Serializable {

	public static final long serialVersionUID = 1L;

	static boolean isOperacaoInterna(NFCe nfe) {
		return nfe.getEmpresa().getUf().equals(nfe.getEnderecoEntrega().getUf());
	}

	static boolean isSimplesNacional(ItemProdutoNFCe item) {
		return item.getProduto().getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	static boolean isClienteEstrangeiro(NFCe nfe) {
		return (nfe.getCliente().getEstrangeiro().equals(true) || nfe.getCliente().getExterior().equals(true));
	}

	static boolean isClientePessoaFisica(NFCe nfe) {
		return nfe.getCliente().getTipoPessoa() == TipoPessoa.FISICA;
	}

	static boolean isProdutoValido(ItemProdutoNFCe item) {
		return item.getId() != null;
	}

	static boolean isSimplesNacional(NFCe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	static boolean isSimplesComExcesso(NFCe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES_EXCESSAO;
	}

	static boolean isRegimeNormal(NFCe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.NORMAL;
	}

	static boolean isAvista(NFCe nfe) {
		return nfe.getFormaPagamento() == FormaPagamentoNFCe.DINHEIRO;
	}

	static boolean isAPrazo(NFCe nfe) {
		return nfe.getFormaPagamento() == FormaPagamentoNFCe.DINHEIRO;
	}
	
}
