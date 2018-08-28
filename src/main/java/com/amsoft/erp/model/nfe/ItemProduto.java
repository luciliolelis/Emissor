package com.amsoft.erp.model.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.enun.RegimeTributario;
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

	@Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorUnitario = BigDecimal.ZERO;

	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
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
	public BigDecimal getValorTotalProdutosSemDesconto() {

		try {
			return this.getValorUnitario().multiply(this.getQuantidade());

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
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
	public BigDecimal getValorTotalProdutosBaseIcms() {

		try {

			if (this.isNotIsento()) {
				return this.getValorUnitario().multiply(this.getQuantidade()).subtract(this.getValorDesconto());
			}

			return BigDecimal.ZERO;

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage() + " - " + e.toString());
		}

		return BigDecimal.ZERO;
	}

	private boolean isNotIsento() {
		return !this.isIsento();
	}

	private boolean isIsento() {

		if (this.isSimplesNacional()) {

		} else {
			if (this.getCstIcms().equals("40")) {
				return true;
			} else if (this.getCstIcms().equals("41")) {
				return true;
			} else if (this.getCstIcms().equals("50")) {
				return true;
			} else if (this.getCstIcms().equals("60")) {
				return true;
			}
		}

		return false;
	}

	@Transient
	public BigDecimal getBaseIcms() {

		try {

			if (this.isReducaoBaseCalculoIcms() && this.isReducaoIcms()) {
				return this.getBaseIcmsComReducao().setScale(2);
			}

			return this.getBaseIcmsSemReducao().setScale(2);

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal getBaseIcmsSemReducao() {

		try {

			// if (this.isSimplesNacional()) {
			// return BigDecimal.ZERO;
			// }

			BigDecimal base = this.getValorTotalProdutosBaseIcms();

			if (this.isSomarFreteBaseCalculo() && this.isNotIsento()) {
				base = base.add(this.getValorFrete());
			}

			if (this.isSomarSeguroBaseCalculo() && this.isNotIsento()) {
				base = base.add(this.getValorSeguro());
			}

			if (this.isSomarDespesasBaseCalculo() && this.isNotIsento()) {
				base = base.add(this.getValorDespesa());
			}

			if (this.isSomarIpiBcIcms() && this.isIpi()) {
				base = base.add(this.getValorIpi());
			}

			return base;

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal getBaseIcmsComReducao() {

		try {
			if (this.isIcms()) {

				BigDecimal reducao = this.getReducaoBaseCalculoIcms();

				BigDecimal base = this.getValorTotalProdutosBaseIcms();

				if (this.isSomarFreteBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorFrete());
				}

				if (this.isSomarSeguroBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorSeguro());
				}

				if (this.isSomarDespesasBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorDespesa());
				}

				if (this.isSomarIpiBcIcms() && this.isIpi()) {
					base = base.add(this.getValorIpi());
				}

				BigDecimal r = base.multiply(reducao.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
				base = base.subtract(r);
				return base;
			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;

	}

	@Transient
	private BigDecimal getIcmsComDiferimento() {

		try {
			if (this.isIcms() && this.isReducaoIcms()) {

				BigDecimal reducao = this.getReducaoBaseCalculoIcms();

				BigDecimal icms = getIcms();

				reducao = icms.multiply(reducao).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);

				return icms.subtract(reducao);
			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getValorIcms() {

		try {

			if (this.isIcms()) {

				if (isDiferimentoIcms()) {
					return this.getBaseIcmsComReducao().multiply(this.getAliquotaIcms()).divide(new BigDecimal(100))
							.setScale(2, RoundingMode.HALF_UP);
				}

				if (isReducaoBaseCalculoIcms() && this.isReducaoIcms()) {
					return this.getBaseIcmsComReducao().multiply(this.getAliquotaIcms()).divide(new BigDecimal(100))
							.setScale(2, RoundingMode.HALF_UP);
				}

				return this.getBaseIcmsSemReducao().multiply(this.getAliquotaIcms()).divide(new BigDecimal(100))
						.setScale(2, RoundingMode.HALF_UP);

			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal getIcms() {

		try {

			if (this.isIcms()) {

				BigDecimal base = BigDecimal.ZERO;

				if (isReducaoBaseCalculoIcms() && this.isReducaoIcms()) {
					base = this.getBaseIcmsComReducao();
				} else {
					base = this.getBaseIcmsSemReducao();
				}

				BigDecimal aliquota = this.getAliquotaIcms();

				return base.multiply(aliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getPis() {

		try {
			if (this.isPis()) {

				return this.getValorTotal().multiply(this.getAliquotaPis()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getCofins() {

		try {
			if (this.isCofins()) {
				return this.getValorTotal().multiply(this.getAliquotaCofins()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal getValorIcmsSemDiferimento() {

		try {
			if (this.isIcms()) {
				return this.getBaseIcms().multiply(this.getAliquotaIcms()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal getValorIcmsComDiferimento() {
		BigDecimal diferimento = BigDecimal.ZERO;

		try {
			if (this.isIcms() && this.isReducaoIcms()) {
				diferimento = this.getValorIcmsSemDiferimento().multiply(this.getReducaoBaseCalculoIcms())
						.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);

				return this.getValorIcmsSemDiferimento().subtract(diferimento);
			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return diferimento;
	}

	@Transient
	private boolean isDiferimentoIcms() {
		return this.isCstIcmsItem() && this.getCstIcms().equals("51");
	}

	@SuppressWarnings("unused")
	private BigDecimal getBaseIcmsStComDiferencial(BigDecimal base, BigDecimal dif) {

		BigDecimal baseComDiferencial = BigDecimal.ZERO;

		BigDecimal percentualTributado = new BigDecimal(100).subtract(dif).divide(new BigDecimal(100), 2,
				RoundingMode.HALF_UP);

		baseComDiferencial = base.divide(percentualTributado, 2, RoundingMode.HALF_UP);

		return baseComDiferencial;
	}

	private BigDecimal getBaseIcmsStComReducao(BigDecimal base) {

		BigDecimal reducao = this.getReducaoBaseCalculoIcmsSt();

		reducao = base.multiply(reducao.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);

		base = base.subtract(reducao);

		return base;
	}

	@Transient
	public BigDecimal getBaseIcmsSt() {

		try {

			if (this.isIcmsSt()) {

				BigDecimal base = this.getValorTotalProdutosBaseIcms();

				if (this.isSomarFreteBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorFrete());
				}

				if (this.isSomarSeguroBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorSeguro());
				}

				if (this.isSomarDespesasBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorDespesa());
				}

				if (this.isSomarIpiBcIcmsSt() && this.isIpi()) {
					base = base.add(this.getValorIpi());
				}

				BigDecimal aux = BigDecimal.ZERO;

				if (isNotZero(this.getMva())) {
					aux = base.multiply(this.getMva()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
				} else if (isNotZero(this.getPauta())) {
					return this.getQuantidade().multiply(this.getPauta());
				} else {

					aux = base.multiply(this.getAliquotaIcms()).divide(new BigDecimal(100)).setScale(2,
							RoundingMode.HALF_UP);

					aux = base.subtract(aux);

					BigDecimal fator = BigDecimal.ONE.subtract(this.aliquotaIcmsSt.divide(new BigDecimal(100)));

					base = aux.divide(fator, 2, RoundingMode.HALF_UP);

					return base.setScale(2);
				}

				base = base.add(aux);

				if (this.isReducaoBaseCalculoIcmsSt() && this.isReducaoIcms()) {
					return this.getBaseIcmsStComReducao(base).setScale(2);
				}

				return base.setScale(2);

			}

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	private BigDecimal diferencalAliquotaIcms() {

		BigDecimal ret = BigDecimal.ZERO;

		if (isDiferencalAliquotaIcms() == 1 || !isInterestadual() || isNotConsumidorFinal()) {
			return BigDecimal.ZERO;
		}

		ret = this.getAliquotaIcmsSt().subtract(this.getAliquotaIcms());

		if (isMenorQueZero(ret)) {
			return BigDecimal.ZERO;
		}

		return ret;
	}

	@Transient
	private int isDiferencalAliquotaIcms() {
		return this.getAliquotaIcms().compareTo(this.getAliquotaIcmsSt());
	}

	private Empresa getEmpresaLogada() {

		if (isEmitente()) {
			return this.getNfe().getEmpresa();
		}

		return null;
	}

	@Transient
	public BigDecimal getValorIcmsSt() {

		try {

			if (this.isIcms() && this.isIcmsSt()) {

				BigDecimal baseSt = this.getBaseIcmsSt();
				BigDecimal icms = this.getValorIcms();
				BigDecimal aux = BigDecimal.ZERO;

				if (isNotZero(baseSt)) {

					BigDecimal dif = diferencalAliquotaIcms();

					if (isNotZero(dif) && this.isInterestadual()) {
						return baseSt.multiply(dif.divide(new BigDecimal(100)));
					}

					aux = baseSt.multiply(this.getAliquotaIcmsSt()).divide(new BigDecimal(100)).setScale(2,
							RoundingMode.HALF_UP);

					aux = aux.subtract(icms);

					return aux.setScale(2);
				}
			}

		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getValorIpi() {

		try {
			if (this.isIpi()) {

				BigDecimal base = this.getValorTotal().add(this.getValorDesconto());

				if (this.isSomarFreteBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorFrete());
				}

				if (this.isSomarSeguroBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorSeguro());
				}

				if (this.isSomarDespesasBaseCalculo() && this.isNotIsento()) {
					base = base.add(this.getValorDespesa());
				}

				BigDecimal aliquota = this.getAliquotaIpi();

				return base.multiply(aliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			}
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

	private boolean isSimplesNacional() {
		return this.isEmitente() && this.getEmpresaLogada().getRegimeTributario() == RegimeTributario.SIMPLES;
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
	private boolean isReducaoBaseCalculoIcms() {

		if (this.isSimplesNacional()) {
			return this.isCsosnItem() && this.isReducaoIcms() && this.getCsosn().equals("201")
					|| this.getCsosn().equals("202") || this.getCsosn().equals("900");
		} else {
			return this.isCstIcmsItem() && this.isReducaoIcms() && this.getCstIcms().equals("10")
					|| this.getCstIcms().equals("20") || this.getCstIcms().equals("70")
					|| this.getCstIcms().equals("90");
		}

	}

	boolean isReducaoBaseCalculoIcmsSt() {
		if (this.isSimplesNacional()) {
			return this.isCsosnItem() && this.isReducaoIcmsSt() && this.getCsosn().equals("201")
					|| this.getCsosn().equals("202") || this.getCsosn().equals("900");
		} else {
			return this.isCstIcmsItem() && this.isReducaoIcmsSt() && this.getCstIcms().equals("10")
					|| this.getCstIcms().equals("70") || this.getCstIcms().equals("90");
		}
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

	@Transient
	private boolean isSomarIpiBcIcms() {
		return this.getSomarIpiBcIcms() != null && this.getSomarIpiBcIcms() == true;
	}

	@Transient
	private boolean isSomarIpiBcIcmsSt() {
		return this.getSomarIpiBcIcmsSt() != null && this.getSomarIpiBcIcmsSt() == true;
	}

	private boolean isSomarFreteBaseCalculo() {
		return this.getNfe().getSomarFreteBaseCalculo().equals(Boolean.TRUE);
	}

	private boolean isSomarSeguroBaseCalculo() {
		return this.getNfe().getSomarSeguroBaseCalculo().equals(Boolean.TRUE);
	}

	private boolean isSomarDespesasBaseCalculo() {
		return this.getNfe().getSomarDespesasBaseCalculo().equals(Boolean.TRUE);
	}

	@Transient
	public boolean isCstIpiIguais() {

		if (this.isProduto()) {

			if (!this.isCstIpiItem() && !this.isCstIpiProduto()) {
				return true;
			}

			if (!this.isCstIpiItem() || !this.isCstIpiProduto()) {
				return false;
			}

			if (this.isCstIpiItem() && this.isCstIpiProduto()) {
				return this.getProduto().getCstipi().getCodigo().equals(this.getCstIpi());
			}
		}

		return true;
	}

	public boolean isConsumidorFinal() {
		return (this.getNfe().isVendaConsumidorFinal() == true || this.isExportacao());
	}

	public boolean isNotConsumidorFinal() {
		return !isConsumidorFinal();
	}

	@Transient
	public boolean isAlteracoes() {

		if (this.isSimplesNacional()) {
			if (!this.isCsosnIguais() && this.isNotConsumidorFinal()) {
				return true;
			}

			if (!this.isCsosnConsumidorFinalIguais() && this.isConsumidorFinal()) {
				return true;
			}

		} else {
			if (!this.isCstIcmsIguais() && this.isNotConsumidorFinal()) {
				return true;
			}

			if (!this.isCstIcmsConsumidorFinalIguais() && this.isConsumidorFinal()) {
				return true;
			}
		}

		if (!this.isAliquotasIcmsIguais()) {
			return true;
		} else if (!this.isAliquotasIcmsStIguais()) {
			return true;
		} else if (!this.isCstPisIguais()) {
			return true;
		} else if (!this.isAliquotasPisIguais()) {
			return true;
		} else if (!this.isAliquotasCofinsIguais()) {
			return true;
		} else if (!this.isCstIpiIguais()) {
			return true;
		} else if (!this.isAliquotasIpiIguais()) {
			return true;
		} else if (!this.isValoresIguais()) {
			return true;
		} else if (!this.isMvaIguais()) {
			return true;
		} else if (!this.isNcmIguais()) {
			return true;
		} else if (!this.isReducoesIguais()) {
			return true;
		} else if (!this.isReducoesStIguais()) {
			return true;
		} else if (!this.isPautasIguais()) {
			return true;
		}

		return false;
	}

	private boolean isCstIpiItem() {
		return this.getCstIpi() != null;
	}

	private boolean isCstIpiProduto() {
		return this.getProduto().getCstipi() != null;
	}

	@Transient
	public boolean isAliquotasIpiIguais() {

		if (this.isProduto()) {
			if (!this.isAliquotaIpiProduto() && !this.isAliquotaIpiItem()) {
				return true;
			}

			if (!this.isAliquotaIpiProduto() || !this.isAliquotaIpiItem()) {
				return false;
			}

			if (this.isAliquotaIpiProduto() && this.isAliquotaIpiItem()) {
				return this.getProduto().getAliquotaIpi().equals(this.getAliquotaIpi());
			}

		}

		return true;
	}

	private boolean isAliquotaIpiProduto() {
		return this.getProduto().getAliquotaIpi() != null;
	}

	private boolean isAliquotaIpiItem() {
		return this.getAliquotaIpi() != null;
	}

	@Transient
	public boolean isCstPisIguais() {

		if (this.isProduto()) {

			if (!this.isCstPisProduto() && !this.isCstPisItem()) {
				return true;
			}

			if (!this.isCstPisProduto() || !this.isCstPisItem()) {
				return false;
			}

			if (this.isCstPisProduto() && this.isCstPisItem()) {
				return this.getProduto().getCstpiscofins().getCodigo().equals(this.getCstPis());
			}

		}

		return true;
	}

	private boolean isCstPisProduto() {
		return this.getProduto().getCstpiscofins() != null;
	}

	private boolean isCstPisItem() {
		return this.getCstPis() != null;
	}

	@Transient
	public boolean isAliquotasPisIguais() {

		if (this.isProduto()) {

			if (!this.isAliquotaPisProduto() && !this.isAliquotaPisItem()) {
				return true;
			}

			if (!this.isAliquotaPisProduto() || !this.isAliquotaPisItem()) {
				return false;
			}

			if (this.isAliquotaPisProduto() && this.isAliquotaPisItem()) {
				return this.getProduto().getAliquotaPis().equals(this.getAliquotaPis());
			}
		}

		return true;
	}

	private boolean isAliquotaPisProduto() {
		return this.getProduto().getAliquotaPis() != null;
	}

	private boolean isAliquotaPisItem() {
		return this.getAliquotaPis() != null;
	}

	@Transient
	public boolean isAliquotasCofinsIguais() {

		if (this.isProduto()) {

			if (!this.isAliquotaCofinsProduto() && !this.isAliquotaCofinsItem()) {
				return true;
			}

			if (!this.isAliquotaCofinsProduto() || !this.isAliquotaCofinsItem()) {
				return false;
			}

			if (this.isAliquotaCofinsProduto() && this.isAliquotaCofinsItem()) {
				return this.getProduto().getAliquotaCofins().equals(this.getAliquotaCofins());
			}
		}

		return true;
	}

	private boolean isAliquotaCofinsProduto() {
		return this.getProduto().getAliquotaCofins() != null;
	}

	private boolean isAliquotaCofinsItem() {
		return this.getAliquotaCofins() != null;
	}

	@Transient
	public boolean isCstIcmsIguais() {

		if (this.isProduto()) {

			if (!this.isCstIcmsProduto() && !this.isCstIcmsItem()) {
				return true;
			}

			if (!this.isCstIcmsProduto() || !this.isCstIcmsItem()) {
				return false;
			}

			if (this.isCstIcmsProduto() && this.isCstIcmsItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getCsticms().getCodigo().equals(this.getCstIcms());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getCstIcms().getCodigo().equals(this.getCstIcms());
			}
		}

		return true;
	}

	private boolean isCstIcmsItem() {
		return this.getCstIcms() != null;
	}

	private boolean isCstIcmsProduto() {

		int index = this.getIndex();

		if (index == -1)
			return false;

		if (this.isConsumidorFinal())
			return this.getProduto().getCsticms().getCodigo() != null;
		else
			return this.getProduto().getItensIcmsUf().get(index).getCstIcms() != null;

	}

	@Transient
	public boolean isCsosnIguais() {

		if (this.isProduto()) {
			if (!this.isCsosnProduto() && !this.isCsosnItem()) {
				return true;
			}

			if (!this.isCsosnProduto() || !this.isCsosnItem()) {
				return false;
			}

			if (this.isCsosnProduto() && this.isCsosnItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getCsosn().getCodigo().equals(this.getCsosn());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getCsosn().getCodigo().equals(this.getCsosn());
			}
		}

		return true;
	}

	private boolean isCsosnItem() {
		return this.getCsosn() != null;
	}

	private boolean isCsosnProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getCsosn() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getCsosn() != null;
	}

	@Transient
	public boolean isNcmIguais() {

		if (this.isProduto()) {

			if (!this.isNcmProduto() && !this.isNcmItem()) {
				return true;
			}

			if (!this.isNcmProduto() || !this.isNcmItem()) {
				return false;
			}

			if (this.isNcmProduto() && this.isNcmItem()) {
				return this.getProduto().getNcm().equals(this.getNcm());
			}
		}

		return true;
	}

	private boolean isNcmItem() {
		return this.getNcm() != null;
	}

	private boolean isNcmProduto() {
		return this.getProduto().getNcm() != null;
	}

	@Transient
	public boolean isMvaIguais() {

		if (this.isProduto()) {
			if (!this.isMvaProduto() && !this.isMvaItem()) {
				return true;
			}

			if (!this.isMvaProduto() || !this.isMvaItem()) {
				return false;
			}

			if (this.isMvaProduto() && this.isMvaItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getMva().equals(this.getMva());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getMva().equals(this.getMva());
			}
		}

		return true;
	}

	private boolean isMvaItem() {
		return this.getMva() != null;
	}

	private boolean isMvaProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getMva() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getMva() != null;
	}

	@Transient
	public boolean isReducoesIguais() {

		if (this.isProduto()) {

			if (!this.isReducaoProduto() && !this.isReducaoItem())
				return true;

			if (!this.isReducaoProduto() || !this.isReducaoItem())
				return false;

			if (this.isReducaoProduto() && this.isReducaoItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getReducaoBaseCalculoIcms().equals(this.getReducaoBaseCalculoIcms());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getReducaoIcms()
						.equals(this.getReducaoBaseCalculoIcms());
			}
		}

		return true;
	}

	private boolean isReducaoItem() {
		return this.getReducaoBaseCalculoIcms() != null;
	}

	private boolean isReducaoProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getReducaoBaseCalculoIcms() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getReducaoIcms() != null;
	}

	@Transient
	public boolean isReducoesStIguais() {

		if (this.isProduto()) {

			if (!this.isReducaoStProduto() && !this.isReducaoStItem()) {
				return true;
			}

			if (!this.isReducaoStProduto() || !this.isReducaoStItem()) {
				return false;
			}

			if (this.isReducaoStProduto() && this.isReducaoStItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getReducaoBaseCalculoIcmsSt().equals(this.getReducaoBaseCalculoIcmsSt());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getReducaoIcmsSt()
						.equals(this.getReducaoBaseCalculoIcmsSt());
			}
		}

		return true;
	}

	private boolean isReducaoStItem() {
		return this.getReducaoBaseCalculoIcmsSt() != null;
	}

	private boolean isReducaoStProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getReducaoBaseCalculoIcmsSt() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getReducaoIcmsSt() != null;
	}

	@Transient
	public boolean isPautasIguais() {

		if (this.isProduto()) {

			if (!this.isPautaProduto() && !this.isPautaItem()) {
				return true;
			}

			if (!this.isPautaProduto() || !this.isPautaItem()) {
				return false;
			}

			if (this.isPautaProduto() && this.isPautaItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getPauta().equals(this.getPauta());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getValorPauta().equals(this.getPauta());
			}
		}

		return true;
	}

	private boolean isPautaItem() {
		return this.getPauta() != null;
	}

	private boolean isPautaProduto() {
		return this.getProduto().getValorUnitario() != null;
	}

	@Transient
	public boolean isValoresIguais() {
		return this.isProduto() && this.getProduto().getValorUnitario().equals(this.getValorUnitario());
	}

	@Transient
	public boolean isAliquotasIcmsIguais() {

		if (this.isProduto()) {

			if (!this.isAliquotaIcmsProduto() && !this.isAliquotaIcmsItem()) {
				return true;
			}

			if (!this.isAliquotaIcmsProduto() || !this.isAliquotaIcmsItem()) {
				return false;
			}

			if (this.isAliquotaIcmsProduto() && this.isAliquotaIcmsItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getAliquotaIcms().equals(this.getAliquotaIcms());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getAliquotaIcms().equals(this.getAliquotaIcms());
			}
		}

		return true;
	}

	private boolean isAliquotaIcmsProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getAliquotaIcms() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getAliquotaIcms() != null;
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

	private boolean isAliquotaIcmsItem() {
		return this.getAliquotaIcms() != null;
	}

	@Transient
	public boolean isAliquotasIcmsStIguais() {

		if (this.isProduto()) {

			if (!this.isAliquotaIcmsStProduto() && !this.isAliquotaIcmsStItem()) {
				return true;
			}

			if (!this.isAliquotaIcmsStProduto() || !this.isAliquotaIcmsStItem()) {
				return false;
			}

			if (this.isAliquotaIcmsStProduto() && this.isAliquotaIcmsStItem()) {

				if (this.isConsumidorFinal())
					return this.getProduto().getAliquotaIcmsSt().equals(this.getAliquotaIcmsSt());

				int index = this.getIndex();

				if (index == -1)
					return false;

				return this.getProduto().getItensIcmsUf().get(index).getAliquotaIcmsSt()
						.equals(this.getAliquotaIcmsSt());
			}
		}

		return true;
	}

	private boolean isAliquotaIcmsStProduto() {

		if (this.isConsumidorFinal())
			return this.getProduto().getAliquotaIcmsSt() != null;

		int index = this.getIndex();

		if (index == -1)
			return false;

		return this.getProduto().getItensIcmsUf().get(index).getAliquotaIcmsSt() != null;
	}

	private boolean isAliquotaIcmsStItem() {
		return this.getAliquotaIcmsSt() != null;
	}

	@Transient
	public boolean isCstIcmsConsumidorFinalIguais() {

		if (this.isProduto()) {

			if (!this.isCstIcmsConsumidorFinalProduto() && !this.isCstIcmsConsumidorFinalItem()) {
				return true;
			}

			if (!this.isCstIcmsConsumidorFinalProduto() || !this.isCstIcmsConsumidorFinalItem()) {
				return false;
			}

			if (this.isCstIcmsConsumidorFinalProduto() && this.isCstIcmsConsumidorFinalItem()) {
				return this.getProduto().getCsticms().getCodigo().equals(this.getCstIcms());
			}
		}

		return true;
	}

	private boolean isCstIcmsConsumidorFinalProduto() {
		return this.getProduto().getCsticms() != null;
	}

	private boolean isCstIcmsConsumidorFinalItem() {
		return this.getCstIcms() != null;
	}

	@Transient
	public boolean isCsosnConsumidorFinalIguais() {

		if (this.isProduto()) {
			if (!this.isCsosnConsumidorFinalProduto() && !this.isCsosnConsumidorFinalItem()) {
				return true;
			}

			if (!this.isCsosnConsumidorFinalProduto() || !this.isCsosnConsumidorFinalItem()) {
				return false;
			}

			if (this.isCsosnConsumidorFinalProduto() && this.isCsosnConsumidorFinalItem()) {
				return this.getProduto().getCsosn().equals(this.getCsosn());
			}
		}

		return true;
	}

	private boolean isCsosnConsumidorFinalProduto() {
		return this.getProduto().getCsticms() != null;
	}

	private boolean isCsosnConsumidorFinalItem() {
		return this.getCstIcms() != null;
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

	@Transient
	public String getAlertas() {

		String ret = "";

		if (this.isSimplesNacional()) {
			if (this.isCsosnIguais() == false && this.isNotConsumidorFinal()) {

				if (ret.equals("")) {
					ret = "CSOSN";
				} else {
					ret = ret + ", CSOSN";
				}
			}

			if (this.isCsosnConsumidorFinalIguais() == false && this.isConsumidorFinal()) {
				if (ret.equals("")) {
					ret = "CSOSN consumidor final";
				} else {
					ret = ret + ", CSOSN consumidor final";
				}
			}

		} else {

			if (this.isCstIcmsIguais() == false && this.isNotConsumidorFinal()) {
				if (ret.equals("")) {
					ret = "CST ICMS";
				} else {
					ret = ret + ", CST ICMS";
				}
			}

			if (this.isCstIcmsConsumidorFinalIguais() == false && this.isConsumidorFinal()) {
				if (ret.equals("")) {
					ret = "CST ICMS consumidor final";
				} else {
					ret = ret + ", CST ICMS consumidor final";
				}
			}
		}

		if (this.isAliquotasIcmsIguais() == false) {
			if (ret.equals("")) {
				ret = "Alíquota ICMS";
			} else {
				ret = ret + ", Alíquota ICMS";
			}
		}

		if (this.isAliquotasIcmsStIguais() == false) {
			if (ret.equals("")) {
				ret = "Alíquota ICMS ST";
			} else {
				ret = ret + ", Alíquota ICMS ST";
			}
		}

		if (this.isCstPisIguais() == false) {
			if (ret.equals("")) {
				ret = "CST PIS COFINS";
			} else {
				ret = ret + ", CST PIS COFINS";
			}
		}

		if (this.isAliquotasPisIguais() == false) {
			if (ret.equals("")) {
				ret = "Alíquota PIS";
			} else {
				ret = ret + ", Alíquota PIS";
			}
		}

		if (this.isAliquotasCofinsIguais() == false) {
			if (ret.equals("")) {
				ret = "Alíquota COFINS";
			} else {
				ret = ret + ", Alíquota COFINS";
			}
		}

		if (this.isCstIpiIguais() == false) {
			if (ret.equals("")) {
				ret = "CST IPI";
			} else {
				ret = ret + ", CST IPI";
			}
		}

		if (this.isAliquotasIpiIguais() == false) {
			if (ret.equals("")) {
				ret = "Alíquota PIS";
			} else {
				ret = ret + ", Alíquota PIS";
			}
		}

		if (this.isValoresIguais() == false) {
			if (ret.equals("")) {
				ret = "Valor do produto";
			} else {
				ret = ret + ", Valor do produto";
			}
		}

		if (this.isNcmIguais() == false) {
			if (ret.equals("")) {
				ret = "NCM";
			} else {
				ret = ret + ", NCM";
			}
		}

		if (this.isMvaIguais() == false) {
			if (ret.equals("")) {
				ret = "MVA";
			} else {
				ret = ret + ", MVA";
			}
		}

		if (this.isReducoesIguais() == false) {
			if (ret.equals("")) {
				ret = "Redução BC ICMC";
			} else {
				ret = ret + ", Redução BC ICMC";
			}
		}

		if (this.isReducoesStIguais() == false) {
			if (ret.equals("")) {
				ret = "Redução BC ICMC ST";
			} else {
				ret = ret + ", Redução BC ICMC ST";
			}
		}

		if (this.isPautasIguais() == false) {
			if (ret.equals("")) {
				ret = "Valor de pauta";
			} else {
				ret = ret + ", Valor de pauta";
			}
		}

		return ret;
	}

}
