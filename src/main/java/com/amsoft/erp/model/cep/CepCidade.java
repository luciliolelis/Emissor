package com.amsoft.erp.model.cep;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "cep_cidade")
public class CepCidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id_cidade;
	
	
	private String cidade;

	@ManyToOne
	@JoinColumn(name = "uf")
	private CepEstado uf;

	private String cod_ibge;
	private String area;

	public Long getId_cidade() {
		return id_cidade;
	}

	public void setId_cidade(Long id_cidade) {
		this.id_cidade = id_cidade;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public CepEstado getUf() {
		return uf;
	}

	public void setUf(CepEstado uf) {
		this.uf = uf;
	}

	public String getCod_ibge() {
		return cod_ibge;
	}

	public void setCod_ibge(String cod_ibge) {
		this.cod_ibge = cod_ibge;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id_cidade == null) ? 0 : id_cidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CepCidade other = (CepCidade) obj;
		if (id_cidade == null) {
			if (other.id_cidade != null)
				return false;
		} else if (!id_cidade.equals(other.id_cidade))
			return false;
		return true;
	}

}