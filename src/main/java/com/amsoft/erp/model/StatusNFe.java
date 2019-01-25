package com.amsoft.erp.model;

public enum StatusNFe {

	CADASTRO("Cadastro"), 
	AUTORIZADA("Autorizada"), 
	AUTORIZADACORRECAO("Autorizada com correção"), 
	CANCELADA("Cancelada"),
	EMPROCESSAMENTO("Em processamento"),
	FALHA("Falha no envio"),
	EXCUIDA("Excluida"),
	INUTILIZADA("Inutilizada");
	
	private String descricao;

	StatusNFe(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
