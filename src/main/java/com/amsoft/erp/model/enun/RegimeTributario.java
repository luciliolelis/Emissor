package com.amsoft.erp.model.enun;

public enum RegimeTributario {

	SIMPLES("Simples nacional"), 
	SIMPLES_EXCESSAO("Simples nacional - Excesso de sublimite de receita bruta"), 
	NORMAL("Regime Normal");
	
	private String descricao;

	RegimeTributario(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}