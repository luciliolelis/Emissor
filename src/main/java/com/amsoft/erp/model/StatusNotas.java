package com.amsoft.erp.model;

public enum StatusNotas {

	AUTORIZADA("Autorizada"), 
	CANCELADA("Cancelada"),
	INUTILIZADA("Inutilizada");
	
	private String descricao;

	StatusNotas(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
