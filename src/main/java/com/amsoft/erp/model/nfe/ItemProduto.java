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
import javax.persistence.Transient;

import com.amsoft.erp.model.produto.ItemIcmsUf;
import com.amsoft.erp.model.produto.Produto;

@Entity
@Table(name = "item_produto")
public class ItemProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2)
	private String uf;

	@Column(nullable = false)
	private BigDecimal quantidade = BigDecimal.ONE;

	@Column(name = "valor_unitario", nullable = false, precision = 10, scale = 4)
	private BigDecimal valorUnitario = BigDecimal.ZERO;

	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 4)
	private BigDecimal valorDesconto = BigDecimal.ZERO;

	@Column(name = "valor_frete", nullable = false, precision = 10, scale = 4)
	private BigDecimal valorFrete = BigDecimal.ZERO;

	@Column(name = "valor_despesa", nullable = false, precision = 10, scale = 4)
	private BigDecimal valorDespesa = BigDecimal.ZERO;

	@Column(name = "valor_seguro", nullable = false, precision = 10, scale = 4)
	private BigDecimal valorSeguro = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "nfe_id", nullable = false)
	private Nfe nfe;

	@Column(nullable = false)
	private Integer cfop = 0;

	@Column(name = "cst_pis")
	private String cstPis;

	@Column(name = "aliquota_pis", precision = 10, scale = 2)
	private BigDecimal aliquotaPis = BigDecimal.ZERO;

	@Column(name = "cst_cofins")
	private String cstCofins;

	@Column(name = "aliquota_cofins", precision = 10, scale = 2)
	private BigDecimal aliquotaCofins = BigDecimal.ZERO;

	@Column(name = "cst_icms")
	private String cstIcms;

	@Column(name = "cst_ipi")
	private String cstIpi;

	@Column(name = "aliquota_ipi", precision = 10, scale = 2)
	private BigDecimal aliquotaIpi = BigDecimal.ZERO;

	@Column(name = "csosn")
	private String csosn;

	@Column(name = "aliquota_icms", precision = 10, scale = 2)
	private BigDecimal aliquotaIcms = BigDecimal.ZERO;

	@Column(name = "aliquota_icms_st", precision = 10, scale = 2)
	private BigDecimal aliquotaIcmsSt = BigDecimal.ZERO;

	@Column(name = "mva", precision = 10, scale = 2)
	private BigDecimal mva = BigDecimal.ZERO;

	@Column(name = "reducao_base_calculo_icms", precision = 10, scale = 2)
	private BigDecimal reducaoBaseCalculoIcms = BigDecimal.ZERO;

	@Column(name = "reducao_base_calculo_icms_st", precision = 10, scale = 2)
	private BigDecimal reducaoBaseCalculoIcmsSt = BigDecimal.ZERO;

	@Column(name = "pauta", precision = 10, scale = 2)
	private BigDecimal pauta = BigDecimal.ZERO;

	@Column(name = "ncm")
	private String ncm;

	@Column(name = "somar_ipi_bc_icms")
	private Boolean somarIpiBcIcms;

	@Column(name = "somar_ipi_bc_icms_st")
	private Boolean somarIpiBcIcmsSt;

	@Column(name = "valor_transparencia", precision = 10, scale = 2)
	private BigDecimal valorTransparencia = BigDecimal.ZERO;

	public BigDecimal getValorTransparencia() {
		return valorTransparencia;
	}

	public void setValorTransparencia(BigDecimal valorTransparencia) {
		this.valorTransparencia = valorTransparencia;
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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public BigDecimal getMva() {
		return mva;
	}

	public void setMva(BigDecimal mva) {
		this.mva = mva;
	}

	public BigDecimal getReducaoBaseCalculoIcms() {
		return reducaoBaseCalculoIcms;
	}

	public void setReducaoBaseCalculoIcms(BigDecimal reducaoBaseCalculoIcms) {
		this.reducaoBaseCalculoIcms = reducaoBaseCalculoIcms;
	}

	public BigDecimal getReducaoBaseCalculoIcmsSt() {
		return reducaoBaseCalculoIcmsSt;
	}

	public void setReducaoBaseCalculoIcmsSt(BigDecimal reducaoBaseCalculoIcmsSt) {
		this.reducaoBaseCalculoIcmsSt = reducaoBaseCalculoIcmsSt;
	}

	public BigDecimal getPauta() {
		return pauta;
	}

	public void setPauta(BigDecimal pauta) {
		this.pauta = pauta;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public BigDecimal getAliquotaIcmsSt() {
		return aliquotaIcmsSt;
	}

	public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
		this.aliquotaIcmsSt = aliquotaIcmsSt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(BigDecimal valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Nfe getNfe() {
		return nfe;
	}

	public void setNfe(Nfe nfe) {
		this.nfe = nfe;
	}

	public Integer getCfop() {
		return cfop;
	}

	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}

	public String getCstPis() {
		return cstPis;
	}

	public void setCstPis(String cstPis) {
		this.cstPis = cstPis;
	}

	public String getCstCofins() {
		return cstCofins;
	}

	public void setCstCofins(String cstCofins) {
		this.cstCofins = cstCofins;
	}

	public String getCstIcms() {
		return cstIcms;
	}

	public void setCstIcms(String cstIcms) {
		this.cstIcms = cstIcms;
	}

	public String getCsosn() {
		return csosn;
	}

	public void setCsosn(String csosn) {
		this.csosn = csosn;
	}

	public BigDecimal getAliquotaPis() {
		return aliquotaPis;
	}

	public void setAliquotaPis(BigDecimal aliquotaPis) {
		this.aliquotaPis = aliquotaPis;
	}

	public BigDecimal getAliquotaCofins() {
		return aliquotaCofins;
	}

	public void setAliquotaCofins(BigDecimal aliquotaCofins) {
		this.aliquotaCofins = aliquotaCofins;
	}

	public BigDecimal getAliquotaIcms() {
		return aliquotaIcms;
	}

	public void setAliquotaIcms(BigDecimal aliquotaIcms) {
		this.aliquotaIcms = aliquotaIcms;
	}

	public String getCstIpi() {
		return cstIpi;
	}

	public void setCstIpi(String cstIpi) {
		this.cstIpi = cstIpi;
	}

	public BigDecimal getAliquotaIpi() {
		return aliquotaIpi;
	}

	public void setAliquotaIpi(BigDecimal aliquotaIpi) {
		this.aliquotaIpi = aliquotaIpi;
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
		ItemProduto other = (ItemProduto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	@Transient
	public BigDecimal getValorTotal() {

		try {
			return this.getValorUnitario().multiply(this.getQuantidade()).subtract(this.getValorDesconto());

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private boolean isInterestadual() {
		return this.isEmitente() && this.isEnderecoEntrega()
				&& !this.getNfe().getEmpresa().getUf().equals(this.getNfe().getEnderecoEntrega().getUf());
	}


	@Transient
	private boolean isEmitente() {
		return this.getNfe().getEmpresa() != null;
	}

	@Transient
	private boolean isEnderecoEntrega() {
		return this.getNfe().getEnderecoEntrega() != null;
	}

	
	@Transient
	private boolean isReducaoIcms() {
		return this.isProduto() && this.getReducaoBaseCalculoIcms() != null
				&& isNotZero(this.getReducaoBaseCalculoIcms());
	}

	boolean isReducaoIcmsSt() {
		return this.isProduto() && this.getReducaoBaseCalculoIcmsSt() != null
				&& isNotZero(this.getReducaoBaseCalculoIcmsSt());
	}

	@Transient
	public boolean isProdutoAssociado() {
		return (this.getProduto() != null && this.getProduto().getId() != null);
	}

	@Transient
	public boolean isCfopProduto() {
		return (this.getCfop() == null && this.getProduto().getId() != null);
	}

	@Transient
	private boolean isIpi() {
		return this.isProduto() && this.getAliquotaIpi() != null && isNotZero(this.getAliquotaIpi());
	}

	@Transient
	private boolean isMva() {
		return this.isProduto() && this.getMva() != null && isNotZero(this.getMva());
	}

	@Transient
	private boolean isIcms() {
		return this.isProduto() && this.getAliquotaIcms() != null && isNotZero(this.getAliquotaIcms());
	}

	@Transient
	private boolean isIcmsSt() {
		return this.isProduto() && this.getAliquotaIcmsSt() != null && isNotZero(this.getAliquotaIcmsSt());
	}

	@Transient
	private boolean isProduto() {
		return this.getProduto() != null;
	}

	@Transient
	private boolean isPis() {
		return this.isProduto() && this.getAliquotaPis() != null && isNotZero(this.getAliquotaPis());
	}

	@Transient
	private boolean isCofins() {
		return this.isProduto() && this.getAliquotaCofins() != null && isNotZero(this.getAliquotaCofins());
	}


	public boolean isConsumidorFinal() {
		return (this.getNfe().isVendaConsumidorFinal() == true || this.isExportacao());
	}

	public boolean isNotConsumidorFinal() {
		return !isConsumidorFinal();
	}

	
	int getIndex() {
		int index = -1;

		if (this.isExportacao())
			return index;

		for (ItemIcmsUf itemIcs : this.produto.getItensIcmsUf()) {
			index++;
			if (this.getNfe().getEnderecoEntrega().getUf().equals(itemIcs.getUf())) {
				return index;
			}
		}
		return index;
	}

	boolean isExportacao() {
		return this.getNfe().getCliente().getEstrangeiro().equals(true)
				|| this.getNfe().getCliente().getExterior().equals(true);
	}

	boolean isNotExportacao() {
		return !this.isExportacao();
	}

	static void error(String error) {
		System.err.println("| ERROR: " + error);
	}

	static void info(String info) {
		System.out.println("| INFO: " + info);
	}

	static boolean isZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) == 0;
	}

	static boolean isMaiorQueZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) > 0;
	}

	static boolean isMenorQueZero(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) < 0;
	}

	static boolean isNotZero(BigDecimal valor) {
		return !isZero(valor);
	}


}
