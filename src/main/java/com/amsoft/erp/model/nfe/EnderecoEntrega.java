package com.amsoft.erp.model.nfe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class EnderecoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(max = 9)
	@Column(name = "entrega_cep", length = 9)
	private String cep;

	@NotBlank
	@Size(max = 200)
	@Column(name = "cidade", length = 200)
	private String cidade;

	@NotBlank
	@Size(max = 9)
	@Column(name = "uf", length = 9)
	private String uf;
	
	@Column(name = "ibge_cidade",length = 10)
	private String ibgeCidade;
	
	@Column(name = "ibge_estado",length = 10)
	private String ibgeEstado;
	
	@NotBlank
	@Size(max = 150)
	@Column(name = "entrega_logradouro", length = 150)
	private String logradouro;

	@NotBlank
	@Size(max = 20)
	@Column(name = "entrega_numero", length = 20)
	private String numero;

	@Size(max = 150)
	@Column(name = "entrega_complemento", length = 150)
	private String complemento;

	@NotBlank
	@Size(max = 60)
	@Column(name = "entrega_bairro", length = 60)
	private String bairro;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbgeCidade() {
		return ibgeCidade;
	}

	public void setIbgeCidade(String ibgeCidade) {
		this.ibgeCidade = ibgeCidade;
	}

	public String getIbgeEstado() {
		return ibgeEstado;
	}

	public void setIbgeEstado(String ibgeEstado) {
		this.ibgeEstado = ibgeEstado;
	}

	
}
