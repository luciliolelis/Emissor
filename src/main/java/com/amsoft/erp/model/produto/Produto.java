package com.amsoft.erp.model.produto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.StatusProduto;
import com.amsoft.erp.model.cep.CepEstado;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private StatusProduto status = StatusProduto.CADASTRO;
	private String sku;
	private Empresa empresa;
	private List<ItemIcmsUf> itensIcmsUf = new ArrayList<ItemIcmsUf>();
	private BigDecimal valorUnitario = BigDecimal.ZERO;
	private OrigemProduto origemProduto;
	private CSTIPI cstipi;
	private CSTPISCOFINS cstpiscofins;
	private String codigoBarras;
	private String unidadeMedida;
	private String cest;
	private String ncm;
	private BigDecimal aliquotaIpi = BigDecimal.ZERO;
	private BigDecimal aliquotaPis = BigDecimal.ZERO;
	private BigDecimal aliquotaCofins = BigDecimal.ZERO;
	private Boolean somarIpiBcIcms;
	private Boolean somarIpiBcIcmsSt;
	private CSTICMS csticms;
	private CSOSN csosn;
	private BigDecimal aliquotaIcms = BigDecimal.ZERO;
	private BigDecimal aliquotaIcmsSt = BigDecimal.ZERO;
	private BigDecimal mva = BigDecimal.ZERO;
	private BigDecimal reducaoBaseCalculoIcms = BigDecimal.ZERO;
	private BigDecimal reducaoBaseCalculoIcmsSt = BigDecimal.ZERO;
	private BigDecimal pauta = BigDecimal.ZERO;
	private CSTICMS csticmsExterior;
	private CSOSN csosnExterior;
	private String complementos;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<ItemIcmsUf> getItensIcmsUf() {
		return itensIcmsUf;
	}

	public void setItensIcmsUf(List<ItemIcmsUf> itensIcmsUf) {
		this.itensIcmsUf = itensIcmsUf;
	}

	@NotBlank
	@Size(max = 80)
	@Column(nullable = false, length = 80)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotBlank
	@Column(nullable = false, length = 20, unique = false)
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku == null ? null : sku.toUpperCase();
	}

	@Column(name = "valor_unitario", nullable = true, precision = 12, scale = 2)
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "origem_produto_id", nullable = false)
	public OrigemProduto getOrigemProduto() {
		return origemProduto;
	}

	public void setOrigemProduto(OrigemProduto origemProduto) {
		this.origemProduto = origemProduto;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cst_ipi_id", nullable = false)
	public CSTIPI getCstipi() {
		return cstipi;
	}

	public void setCstipi(CSTIPI cstipi) {
		this.cstipi = cstipi;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cst_pis_cofins_id", nullable = false)
	public CSTPISCOFINS getCstpiscofins() {
		return cstpiscofins;
	}

	public void setCstpiscofins(CSTPISCOFINS cstpiscofins) {
		this.cstpiscofins = cstpiscofins;
	}

	@Column(name = "codigo_barras", length = 14)
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	@NotBlank
	@Column(name = "unidade_medida", nullable = false, length = 4)
	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	@Column(nullable = false, length = 10)
	public String getCest() {
		return cest;
	}

	public void setCest(String cest) {
		this.cest = cest;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	@Column(name = "aliquota_ipi", nullable = true, precision = 7, scale = 2)
	public BigDecimal getAliquotaIpi() {
		return aliquotaIpi;
	}

	public void setAliquotaIpi(BigDecimal aliquotaIpi) {
		this.aliquotaIpi = aliquotaIpi;
	}

	@Column(name = "aliquota_pis", nullable = true, precision = 7, scale = 2)
	public BigDecimal getAliquotaPis() {
		return aliquotaPis;
	}

	public void setAliquotaPis(BigDecimal aliquotaPis) {
		this.aliquotaPis = aliquotaPis;
	}

	@Column(name = "aliquota_cofins", nullable = true, precision = 7, scale = 2)
	public BigDecimal getAliquotaCofins() {
		return aliquotaCofins;
	}

	public void setAliquotaCofins(BigDecimal aliquotaCofins) {
		this.aliquotaCofins = aliquotaCofins;
	}

	@ManyToOne
	@JoinColumn(name = "empresa_id")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Column(name = "somar_ipi_bc_icms")
	public Boolean getSomarIpiBcIcms() {
		return somarIpiBcIcms;
	}

	public void setSomarIpiBcIcms(Boolean somarIpiBcIcms) {
		this.somarIpiBcIcms = somarIpiBcIcms;
	}

	@Column(name = "somar_ipi_bc_icms_st")
	public Boolean getSomarIpiBcIcmsSt() {
		return somarIpiBcIcmsSt;
	}

	public void setSomarIpiBcIcmsSt(Boolean somarIpiBcIcmsSt) {
		this.somarIpiBcIcmsSt = somarIpiBcIcmsSt;
	}

	@ManyToOne
	@JoinColumn(name = "cst_icms_id", nullable = true)
	public CSTICMS getCsticms() {
		return csticms;
	}

	public void setCsticms(CSTICMS csticms) {
		this.csticms = csticms;
	}

	@ManyToOne
	@JoinColumn(name = "csosn_id", nullable = true)
	public CSOSN getCsosn() {
		return csosn;
	}

	public void setCsosn(CSOSN csosn) {
		this.csosn = csosn;
	}

	@Column(name = "aliquota_icms", precision = 10, scale = 2)
	public BigDecimal getAliquotaIcms() {
		return aliquotaIcms;
	}

	public void setAliquotaIcms(BigDecimal aliquotaIcms) {
		this.aliquotaIcms = aliquotaIcms;
	}

	@Column(name = "aliquota_icms_st", precision = 10, scale = 2)
	public BigDecimal getAliquotaIcmsSt() {
		return aliquotaIcmsSt;
	}

	public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
		this.aliquotaIcmsSt = aliquotaIcmsSt;
	}

	@Column(name = "mva", precision = 10, scale = 2)
	public BigDecimal getMva() {
		return mva;
	}

	public void setMva(BigDecimal mva) {
		this.mva = mva;
	}

	@Column(name = "reducao_base_calculo_icms", precision = 10, scale = 2)
	public BigDecimal getReducaoBaseCalculoIcms() {
		return reducaoBaseCalculoIcms;
	}

	public void setReducaoBaseCalculoIcms(BigDecimal reducaoBaseCalculoIcms) {
		this.reducaoBaseCalculoIcms = reducaoBaseCalculoIcms;
	}

	@Column(name = "reducao_base_calculo_icms_st", precision = 10, scale = 2)
	public BigDecimal getReducaoBaseCalculoIcmsSt() {
		return reducaoBaseCalculoIcmsSt;
	}

	public void setReducaoBaseCalculoIcmsSt(BigDecimal reducaoBaseCalculoIcmsSt) {
		this.reducaoBaseCalculoIcmsSt = reducaoBaseCalculoIcmsSt;
	}

	@Column(name = "pauta", precision = 10, scale = 2)
	public BigDecimal getPauta() {
		return pauta;
	}

	public void setPauta(BigDecimal pauta) {
		this.pauta = pauta;
	}

	@Column(name = "complementos")
	public String getComplementos() {
		return complementos;
	}

	public void setComplementos(String complementos) {
		this.complementos = complementos;
	}

	@ManyToOne
	@JoinColumn(name = "cst_icms_exterior_id")
	public CSTICMS getCsticmsExterior() {
		return csticmsExterior;
	}

	public void setCsticmsExterior(CSTICMS csticmsExterior) {
		this.csticmsExterior = csticmsExterior;
	}

	@ManyToOne
	@JoinColumn(name = "csosn_exterior_id")
	public CSOSN getCsosnExterior() {
		return csosnExterior;
	}

	public void setCsosnExterior(CSOSN csosnExterior) {
		this.csosnExterior = csosnExterior;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	public StatusProduto getStatus() {
		return status;
	}

	public void setStatus(StatusProduto status) {
		this.status = status;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void adicionarItemVazio() {
		CepEstado uf = new CepEstado();
		ItemIcmsUf item = new ItemIcmsUf();
		item.setUf(uf);
		item.setProduto(this);
		this.getItensIcmsUf().add(0, item);
	}

	public void removerItemVazio() {
		if (!this.getItensIcmsUf().isEmpty()) {
			ItemIcmsUf primeiroItem = this.getItensIcmsUf().get(0);
			if (primeiroItem != null && primeiroItem.getUf().getUf() == null) {
				this.getItensIcmsUf().remove(0);
			}
		}
	}

	static boolean isMaiorQueZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) > 0;
	}

	@Transient
	public boolean isUsaMva() {
		return this.getMva() != null && isMaiorQueZero(this.getMva());

	}

	@Transient
	public boolean isUsaPauta() {
		return this.getPauta() != null && isMaiorQueZero(this.getPauta());
	}
}