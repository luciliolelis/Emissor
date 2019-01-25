package com.amsoft.erp.model.nfce;

public enum FormaPagamentoNFCe {

	DINHEIRO("Dinheiro"), 
	CHEQUE("Cheque"), 
	CARTAO_CREDITO("Cartão de crédito"), 
	CARTAO_DEBITO("Cartão de débito"), 
	CREDITO_LOJA("Crédito Loja"), 
	VALE_ALIMENTACAO("Vale Alimentação"), 
	VALE_REFEICAO("Vale Refeição"), 
	VALE_PRESENTE("Vale Presente"), 
	VALE_COMBUSTIVEL("Vale Combustível"),
	OUTROS("Outros");
	
	private String descricao;
	
	FormaPagamentoNFCe(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}