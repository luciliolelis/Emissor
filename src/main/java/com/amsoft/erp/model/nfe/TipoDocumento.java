package com.amsoft.erp.model.nfe;

public enum TipoDocumento {

	ENTRADA("Entrada"), 
	SAIDA("Saída");
	
	private String descricao;

	TipoDocumento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
