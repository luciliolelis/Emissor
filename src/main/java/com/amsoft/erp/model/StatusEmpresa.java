package com.amsoft.erp.model;

public enum StatusEmpresa {

	CADASTRO("Cadastro"), 
	EXCUIDO("Excluido");
	
	private String descricao;

	StatusEmpresa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
