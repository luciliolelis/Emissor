package com.amsoft.erp.model.nfe;

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

@Entity
@Table(name = "item_complemento")
public class ItemComplemento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne
	@JoinColumn(name = "nfe_id", nullable = false)
	private Nfe nfe;

	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="ncm")
	private String ncm;
	
	@Column(name="cst")
	private String cst;
	
	@Column(name="cfop")
	private Integer cfop;
	
	@Column(name="unidade")
	private String unidade;
	
	@Column(name="quantidade", precision = 10, scale = 2)
	private BigDecimal quantidade;
	
	@Column(name="valor", precision = 10, scale = 2)
	private BigDecimal valor;
	
	@Column(name="desconto", precision = 10, scale = 2)
	private BigDecimal desconto;
	
	@Column(name="total", precision = 10, scale = 2)
	private BigDecimal total;
	
	@Column(name="bc_icms", precision = 10, scale = 2)
	private BigDecimal bcIcms;
	
	@Column(name="bc_icms_st", precision = 10, scale = 2)
	private BigDecimal bcIcmsSt;
	
	@Column(name="bc_ipi", precision = 10, scale = 2)
	private BigDecimal bcIpi;
	
	@Column(name="icms", precision = 10, scale = 2)
	private BigDecimal icms;
	
	@Column(name="icms_st", precision = 10, scale = 2)
	private BigDecimal icmsSt;
	
	@Column(name="aliquita_icms", precision = 10, scale = 2)
	private BigDecimal aliquotaIcms;
	
	@Column(name="aliquita_icms_st", precision = 10, scale = 2)
	private BigDecimal aliquotaIcmsSt;
	
	@Column(name="ipi", precision = 10, scale = 2)
	private BigDecimal ipi;
	
	@Column(name="aliquita_ipi", precision = 10, scale = 2)
	private BigDecimal aliquotaIpi;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Nfe getNfe() {
		return nfe;
	}

	public void setNfe(Nfe nfe) {
		this.nfe = nfe;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public Integer getCfop() {
		return cfop;
	}

	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getBcIcms() {
		return bcIcms;
	}

	public void setBcIcms(BigDecimal bcIcms) {
		this.bcIcms = bcIcms;
	}

	public BigDecimal getBcIcmsSt() {
		return bcIcmsSt;
	}

	public void setBcIcmsSt(BigDecimal bcIcmsSt) {
		this.bcIcmsSt = bcIcmsSt;
	}

	public BigDecimal getIcms() {
		return icms;
	}

	public void setIcms(BigDecimal icms) {
		this.icms = icms;
	}

	public BigDecimal getIcmsSt() {
		return icmsSt;
	}

	public void setIcmsSt(BigDecimal icmsSt) {
		this.icmsSt = icmsSt;
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

	public BigDecimal getIpi() {
		return ipi;
	}

	public void setIpi(BigDecimal ipi) {
		this.ipi = ipi;
	}

	public BigDecimal getAliquotaIpi() {
		return aliquotaIpi;
	}

	public void setAliquotaIpi(BigDecimal aliquotaIpi) {
		this.aliquotaIpi = aliquotaIpi;
	}

	public BigDecimal getBcIpi() {
		return bcIpi;
	}

	public void setBcIpi(BigDecimal bcIpi) {
		this.bcIpi = bcIpi;
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
		ItemComplemento other = (ItemComplemento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
