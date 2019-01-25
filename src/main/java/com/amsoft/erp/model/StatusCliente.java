package com.amsoft.erp.model;

public enum StatusCliente {

	CADASTRO("Cadastro"), 
	EXCUIDO("Excluido");
	
	private String descricao;

	StatusCliente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
