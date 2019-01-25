package com.amsoft.erp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ibpt")
public class Ibpt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String codigo;
	private String ex;
	private Integer tipo;
	private String descricao;
	private BigDecimal nacionalfederal = BigDecimal.ZERO;
	private BigDecimal importadosfederal = BigDecimal.ZERO;
	private BigDecimal estadual = BigDecimal.ZERO;
	private BigDecimal municipal = BigDecimal.ZERO;
	private Date vigenciainicio;
	private Date vigenciafim;
	private String chave;
	private String versao;
	private String fonte;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEx() {
		return ex;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getNacionalfederal() {
		return nacionalfederal;
	}

	public void setNacionalfederal(BigDecimal nacionalfederal) {
		this.nacionalfederal = nacionalfederal;
	}

	public BigDecimal getImportadosfederal() {
		return importadosfederal;
	}

	public void setImportadosfederal(BigDecimal importadosfederal) {
		this.importadosfederal = importadosfederal;
	}

	public BigDecimal getEstadual() {
		return estadual;
	}

	public void setEstadual(BigDecimal estadual) {
		this.estadual = estadual;
	}

	public BigDecimal getMunicipal() {
		return municipal;
	}

	public void setMunicipal(BigDecimal municipal) {
		this.municipal = municipal;
	}

	public Date getVigenciainicio() {
		return vigenciainicio;
	}

	public void setVigenciainicio(Date vigenciainicio) {
		this.vigenciainicio = vigenciainicio;
	}

	public Date getVigenciafim() {
		return vigenciafim;
	}

	public void setVigenciafim(Date vigenciafim) {
		this.vigenciafim = vigenciafim;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
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
		Ibpt other = (Ibpt) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}