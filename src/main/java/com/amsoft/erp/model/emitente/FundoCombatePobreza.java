package com.amsoft.erp.model.emitente;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.amsoft.erp.model.cep.CepEstado;

@Entity
@Table(name = "fundo_combate_pobreza")
public class FundoCombatePobreza implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "aliquota_fcp", precision = 10, scale = 2)
	private BigDecimal aliquotaFcp = BigDecimal.ZERO;

	@Column(name = "aliquota_fcp_st", precision = 10, scale = 2)
	private BigDecimal aliquotaFcpSt = BigDecimal.ZERO;

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

	public BigDecimal getAliquotaFcp() {
		return aliquotaFcp;
	}

	public void setAliquotaFcp(BigDecimal aliquotaFcp) {
		this.aliquotaFcp = aliquotaFcp;
	}

	public BigDecimal getAliquotaFcpSt() {
		return aliquotaFcpSt;
	}

	public void setAliquotaFcpSt(BigDecimal aliquotaFcpSt) {
		this.aliquotaFcpSt = aliquotaFcpSt;
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
		FundoCombatePobreza other = (FundoCombatePobreza) obj;
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