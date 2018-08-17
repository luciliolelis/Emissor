package com.amsoft.erp.model;

public enum StatusProduto {

	CADASTRO("Cadastro"), 
	EXCUIDO("Excluido");
	
	private String descricao;

	StatusProduto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
