package com.amsoft.erp.model.nfce;

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

import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.produto.Produto;

@Entity
@Table(name = "item_produto_nfce")
public class ItemProdutoNFCe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	@JoinColumn(name = "nfce_id", nullable = false)
	private NFCe nfce;

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

	@Column(name = "mva", precision = 10, scale = 2)
	private BigDecimal mva = BigDecimal.ZERO;

	@Column(name = "pauta", precision = 10, scale = 2)
	private BigDecimal pauta = BigDecimal.ZERO;

	@Column(name = "ncm")
	private String ncm;

	@Column(name = "somar_ipi_bc_icms")
	private Boolean somarIpiBcIcms;

	@Column(name = "valor_transparencia", precision = 10, scale = 2)
	private BigDecimal valorTransparencia = BigDecimal.ZERO;
	
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

	

	public NFCe getNfce() {
		return nfce;
	}

	public void setNfce(NFCe nfce) {
		this.nfce = nfce;
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

	public BigDecimal getAliquotaPis() {
		return aliquotaPis;
	}

	public void setAliquotaPis(BigDecimal aliquotaPis) {
		this.aliquotaPis = aliquotaPis;
	}

	public String getCstCofins() {
		return cstCofins;
	}

	public void setCstCofins(String cstCofins) {
		this.cstCofins = cstCofins;
	}

	public BigDecimal getAliquotaCofins() {
		return aliquotaCofins;
	}

	public void setAliquotaCofins(BigDecimal aliquotaCofins) {
		this.aliquotaCofins = aliquotaCofins;
	}

	public String getCstIcms() {
		return cstIcms;
	}

	public void setCstIcms(String cstIcms) {
		this.cstIcms = cstIcms;
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

	public String getCsosn() {
		return csosn;
	}

	public void setCsosn(String csosn) {
		this.csosn = csosn;
	}

	public BigDecimal getAliquotaIcms() {
		return aliquotaIcms;
	}

	public void setAliquotaIcms(BigDecimal aliquotaIcms) {
		this.aliquotaIcms = aliquotaIcms;
	}

	public BigDecimal getMva() {
		return mva;
	}

	public void setMva(BigDecimal mva) {
		this.mva = mva;
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

	public Boolean getSomarIpiBcIcms() {
		return somarIpiBcIcms;
	}

	public void setSomarIpiBcIcms(Boolean somarIpiBcIcms) {
		this.somarIpiBcIcms = somarIpiBcIcms;
	}

	public BigDecimal getValorTransparencia() {
		return valorTransparencia;
	}

	public void setValorTransparencia(BigDecimal valorTransparencia) {
		this.valorTransparencia = valorTransparencia;
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
		ItemProdutoNFCe other = (ItemProdutoNFCe) obj;
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
	public BigDecimal getValorIcms() {

		try {


			if (this.isIcms()) {





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

			
				base = this.getBaseIcmsSemReducao();
		

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

	private Empresa getEmpresaLogada() {

		if (isEmitente()) {
			return this.getNfce().getEmpresa();
		}

		return null;
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


	private boolean isSimplesNacional() {
		return this.isEmitente() && this.getEmpresaLogada().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	@Transient
	private boolean isEmitente() {
		return this.getNfce().getEmpresa() != null;
	}

	@Transient
	private boolean isEnderecoEntrega() {
		return this.getNfce().getEnderecoEntrega() != null;
	}

	@Transient
	public boolean isProdutoAssociado() {
		return (this.getProduto() != null && this.getProduto().getId() != null) ;
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


	private boolean isSomarFreteBaseCalculo() {
		return this.getNfce().getSomarFreteBaseCalculo().equals(Boolean.TRUE);
	}

	private boolean isSomarSeguroBaseCalculo() {
		return this.getNfce().getSomarSeguroBaseCalculo().equals(Boolean.TRUE);
	}

	private boolean isSomarDespesasBaseCalculo() {
		return this.getNfce().getSomarDespesasBaseCalculo().equals(Boolean.TRUE);
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
	public boolean isValoresIguais() {
		return this.isProduto() && this.getProduto().getValorUnitario().equals(this.getValorUnitario());
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
