package com.amsoft.erp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.amsoft.erp.model.cep.CepEstado;

@Entity
@Table(name = "inscricao_estadual_st")
public class InscricaoEstadualST implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	@Size(max = 14)
	@Column(name = "numero", length = 14)
	private String numero;
	
	@ManyToOne
	@JoinColumn(name = "uf_id", nullable = false)
	private CepEstado uf;
	
	@ManyToOne
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public CepEstado getUf() {
		return uf;
	}

	public void setUf(CepEstado uf) {
		this.uf = uf;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		InscricaoEstadualST other = (InscricaoEstadualST) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean isEstadoAssociado() {
		return this.getUf() != null && this.getUf().getUf() != null;
	}

}