package com.amsoft.erp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class EnderecoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank @Size(max = 150)
	@Column(name = "logradouro_entrega", nullable = false, length = 150)
	private String logradouroEntrega;
	
	@NotBlank @Size(max = 20)
	@Column(name = "numero_entrega", nullable = false, length = 20)
	private String numeroEntrega;
	
	@Size(max = 150)
	@Column(name = "complemento_entrega", length = 150)
	private String complementoEntrega;
	
	@NotBlank @Size(max = 60)
	@Column(name = "cidade_entrega", nullable = false, length = 60)
	private String cidadeEntrega;
	
	@Column(name = "cidade_ibge_entrega", length = 7)
	private String cidadeIbgeEntrega;
	
	@NotBlank @Size(max = 60)
	@Column(name = "uf_entrega", nullable = false, length = 60)
	private String ufEntrega;
	
	@Column(name = "uf_ibge_entrega", length = 2)
	private String ufIbgeEntrega;
	
	
	@NotBlank @Size(max = 9)
	@Column(name = "cep_entrega", nullable = false, length = 9)
	private String cepEntrega;

	public String getLogradouroEntrega() {
		return logradouroEntrega;
	}

	public void setLogradouroEntrega(String logradouroEntrega) {
		this.logradouroEntrega = logradouroEntrega;
	}

	public String getNumeroEntrega() {
		return numeroEntrega;
	}

	public void setNumeroEntrega(String numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}

	public String getComplementoEntrega() {
		return complementoEntrega;
	}

	public void setComplementoEntrega(String complementoEntrega) {
		this.complementoEntrega = complementoEntrega;
	}

	public String getCidadeEntrega() {
		return cidadeEntrega;
	}

	public void setCidadeEntrega(String cidadeEntrega) {
		this.cidadeEntrega = cidadeEntrega;
	}

	public String getUfEntrega() {
		return ufEntrega;
	}

	public void setUfEntrega(String ufEntrega) {
		this.ufEntrega = ufEntrega;
	}

	public String getCepEntrega() {
		return cepEntrega;
	}

	public void setCepEntrega(String cepEntrega) {
		this.cepEntrega = cepEntrega;
	}

	public String getCidadeIbgeEntrega() {
		return cidadeIbgeEntrega;
	}

	public void setCidadeIbgeEntrega(String cidadeIbgeEntrega) {
		this.cidadeIbgeEntrega = cidadeIbgeEntrega;
	}

	public String getUfIbgeEntrega() {
		return ufIbgeEntrega;
	}

	public void setUfIbgeEntrega(String ufIbgeEntrega) {
		this.ufIbgeEntrega = ufIbgeEntrega;
	}

	

	

}
