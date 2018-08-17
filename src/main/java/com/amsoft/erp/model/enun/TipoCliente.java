package com.amsoft.erp.model.enun;

public enum TipoCliente {

	CLIENTE("Cliente"), 
	FORNECEDOR("Fornecedor"), 
	TRANSPORTADOR("Transportador");
	
	private String descricao;

	TipoCliente(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
