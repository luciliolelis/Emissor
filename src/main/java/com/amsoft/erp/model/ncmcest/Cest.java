package com.amsoft.erp.model.ncmcest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cest")
public class Cest {

	Long id;
	String item;
	String cest;
	String ncm;
	String descricao;
	String segmentoId;
	String segmento;
	String uTrib;
	String uTribDescicao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNcm() {
		return ncm;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Size(max = 10)
	@Column(name = "cest", length = 10)
	public String getCest() {
		return cest;
	}

	public void setCest(String cest) {
		this.cest = cest;
	}

	@Column(columnDefinition = "text")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSegmentoId() {
		return segmentoId;
	}

	public void setSegmentoId(String segmentoId) {
		this.segmentoId = segmentoId;
	}


	@Column(columnDefinition = "text")
	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getuTrib() {
		return uTrib;
	}

	public void setuTrib(String uTrib) {
		this.uTrib = uTrib;
	}

	public String getuTribDescicao() {
		return uTribDescicao;
	}

	public void setuTribDescicao(String uTribDescicao) {
		this.uTribDescicao = uTribDescicao;
	}
	
	
	

	public void setNcm(String ncm) {
		this.ncm = ncm;
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
		Cest other = (Cest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
