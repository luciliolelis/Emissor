package com.amsoft.erp.model.nfe;

public enum FormaPagamento {

	DINHEIRO("Dinheiro"), 
	CHEQUE("Cheque"), 
	CARTAO_CREDITO("Cartão de crédito"), 
	CARTAO_DEBITO("Cartão de débito"), 
	CREDITO_LOJA("Crédito Loja"), 
	VALE_ALIMENTACAO("Vale Alimentação"), 
	VALE_REFEICAO("Vale Refeição"), 
	VALE_PRESENTE("Vale Presente"), 
	VALE_COMBUSTIVEL("Vale Combustível"),
	DUPLICATA_MERCANTIL("Duplicata Mercantil"),
	OUTROS("Outros");
	
	
	
	private String descricao;
	
	FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}