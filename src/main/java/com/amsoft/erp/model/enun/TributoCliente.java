package com.amsoft.erp.model.enun;

public enum TributoCliente {

	ICMS("Contribuinte ICMS"), 
	RURAL("Produtor Rural"), 
	FIANAL("Consumidor final");
	
	private String descricao;

	TributoCliente(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}