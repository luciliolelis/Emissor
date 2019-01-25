package com.amsoft.erp.model.nfe;

public enum FinalidadeOperacao {

	NORMAL("1 - Nf-e normal"), 
	COMPLEMENTA("2 - Nf-e complementar"), 
	AJUSTE("3 - Nf-e de ajuste"),
	DEVOLUCAO("4 - Devolução de mercadoria");
	
	private String descricao;
	
	FinalidadeOperacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}