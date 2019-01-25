package com.amsoft.erp.controller.xml;

import java.io.Serializable;

import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.nfe.EnderecoTransportador;
import com.amsoft.erp.model.nfe.FinalidadeOperacao;
import com.amsoft.erp.model.nfe.FormaPagamento;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.TipoDocumento;

public class ValidadorXml implements Serializable {

	public static final long serialVersionUID = 1L;

	static boolean isFeteEmitente(Nfe nfe) {
		return nfe.getModalidadeFrete().equals("EMITENTE");
	}

	static boolean isFreteDestinatario(Nfe nfe) {
		return nfe.getModalidadeFrete().equals("DESTINATARIO");
	}

	static boolean isOperacaoInterna(Nfe nfe) {
		return nfe.getEmpresa().getUf().equals(nfe.getEnderecoEntrega().getUf());
	}

	static boolean isNormal(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.NORMAL;
	}

	static boolean isComplementar(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.COMPLEMENTA;
	}

	public static boolean isDevolucao(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.DEVOLUCAO;
	}

	static boolean isAjuste(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.AJUSTE;
	}

	static boolean isSimplesNacional(ItemProduto item) {
		return item.getProduto().getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	static boolean isClienteEstrangeiro(Nfe nfe) {
		return (nfe.getCliente().getEstrangeiro().equals(true) || nfe.getCliente().getExterior().equals(true));
	}

	static boolean isClientePessoaFisica(Nfe nfe) {
		return nfe.getCliente().getTipoPessoa() == TipoPessoa.FISICA;
	}

	static boolean isTransportadorPessoaFisica(Nfe nfe) {
		return nfe.getTransportador().getTipoPessoa() == TipoPessoa.FISICA;
	}

	static boolean isProdutoValido(ItemProduto item) {
		return item.getId() != null;
	}

	static boolean isSimplesNacional(Nfe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	static boolean isSimplesComExcesso(Nfe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES_EXCESSAO;
	}

	static boolean isRegimeNormal(Nfe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.NORMAL;
	}

	static boolean isAvista(Nfe nfe) {
		return nfe.getFormaPagamento() == FormaPagamento.DINHEIRO;
	}

	static boolean isAPrazo(Nfe nfe) {
		return nfe.getFormaPagamento() == FormaPagamento.DINHEIRO;
	}

	static boolean isSaida(Nfe nfe) {
		return nfe.getTipoDocumento() == TipoDocumento.SAIDA;
	}

	static boolean isLogradoutoTrasnportador(EnderecoTransportador endereco) {
		return endereco.getLogradouroTransportador() != null && !endereco.getLogradouroTransportador().equals("");
	}

	static boolean isNumeroTransportador(EnderecoTransportador endereco) {
		return endereco.getNumeroTransportador() != null && !endereco.getNumeroTransportador().equals("");
	}

	static boolean isBairroTrasportador(EnderecoTransportador endereco) {
		return endereco.getBairroTransportador() != null && !endereco.getBairroTransportador().equals("");
	}

	static boolean isCepTransportador(EnderecoTransportador endereco) {
		return endereco.getCepTransportador() != null && !endereco.getCepTransportador().equals("");
	}

	static boolean isComplementoTransportador(EnderecoTransportador endereco) {
		return endereco.getComplementoTransportador() != null && !endereco.getComplementoTransportador().equals("");
	}

	static boolean isTransportador(Nfe nfe) {
		return nfe.getTransportador() != null;
	}

	static boolean isVeiculo(Nfe nfe) {
		return nfe.getVeiculoEntrega() != null && nfe.getVeiculoEntrega().getPlaca() != null;
	}

}
