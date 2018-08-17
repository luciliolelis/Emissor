package com.amsoft.erp.model.produto;

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
@Table(name = "item_icms_uf")
public class ItemIcmsUf implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "uf_id", nullable = false)
	private CepEstado uf;

	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "csosn_id", nullable = true)
	private CSOSN csosn;

	@ManyToOne
	@JoinColumn(name = "csosn_final_id", nullable = true)
	private CSOSN csosnFinal;

	@ManyToOne
	@JoinColumn(name = "cst_icms_id", nullable = true)
	private CSTICMS cstIcms;

	@Column(name = "aliquota_icms", nullable = true, precision = 7, scale = 2)
	private BigDecimal aliquotaIcms = BigDecimal.ZERO;

	@Column(name = "aliquota_icms_st", nullable = true, precision = 7, scale = 2)
	private BigDecimal aliquotaIcmsSt = BigDecimal.ZERO;

	@Column(name = "mva", nullable = true, precision = 7, scale = 2)
	private BigDecimal mva = BigDecimal.ZERO;

	@Column(name = "reducao_icms", nullable = true, precision = 7, scale = 2)
	private BigDecimal reducaoIcms = BigDecimal.ZERO;

	@Column(name = "reducao_icms_st", nullable = true, precision = 7, scale = 2)
	private BigDecimal reducaoIcmsSt = BigDecimal.ZERO;

	@Column(name = "valor_pauta", nullable = true, precision = 12, scale = 2)
	private BigDecimal valorPauta = BigDecimal.ZERO;

	@Column(name = "somar_ipi_bc_icms")
	private Boolean somarIpiBcIcms;

	@Column(name = "somar_ipi_bc_icms_st")
	private Boolean somarIpiBcIcmsSt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public CepEstado getUf() {
		return uf;
	}

	public void setUf(CepEstado uf) {
		this.uf = uf;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public CSOSN getCsosn() {
		return csosn;
	}

	public void setCsosn(CSOSN csosn) {
		this.csosn = csosn;
	}

	public CSOSN getCsosnFinal() {
		return csosnFinal;
	}

	public void setCsosnFinal(CSOSN csosnFinal) {
		this.csosnFinal = csosnFinal;
	}

	public CSTICMS getCstIcms() {
		return cstIcms;
	}

	public void setCstIcms(CSTICMS cstIcms) {
		this.cstIcms = cstIcms;
	}

	public BigDecimal getAliquotaIcms() {
		return aliquotaIcms;
	}

	public void setAliquotaIcms(BigDecimal aliquotaIcms) {
		this.aliquotaIcms = aliquotaIcms;
	}

	public BigDecimal getAliquotaIcmsSt() {
		return aliquotaIcmsSt;
	}

	public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
		this.aliquotaIcmsSt = aliquotaIcmsSt;
	}

	public BigDecimal getMva() {
		return mva;
	}

	public void setMva(BigDecimal mva) {
		this.mva = mva;
	}

	public BigDecimal getReducaoIcms() {
		return reducaoIcms;
	}

	public void setReducaoIcms(BigDecimal reducaoIcms) {
		this.reducaoIcms = reducaoIcms;
	}

	public BigDecimal getReducaoIcmsSt() {
		return reducaoIcmsSt;
	}

	public void setReducaoIcmsSt(BigDecimal reducaoIcmsSt) {
		this.reducaoIcmsSt = reducaoIcmsSt;
	}

	public BigDecimal getValorPauta() {
		return valorPauta;
	}

	public void setValorPauta(BigDecimal valorPauta) {
		this.valorPauta = valorPauta;
	}

	public Boolean getSomarIpiBcIcms() {
		return somarIpiBcIcms;
	}

	public void setSomarIpiBcIcms(Boolean somarIpiBcIcms) {
		this.somarIpiBcIcms = somarIpiBcIcms;
	}

	public Boolean getSomarIpiBcIcmsSt() {
		return somarIpiBcIcmsSt;
	}

	public void setSomarIpiBcIcmsSt(Boolean somarIpiBcIcmsSt) {
		this.somarIpiBcIcmsSt = somarIpiBcIcmsSt;
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
		ItemIcmsUf other = (ItemIcmsUf) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean isUsaMva() {
		return this.getMva() != null && isMaiorQueZero(this.getMva());

	}

	@Transient
	public boolean isUsaPauta() {
		return this.getValorPauta() != null && isMaiorQueZero(this.getValorPauta());
	}

	static boolean isMaiorQueZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) > 0;
	}
	
	
	@Transient
	public boolean isEstadoAssociado() {
		return this.getUf() != null && this.getUf().getUf() != null;
	}
}