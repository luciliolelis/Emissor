package com.amsoft.erp.model.enun;

public enum Smtp {

	GMAIL("smtp.gmail.com"),
	GOOGLE("smtp.googlemail.com"),
	YAHOO("smtp.mail.yahoo.com.br"),
	IBEST("smtp.ibest.com.br"),
	BOL("smtp.bol.com.br");
	 
	private String descricao;

	Smtp(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}