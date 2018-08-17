package com.amsoft.erp.model.cep;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cep_estado")
public class CepEstado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Size(max = 2)
	@Column(name = "uf", length = 2)
	private String uf;
	private String estado;
	private String cod_ibge;

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCod_ibge() {
		return cod_ibge;
	}

	public void setCod_ibge(String cod_ibge) {
		this.cod_ibge = cod_ibge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
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
		CepEstado other = (CepEstado) obj;
		if (uf == null) {
			if (other.uf != null)
				return false;
		} else if (!uf.equals(other.uf))
			return false;
		return true;
	}

}