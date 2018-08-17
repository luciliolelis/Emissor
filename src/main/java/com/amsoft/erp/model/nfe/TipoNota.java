package com.amsoft.erp.model.nfe;

public enum TipoNota {

	NFE("Nf-e"), 
	CTE("Ct-e"), 
	NF("Nota fiscal");
	
	private String descricao;
	
	TipoNota(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}