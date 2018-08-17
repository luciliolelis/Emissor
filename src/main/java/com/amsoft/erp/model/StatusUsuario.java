package com.amsoft.erp.model;

public enum StatusUsuario {

	CADASTRO("Cadastro"), 
	EXCUIDO("Excluido");
	
	private String descricao;

	StatusUsuario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
