package com.amsoft.erp.model;

public enum TipoRelatorio {

	NFE("NF-e"), 
	NFCE("NFC-e");

	private String descricao;

	TipoRelatorio(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
