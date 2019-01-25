package com.amsoft.erp.repository.filter;

import java.io.Serializable;

import com.amsoft.erp.model.cep.CepCidade;

public class CepFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String estado;
	private String cidade;
	private String cep;
	private String logradouro;

	private CepCidade cepCidade;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public CepCidade getCepCidade() {
		return cepCidade;
	}

	public void setCepCidade(CepCidade cepCidade) {
		this.cepCidade = cepCidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	

}