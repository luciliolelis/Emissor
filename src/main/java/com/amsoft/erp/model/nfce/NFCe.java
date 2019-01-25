package com.amsoft.erp.model.nfce;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.nfe.Cfop;
import com.amsoft.erp.model.nfe.EnderecoEntrega;
import com.amsoft.erp.model.produto.Produto;

@Entity
@Table(name = "nfce")
public class NFCe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusNFe status = StatusNFe.CADASTRO;

	@Column(length = 50)
	private String chave;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Column(name = "protocolo")
	private String protocolo;

	@ManyToOne
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;

	@Column(length = 2)
	private String modelo = "65";

	@NotNull
	@Column(name = "numero_nfe", nullable = false)
	private Integer numero;

	@Column(name = "emissao")
	private Date emissao = new Date();

	@Column(name = "saida")
	private Date dataSaida = new Date();

	private EnderecoEntrega enderecoEntrega;

	@Column(name = "somar_despesas_base_calculo")
	private Boolean somarDespesasBaseCalculo = Boolean.TRUE;

	@Column(name = "somar_seguro_base_calculo")
	private Boolean somarSeguroBaseCalculo = Boolean.TRUE;

	@Column(name = "somar_frete_base_calculo")
	private Boolean somarFreteBaseCalculo = Boolean.TRUE;

	@ManyToOne
	@JoinColumn(name = "cfop_id", nullable = true)
	private Cfop cfop;

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "nfce", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemProdutoNFCe> itensProdutos = new ArrayList<ItemProdutoNFCe>();

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "nfce", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemDuplicataNFCe> itensDuplicatas = new ArrayList<ItemDuplicataNFCe>();

	@NotNull
	@Column(name = "valor_frete", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorFrete = BigDecimal.ZERO;

	@NotNull
	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorDesconto = BigDecimal.ZERO;

	@Column(name = "valor_seguro", precision = 10, scale = 2)
	private BigDecimal valorSeguro = BigDecimal.ZERO;

	@Column(name = "valor_total_produtos", precision = 10, scale = 2)
	private BigDecimal valorTotalProdutos = BigDecimal.ZERO;

	@Column(name = "valor_despesas", precision = 10, scale = 2)
	private BigDecimal valorDespesas = BigDecimal.ZERO;

	@Column(name = "valor_base_icms", precision = 10, scale = 2)
	private BigDecimal valorBaseIcms = BigDecimal.ZERO;

	@Column(name = "valor_icms", precision = 10, scale = 2)
	private BigDecimal valorIcms = BigDecimal.ZERO;

	@Column(name = "xml")
	private String xml;

	@Column(name = "retConsReciNFe")
	private String retConsReciNFe;

	@Column(name = "retConsSitNFe")
	private String retConsSitNFe;

	@Column(name = "retEnviNFe")
	private String retEnviNFe;

	@Column(name = "enviNFe")
	private String enviNFe;

	@Column(name = "retEnvEventoCancNFe")
	private String retEnvEventoCancNFe;

	@Column(name = "intervalo_parcelas")
	private Integer intervaloParcelas = 30;

	@Column(name = "quantidade_parcelas")
	private Integer quantidadeParcelas = 0;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = true, length = 20)
	private FormaPagamentoNFCe formaPagamento = FormaPagamentoNFCe.DINHEIRO;

	@Enumerated(EnumType.STRING)
	@Column(name = "pagamento", nullable = true, length = 20)
	private FormaPagamentoNFCe pagamento = FormaPagamentoNFCe.DINHEIRO;

	@NotNull
	@Column(name = "valor_total", nullable = true, precision = 10, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Column(name = "valor_transparencia", precision = 10, scale = 2)
	private BigDecimal valorTransparencia = BigDecimal.ZERO;

	@Column(name = "calcular_conforme_ibpt")
	private Boolean calcularConformeIBPT = Boolean.TRUE;

	@Column(name = "dados_complementares")
	private String dadosComplementares;

	@Column(name = "fisco")
	private String fisco;

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Integer getIntervaloParcelas() {
		return intervaloParcelas;
	}

	public void setIntervaloParcelas(Integer intervaloParcelas) {
		this.intervaloParcelas = intervaloParcelas;
	}

	public String getDadosComplementares() {
		return dadosComplementares;
	}

	public void setDadosComplementares(String dadosComplementares) {
		this.dadosComplementares = dadosComplementares;
	}

	public String getFisco() {
		return fisco;
	}

	public void setFisco(String fisco) {
		this.fisco = fisco;
	}

	public Boolean getCalcularConformeIBPT() {
		return calcularConformeIBPT;
	}

	public void setCalcularConformeIBPT(Boolean calcularConformeIBPT) {
		this.calcularConformeIBPT = calcularConformeIBPT;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public BigDecimal getValorIcms() {
		return valorIcms;
	}

	public void setValorIcms(BigDecimal valorIcms) {
		this.valorIcms = valorIcms;
	}

	public BigDecimal getValorDespesas() {
		return valorDespesas;
	}

	public void setValorDespesas(BigDecimal valorDespesas) {
		this.valorDespesas = valorDespesas;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorBaseIcms() {
		return valorBaseIcms;
	}

	public void setValorBaseIcms(BigDecimal valorBaseIcms) {
		this.valorBaseIcms = valorBaseIcms;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusNFe getStatus() {
		return status;
	}

	public void setStatus(StatusNFe status) {
		this.status = status;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	@Embedded
	public EnderecoEntrega getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public Cfop getCfop() {
		return cfop;
	}

	public void setCfop(Cfop cfop) {
		this.cfop = cfop;
	}

	public Boolean getSomarDespesasBaseCalculo() {
		return somarDespesasBaseCalculo;
	}

	public void setSomarDespesasBaseCalculo(Boolean somarDespesasBaseCalculo) {
		this.somarDespesasBaseCalculo = somarDespesasBaseCalculo;
	}

	public Boolean getSomarSeguroBaseCalculo() {
		return somarSeguroBaseCalculo;
	}

	public void setSomarSeguroBaseCalculo(Boolean somarSeguroBaseCalculo) {
		this.somarSeguroBaseCalculo = somarSeguroBaseCalculo;
	}

	public Boolean getSomarFreteBaseCalculo() {
		return somarFreteBaseCalculo;
	}

	public void setSomarFreteBaseCalculo(Boolean somarFreteBaseCalculo) {
		this.somarFreteBaseCalculo = somarFreteBaseCalculo;
	}

	public List<ItemProdutoNFCe> getItensProdutos() {
		return itensProdutos;
	}

	public void setItensProdutos(List<ItemProdutoNFCe> itensProdutos) {
		this.itensProdutos = itensProdutos;
	}

	public List<ItemDuplicataNFCe> getItensDuplicatas() {
		return itensDuplicatas;
	}

	public void setItensDuplicatas(List<ItemDuplicataNFCe> itensDuplicatas) {
		this.itensDuplicatas = itensDuplicatas;
	}

	public BigDecimal getValorTotalProdutos() {
		return valorTotalProdutos;
	}

	public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
		this.valorTotalProdutos = valorTotalProdutos;
	}

	@Transient
	public BigDecimal getValorTotalProdutoSemDesconto() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProdutoNFCe item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				total = total.add(item.getValorTotalProdutosSemDesconto());
			}
		}

		return total.setScale(2);
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getRetConsReciNFe() {
		return retConsReciNFe;
	}

	public void setRetConsReciNFe(String retConsReciNFe) {
		this.retConsReciNFe = retConsReciNFe;
	}

	public String getRetConsSitNFe() {
		return retConsSitNFe;
	}

	public void setRetConsSitNFe(String retConsSitNFe) {
		this.retConsSitNFe = retConsSitNFe;
	}

	public String getRetEnviNFe() {
		return retEnviNFe;
	}

	public void setRetEnviNFe(String retEnviNFe) {
		this.retEnviNFe = retEnviNFe;
	}

	public String getEnviNFe() {
		return enviNFe;
	}

	public void setEnviNFe(String enviNFe) {
		this.enviNFe = enviNFe;
	}

	public String getRetEnvEventoCancNFe() {
		return retEnvEventoCancNFe;
	}

	public void setRetEnvEventoCancNFe(String retEnvEventoCancNFe) {
		this.retEnvEventoCancNFe = retEnvEventoCancNFe;
	}

	public FormaPagamentoNFCe getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoNFCe formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public FormaPagamentoNFCe getPagamento() {
		return pagamento;
	}

	public void setPagamento(FormaPagamentoNFCe pagamento) {
		this.pagamento = pagamento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
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
		NFCe other = (NFCe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getValorTotalNota() {
		BigDecimal total = BigDecimal.ZERO;

		try {
			total = this.getValorTotalProdutos().add(this.getValorFrete()).add(this.getValorDespesas())
					.add(this.getValorSeguro()).add(BigDecimal.ZERO);
		} catch (Exception e) {
			total = BigDecimal.ZERO;
		}

		this.setValorTotal(total.setScale(2));

		return this.getValorTotal();
	}

	public void recalcularValorTotalDesconto() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProdutoNFCe item : this.getItensProdutos()) {
			if (this.isProdutoValido(item) && item.getValorDesconto() != null) {
				total = total.add(item.getValorDesconto().setScale(2));
			}
		}

		this.setValorDesconto(total.setScale(2));

	}

	public void recalcularValorTotal() {
		this.setValorTotalProdutos(this.calcularTotalProdutos());
	}

	private boolean isProdutoValido(ItemProdutoNFCe item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	private BigDecimal calcularTotalProdutos() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemProdutoNFCe item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getValorTotal().setScale(2, RoundingMode.HALF_EVEN));
			}
		}
		return total;
	}

	@Transient
	public boolean isCadastro() {
		return (StatusNFe.CADASTRO.equals(this.getStatus()) || StatusNFe.FALHA.equals(this.getStatus()));
	}

	public void adicionarItemVazio() {
		if (this.isCadastro()) {
			Produto produto = new Produto();
			ItemProdutoNFCe item = new ItemProdutoNFCe();
			item.setProduto(produto);
			item.setNfce(this);
			this.getItensProdutos().add(0, item);
		}
	}

	public void adicionarItemVazioFormaPagamento() {

		if (this.isCadastro()) {
			ItemDuplicataNFCe item = new ItemDuplicataNFCe();
			item.setFormaPagamento(null);
			item.setNfce(this);
			this.getItensDuplicatas().add(0, item);
		}

	}

	public void removerItemVazioFormaPagamento() {
		if (!this.getItensDuplicatas().isEmpty()) {
			ItemDuplicataNFCe primeiroItem = this.getItensDuplicatas().get(0);
			if (primeiroItem != null && primeiroItem.getFormaPagamento() == null) {
				this.getItensDuplicatas().remove(0);
			}
		}
	}

	public void removerItemVazio() {
		if (!this.getItensProdutos().isEmpty()) {
			ItemProdutoNFCe primeiroItem = this.getItensProdutos().get(0);
			if (primeiroItem != null && primeiroItem.getProduto().getId() == null) {
				this.getItensProdutos().remove(0);
			}
		}
	}

	static void error(String error) {
		System.err.println("| ERROR: " + error);
	}

	static void info(String info) {
		System.out.println("| INFO: " + info);
	}

	@Transient
	public boolean isAlteravel() {
		return this.isCadastro();
	}

	@Transient
	public boolean isNaoAlteravel() {
		return !this.isAlteravel();
	}
}
