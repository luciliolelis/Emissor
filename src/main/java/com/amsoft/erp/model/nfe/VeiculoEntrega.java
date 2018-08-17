package com.amsoft.erp.model.nfe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class VeiculoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 8)
	@Column(name = "placa", length = 8)
	private String placa;

	@Size(max = 2)
	@Column(name = "uf_veiculo", length = 2)
	private String uf_veiculo;

	@Size(max = 200)
	@Column(name = "cidade_veiculo", length = 200)
	private String cidade_veiculo;

	@Size(max = 30)
	@Column(name = "marca", length = 30)
	private String marca;

	private String renavam;
	private String rntrc;
	private String ciot;
	private String antt;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getRntrc() {
		return rntrc;
	}

	public void setRntrc(String rntrc) {
		this.rntrc = rntrc;
	}

	public String getCiot() {
		return ciot;
	}

	public void setCiot(String ciot) {
		this.ciot = ciot;
	}

	public String getAntt() {
		return antt;
	}

	public void setAntt(String antt) {
		this.antt = antt;
	}

	public String getUf_veiculo() {
		return uf_veiculo;
	}

	public void setUf_veiculo(String uf_veiculo) {
		this.uf_veiculo = uf_veiculo;
	}

	public String getCidade_veiculo() {
		return cidade_veiculo;
	}

	public void setCidade_veiculo(String cidade_veiculo) {
		this.cidade_veiculo = cidade_veiculo;
	}

	

}
