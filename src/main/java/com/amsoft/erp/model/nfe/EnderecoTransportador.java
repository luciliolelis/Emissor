package com.amsoft.erp.model.nfe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class EnderecoTransportador implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Size(max = 9)
	@Column(name = "cep_transportador", length = 9)
	private String cepTransportador;
	
	@Size(max = 200)
	@Column(name = "cidade_transportador", length = 200)
	private String cidadeTransportador;
	
	@Size(max = 2)
	@Column(name = "uf_transportador", length = 2)
	private String ufTransportador;
	
	@Size(max = 150)
	@Column(name = "logradouro_transportador", length = 150)
	private String logradouroTransportador;
	
	@Size(max = 20)
	@Column(name = "numero_transportador", length = 20)
	private String numeroTransportador;
	
	@Size(max = 150)
	@Column(name = "complemento_transportador", length = 150)
	private String complementoTransportador;
	
	@Size(max = 60)
	@Column(name = "bairro_transportador", length = 60)
	private String bairroTransportador;

	public String getCepTransportador() {
		return cepTransportador;
	}

	public void setCepTransportador(String cepTransportador) {
		this.cepTransportador = cepTransportador;
	}


	public String getCidadeTransportador() {
		return cidadeTransportador;
	}

	public void setCidadeTransportador(String cidadeTransportador) {
		this.cidadeTransportador = cidadeTransportador;
	}

	public String getUfTransportador() {
		return ufTransportador;
	}

	public void setUfTransportador(String ufTransportador) {
		this.ufTransportador = ufTransportador;
	}

	public String getLogradouroTransportador() {
		return logradouroTransportador;
	}

	public void setLogradouroTransportador(String logradouroTransportador) {
		this.logradouroTransportador = logradouroTransportador;
	}

	public String getNumeroTransportador() {
		return numeroTransportador;
	}

	public void setNumeroTransportador(String numeroTransportador) {
		this.numeroTransportador = numeroTransportador;
	}

	public String getComplementoTransportador() {
		return complementoTransportador;
	}

	public void setComplementoTransportador(String complementoTransportador) {
		this.complementoTransportador = complementoTransportador;
	}

	public String getBairroTransportador() {
		return bairroTransportador;
	}

	public void setBairroTransportador(String bairroTransportador) {
		this.bairroTransportador = bairroTransportador;
	}


}
