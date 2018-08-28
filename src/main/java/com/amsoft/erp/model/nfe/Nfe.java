package com.amsoft.erp.model.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.amsoft.erp.model.produto.Produto;

@Entity
@Table(name = "nfe")
public class Nfe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Column(name = "quantidade_parcelas")
	private Integer quantidadeParcelas = 0;

	@Column(name = "intervalo_parcelas")
	private Integer intervaloParcelas = 30;

	private VeiculoEntrega veiculoEntrega;
	private EnderecoTransportador enderecoTransportador;
	private EnderecoEntrega enderecoEntrega;

	@Column(name = "substituicao_tributaria")
	private String substituicaoTributaria;

	@Column(name = "protocolo")
	private String protocolo;

	@Column(name = "modalidade_frete", nullable = true, length = 15)
	private String modalidadeFrete = "EMITENTE";

	@Column(name = "quantidade_tranporte")
	private BigDecimal quantidade = BigDecimal.ZERO;

	@Column(name = "peso_bruto")
	private BigDecimal pesoBruto = BigDecimal.ZERO;

	@Column(name = "peso_liquido")
	private BigDecimal pesoLiquido = BigDecimal.ZERO;

	@Column(name = "especie_transporte")
	private String estpecieTransporte;

	@Column(length = 2)
	private String modelo = "55";

	private String serie = "1";

	@NotNull
	@Column(name = "numero_nfe", nullable = false)
	private Integer numero;

	@ManyToOne
	@JoinColumn(name = "nfe_id")
	private Nfe nfe;

	@Column(length = 50)
	private String chave;

	@Column(name = "chave_ref", length = 50)
	private String chaveRef;

	@Column(name = "emissao")
	private Date emissao = new Date();

	@Column(name = "entrada_saida")
	private Date dataEntradaOuSaida = new Date();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_documento")
	private TipoDocumento tipoDocumento = TipoDocumento.SAIDA;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = true, length = 20)
	private FormaPagamento formaPagamento;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "finalidade_operacao", nullable = false, length = 20)
	private FinalidadeOperacao finalidadeOperacao = FinalidadeOperacao.NORMAL;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_nota")
	private TipoNota tipoNota = TipoNota.NFE;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "transportador_id", nullable = true)
	private Cliente transportador;

	@ManyToOne
	@JoinColumn(name = "cfop_id", nullable = true)
	private Cfop cfop;

	@Column(name = "nf_serie")
	private String nfSerie;

	@Column(name = "nf_numero")
	private String nfNumero;

	@Column(name = "nf_modelo")
	private String nfModelo;

	@Column(name = "nf_uf")
	private String nfUf;

	@Column(name = "nf_mes_ano")
	private Date nfMesAno;

	@Column(name = "nf_doc_receita_federal", length = 18)
	private String nfDocReceitaFederal;

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemProduto> itensProdutos = new ArrayList<ItemProduto>();

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemDuplicata> itensDuplicatas = new ArrayList<ItemDuplicata>();

	@Access(AccessType.PROPERTY)
	@OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemComplemento> itensComplemento = new ArrayList<ItemComplemento>();

	private BigDecimal valorTotal = BigDecimal.ZERO;
	private BigDecimal valorFrete = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorSeguro = BigDecimal.ZERO;
	private BigDecimal valorDespesas = BigDecimal.ZERO;

	private BigDecimal valorTotalPIS = BigDecimal.ZERO;
	private BigDecimal valorTotalCOFINS = BigDecimal.ZERO;
	private BigDecimal valorTotalProdutos = BigDecimal.ZERO;
	private BigDecimal valorIcms = BigDecimal.ZERO;

	@Column(name = "valor_fcp", precision = 10, scale = 2)
	private BigDecimal valorFCP = BigDecimal.ZERO;
	private BigDecimal valorIcmsSt = BigDecimal.ZERO;
	private BigDecimal valorBaseIcms = BigDecimal.ZERO;
	private BigDecimal valorBaseIcmsSt = BigDecimal.ZERO;

	private BigDecimal valorII = BigDecimal.ZERO;
	private BigDecimal valorIpi = BigDecimal.ZERO;
	private BigDecimal valorTransparencia = BigDecimal.ZERO;

	@Column(name = "bc_icms_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal bcIcmsComp = BigDecimal.ZERO;

	@Column(name = "bc_icms_st_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal bcIcmsStComp = BigDecimal.ZERO;

	@Column(name = "bc_ipi_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal bcIpiComp = BigDecimal.ZERO;

	@Column(name = "aliquota_icms_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal aliquotaIcmsComp = BigDecimal.ZERO;

	@Column(name = "aliquota_icms_st_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal aliquotaIcmsStComp = BigDecimal.ZERO;

	@Column(name = "aliquota_ipi_comp", nullable = true, precision = 10, scale = 2)
	private BigDecimal aliquotaIpiComp = BigDecimal.ZERO;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusNFe status = StatusNFe.CADASTRO;

	@Column(name = "somar_despesas_base_calculo")
	private Boolean somarDespesasBaseCalculo = Boolean.TRUE;

	@Column(name = "somar_seguro_base_calculo")
	private Boolean somarSeguroBaseCalculo = Boolean.TRUE;

	@Column(name = "somar_frete_base_calculo")
	private Boolean somarFreteBaseCalculo = Boolean.TRUE;

	@Column(name = "calcular_conforme_ibpt")
	private Boolean calcularConformeIBPT = Boolean.TRUE;

	@ManyToOne
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@Column(name = "dados_complementares")
	private String dadosComplementares;

	@Column(name = "fisco")
	private String fisco;

	@Column(name = "venda_consumidor_final")
	private boolean vendaConsumidorFinal;

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

	@Column(name = "cce")
	private String cce;

	@Column(name = "retcce")
	private String retcce;

	@Column(name = "seqcce")
	private Integer seqcce;

	public String getCce() {
		return cce;
	}

	public void setCce(String cce) {
		this.cce = cce;
	}

	public String getRetcce() {
		return retcce;
	}

	public void setRetcce(String retcce) {
		this.retcce = retcce;
	}

	public Integer getSeqcce() {
		return seqcce;
	}

	public void setSeqcce(Integer seqcce) {
		this.seqcce = seqcce;
	}

	public String getRetEnvEventoCancNFe() {
		return retEnvEventoCancNFe;
	}

	public void setRetEnvEventoCancNFe(String retEnvEventoCancNFe) {
		this.retEnvEventoCancNFe = retEnvEventoCancNFe;
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

	public String getEnviNFe() {
		return enviNFe;
	}

	public void setEnviNFe(String enviNFe) {
		this.enviNFe = enviNFe;
	}

	public String getRetEnviNFe() {
		return retEnviNFe;
	}

	public void setRetEnviNFe(String retEnviNFe) {
		this.retEnviNFe = retEnviNFe;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public boolean isVendaConsumidorFinal() {
		return vendaConsumidorFinal;
	}

	public void setVendaConsumidorFinal(boolean vendaConsumidorFinal) {
		this.vendaConsumidorFinal = vendaConsumidorFinal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModalidadeFrete() {
		return modalidadeFrete;
	}

	public void setModalidadeFrete(String modalidadeFrete) {
		this.modalidadeFrete = modalidadeFrete;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
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

	public Date getDataEntradaOuSaida() {
		return dataEntradaOuSaida;
	}

	public void setDataEntradaOuSaida(Date dataEntradaOuSaida) {
		this.dataEntradaOuSaida = dataEntradaOuSaida;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getTransportador() {
		return transportador;
	}

	public void setTransportador(Cliente transportador) {
		this.transportador = transportador;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public FinalidadeOperacao getFinalidadeOperacao() {
		return finalidadeOperacao;
	}

	public void setFinalidadeOperacao(FinalidadeOperacao finalidadeOperacao) {
		this.finalidadeOperacao = finalidadeOperacao;
	}

	public TipoNota getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(TipoNota tipoNota) {
		this.tipoNota = tipoNota;
	}

	public Cfop getCfop() {
		return cfop;
	}

	public void setCfop(Cfop cfop) {
		this.cfop = cfop;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getNfSerie() {
		return nfSerie;
	}

	public void setNfSerie(String nfSerie) {
		this.nfSerie = nfSerie;
	}

	public String getNfNumero() {
		return nfNumero;
	}

	public void setNfNumero(String nfNumero) {
		this.nfNumero = nfNumero;
	}

	public String getNfModelo() {
		return nfModelo;
	}

	public void setNfModelo(String nfModelo) {
		this.nfModelo = nfModelo;
	}

	public String getNfUf() {
		return nfUf;
	}

	public void setNfUf(String nfUf) {
		this.nfUf = nfUf;
	}

	public Date getNfMesAno() {
		return nfMesAno;
	}

	public void setNfMesAno(Date nfMesAno) {
		this.nfMesAno = nfMesAno;
	}

	public String getNfDocReceitaFederal() {
		return nfDocReceitaFederal;
	}

	public void setNfDocReceitaFederal(String nfDocReceitaFederal) {
		this.nfDocReceitaFederal = nfDocReceitaFederal;
	}

	public List<ItemProduto> getItensProdutos() {
		return itensProdutos;
	}

	public void setItensProdutos(List<ItemProduto> itensProdutos) {
		this.itensProdutos = itensProdutos;
	}

	public List<ItemDuplicata> getItensDuplicatas() {
		return itensDuplicatas;
	}

	public void setItensDuplicatas(List<ItemDuplicata> itensDuplicatas) {
		this.itensDuplicatas = itensDuplicatas;
	}

	public List<ItemComplemento> getItensComplemento() {
		return itensComplemento;
	}

	public void setItensComplemento(List<ItemComplemento> itensComplemento) {
		this.itensComplemento = itensComplemento;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	@NotNull
	@Column(name = "valor_total", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Column(name = "valor_total_pis", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorTotalPIS() {
		return valorTotalPIS;
	}

	public void setValorTotalPIS(BigDecimal valorTotalPIS) {
		this.valorTotalPIS = valorTotalPIS;
	}

	@Column(name = "valor_total_cofins", nullable = true, precision = 10, scale = 2)
	public BigDecimal getValorTotalCOFINS() {
		return valorTotalCOFINS;
	}

	public void setValorTotalCOFINS(BigDecimal valorTotalCOFINS) {
		this.valorTotalCOFINS = valorTotalCOFINS;
	}

	public Boolean getCalcularConformeIBPT() {
		return calcularConformeIBPT;
	}

	public void setCalcularConformeIBPT(Boolean calcularConformeIBPT) {
		this.calcularConformeIBPT = calcularConformeIBPT;
	}

	@NotNull
	@Column(name = "valor_frete", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	@NotNull
	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public StatusNFe getStatus() {
		return status;
	}

	public void setStatus(StatusNFe status) {
		this.status = status;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Column(name = "valor_total_produtos", precision = 10, scale = 2)
	public BigDecimal getValorTotalProdutos() {
		return valorTotalProdutos;
	}

	public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
		this.valorTotalProdutos = valorTotalProdutos;
	}

	public BigDecimal getValorFCP() {
		return valorFCP;
	}

	public void setValorFCP(BigDecimal valorFCP) {
		this.valorFCP = valorFCP;
	}

	@Column(name = "valor_icms", precision = 10, scale = 2)
	public BigDecimal getValorIcms() {
		return valorIcms;
	}

	public void setValorIcms(BigDecimal valorIcms) {
		this.valorIcms = valorIcms;
	}

	@Column(name = "valor_icms_st", precision = 10, scale = 2)
	public BigDecimal getValorIcmsSt() {
		return valorIcmsSt;
	}

	public void setValorIcmsSt(BigDecimal valorIcmsSt) {
		this.valorIcmsSt = valorIcmsSt;
	}

	@Column(name = "valor_base_icms", precision = 10, scale = 2)
	public BigDecimal getValorBaseIcms() {
		return valorBaseIcms;
	}

	public void setValorBaseIcms(BigDecimal valorBaseIcms) {
		this.valorBaseIcms = valorBaseIcms;
	}

	@Column(name = "valor_base_icms_st", precision = 10, scale = 2)
	public BigDecimal getValorBaseIcmsSt() {
		return valorBaseIcmsSt;
	}

	public void setValorBaseIcmsSt(BigDecimal valorBaseIcmsSt) {
		this.valorBaseIcmsSt = valorBaseIcmsSt;
	}

	@Column(name = "valor_seguro", precision = 10, scale = 2)
	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	@Column(name = "valor_despesas", precision = 10, scale = 2)
	public BigDecimal getValorDespesas() {
		return valorDespesas;
	}

	public void setValorDespesas(BigDecimal valorDespesas) {
		this.valorDespesas = valorDespesas;
	}

	@Column(name = "valor_ii", precision = 10, scale = 2)
	public BigDecimal getValorII() {
		return valorII;
	}

	public void setValorII(BigDecimal valorII) {
		this.valorII = valorII;
	}

	@Column(name = "valor_ipi", precision = 10, scale = 2)
	public BigDecimal getValorIpi() {
		return valorIpi;
	}

	public void setValorIpi(BigDecimal valorIpi) {
		this.valorIpi = valorIpi;
	}

	@Column(name = "valor_transparencia", precision = 10, scale = 2)
	public BigDecimal getValorTransparencia() {
		return valorTransparencia;
	}

	public void setValorTransparencia(BigDecimal valorTransparencia) {
		this.valorTransparencia = valorTransparencia;
	}

	@Embedded
	public VeiculoEntrega getVeiculoEntrega() {
		return veiculoEntrega;
	}

	public void setVeiculoEntrega(VeiculoEntrega veiculoEntrega) {
		this.veiculoEntrega = veiculoEntrega;
	}

	@Embedded
	public EnderecoTransportador getEnderecoTransportador() {
		return enderecoTransportador;
	}

	public void setEnderecoTransportador(EnderecoTransportador enderecoTransportador) {
		this.enderecoTransportador = enderecoTransportador;
	}

	@Embedded
	public EnderecoEntrega getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public String getSubstituicaoTributaria() {
		return substituicaoTributaria;
	}

	public void setSubstituicaoTributaria(String substituicaoTributaria) {
		this.substituicaoTributaria = substituicaoTributaria;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPesoBruto() {
		return pesoBruto;
	}

	public void setPesoBruto(BigDecimal pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	public BigDecimal getPesoLiquido() {
		return pesoLiquido;
	}

	public void setPesoLiquido(BigDecimal pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	public String getEstpecieTransporte() {
		return estpecieTransporte;
	}

	public void setEstpecieTransporte(String estpecieTransporte) {
		this.estpecieTransporte = estpecieTransporte;
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

	public BigDecimal getBcIcmsComp() {
		return bcIcmsComp;
	}

	public void setBcIcmsComp(BigDecimal bcIcmsComp) {
		this.bcIcmsComp = bcIcmsComp;
	}

	public BigDecimal getBcIcmsStComp() {
		return bcIcmsStComp;
	}

	public void setBcIcmsStComp(BigDecimal bcIcmsStComp) {
		this.bcIcmsStComp = bcIcmsStComp;
	}

	public BigDecimal getBcIpiComp() {
		return bcIpiComp;
	}

	public void setBcIpiComp(BigDecimal bcIpiComp) {
		this.bcIpiComp = bcIpiComp;
	}

	public BigDecimal getAliquotaIcmsComp() {
		return aliquotaIcmsComp;
	}

	public void setAliquotaIcmsComp(BigDecimal aliquotaIcmsComp) {
		this.aliquotaIcmsComp = aliquotaIcmsComp;
	}

	public BigDecimal getAliquotaIcmsStComp() {
		return aliquotaIcmsStComp;
	}

	public void setAliquotaIcmsStComp(BigDecimal aliquotaIcmsStComp) {
		this.aliquotaIcmsStComp = aliquotaIcmsStComp;
	}

	public BigDecimal getAliquotaIpiComp() {
		return aliquotaIpiComp;
	}

	public void setAliquotaIpiComp(BigDecimal aliquotaIpiComp) {
		this.aliquotaIpiComp = aliquotaIpiComp;
	}

	public Nfe getNfe() {
		return nfe;
	}

	public void setNfe(Nfe nfe) {
		this.nfe = nfe;
	}

	public String getChaveRef() {
		return chaveRef;
	}

	public void setChaveRef(String chaveRef) {
		this.chaveRef = chaveRef;
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
		Nfe other = (Nfe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Total nfe = protutos + frete + despesas + seguro + ipi + icms st
	 * 
	 * @return
	 */
	@Transient
	public BigDecimal getValorTotalNota() {
		BigDecimal total = BigDecimal.ZERO;

		try {
			total = this.getValorTotalProdutos().add(this.getValorFrete()).add(this.getValorDespesas())
					.add(this.getValorSeguro()).add(this.getValorIpi()).add(getValorIcmsSt());
		} catch (Exception e) {
			total = BigDecimal.ZERO;
		}

		this.setValorTotal(total.setScale(2));

		return this.getValorTotal();
	}

	@Transient
	public BigDecimal getZero() {
		return BigDecimal.ZERO;
	}

	@Transient
	public BigDecimal getIcmsCompelentar() {
		BigDecimal ret = BigDecimal.ZERO;

		if (isBcIcmsComplementar() && isAliquotaIcmsComplementar())
			ret = this.getBcIcmsComp().multiply(this.getAliquotaIcmsComp().divide(new BigDecimal(100))).setScale(2,
					RoundingMode.HALF_UP);
		;

		return ret;
	}

	@Transient
	public BigDecimal getIcmsStCompelentar() {
		BigDecimal ret = BigDecimal.ZERO;

		if (isBcIcmsStComplementar() && isAliquotaIcmsStComplementar())
			ret = this.getBcIcmsStComp().multiply(this.getAliquotaIcmsStComp().divide(new BigDecimal(100))).setScale(2,
					RoundingMode.HALF_UP);
		;

		return ret;
	}

	@Transient
	public BigDecimal getIpiCompelentar() {
		BigDecimal ret = BigDecimal.ZERO;
		if (isBcIpiComplementar() && isAliquotaIpiComplementar())
			ret = this.getBcIpiComp().multiply(this.getAliquotaIpiComp().divide(new BigDecimal(100))).setScale(2,
					RoundingMode.HALF_UP);
		;

		return ret;
	}

	boolean isBcIcmsComplementar() {
		return this.getBcIcmsComp() != null && isMaiorQZero(this.getBcIcmsComp());
	}

	boolean isAliquotaIcmsComplementar() {
		return this.getAliquotaIcmsComp() != null && isMaiorQZero(this.getAliquotaIcmsComp());
	}

	boolean isBcIcmsStComplementar() {
		return this.getBcIcmsStComp() != null && isMaiorQZero(this.getBcIcmsStComp());
	}

	boolean isAliquotaIcmsStComplementar() {
		return this.getAliquotaIcmsStComp() != null && isMaiorQZero(this.getAliquotaIcmsStComp());
	}

	boolean isBcIpiComplementar() {
		return this.getBcIpiComp() != null && isMaiorQZero(this.getBcIpiComp());
	}

	boolean isAliquotaIpiComplementar() {
		return this.getAliquotaIpiComp() != null && isMaiorQZero(this.getAliquotaIpiComp());
	}

	@Transient
	public boolean isDescontoGeral() {
		BigDecimal totalDesconto = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				totalDesconto = totalDesconto.add(item.getValorDesconto());
			}
		}

		return (totalDesconto.compareTo(BigDecimal.ZERO) == 0);
	}

	public boolean isNotDescontoGeral() {
		return !this.isDescontoGeral();
	}

	@Transient
	public List<ItemDuplicata> adicionarItemParcela() {
		this.getItensDuplicatas().removeAll(getItensDuplicatas());
		Date d = this.getEmissao();

		Calendar c = Calendar.getInstance();
		c.setTime(d);

		if (this.getQuantidadeParcelas() > 0) {
			BigDecimal valorParcela = this.getValorTotalNota().divide(new BigDecimal(this.getQuantidadeParcelas()), 2,
					RoundingMode.HALF_EVEN);

			for (Integer i = 0; i < this.getQuantidadeParcelas(); i++) {
				ItemDuplicata item = new ItemDuplicata();
				item.setNumeroDuplicata("00" + (i + 1));
				item.setValorParcela(valorParcela);

				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + this.getIntervaloParcelas());
				item.setDataVencimento(c.getTime());

				item.setNfe(this);
				this.getItensDuplicatas().add(item);
			}
		}

		return this.getItensDuplicatas();
	}

	public void adicionarItemVazio() {
		if (this.isOrcamento()) {
			Produto produto = new Produto();
			ItemProduto item = new ItemProduto();
			item.setProduto(produto);
			item.setNfe(this);
			this.getItensProdutos().add(0, item);
		}
	}

	public void removerItemVazio() {
		if (!this.getItensProdutos().isEmpty()) {
			ItemProduto primeiroItem = this.getItensProdutos().get(0);
			if (primeiroItem != null && primeiroItem.getProduto().getId() == null) {
				this.getItensProdutos().remove(0);
			}
		}
	}

	public void recalcularValorTotal() {
		this.setValorTotalProdutos(this.calcularTotalProdutos());
	}

	private boolean isProdutoValido(ItemProduto item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	private BigDecimal calcularTotalProdutos() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getValorTotal());
			}
		}
		return total.setScale(2);
	}

	public void recalcularTotalIpi() {
		info("Recalcular Total Ipi ----------");
		this.setValorIpi(this.calcularTotalIpi());
	}

	public void recalcularTotalCofins() {
		info("Recalcular Total COFINS ----------");
		this.setValorTotalCOFINS(this.calcularTotalCofins());
	}

	private BigDecimal calcularTotalCofins() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getCofins());
			}
		}

		info("COFINS total : " + total);
		return total.setScale(2);
	}

	public void recalcularTotalPis() {
		info("Recalcular Total PIS ----------");
		this.setValorTotalPIS(this.calcularTotalPis());
	}

	private BigDecimal calcularTotalPis() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getPis());
			}
		}

		info("PIS total : " + total);
		return total.setScale(2);
	}

	private BigDecimal calcularTotalIpi() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getValorIpi());
			}
		}

		info("IPI total : " + total);
		return total.setScale(2);
	}

	public void recalcularTotalBaseIcms() {
		info("Recalcular Total Base Icms ----------");

		BigDecimal total = this.calcularTotalBaseIcms();
		this.setValorBaseIcms(total);
	}

	private BigDecimal calcularTotalBaseIcms() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getBaseIcms());
			}
		}

		info("Base icms total : " + total);
		return total.setScale(2);
	}

	public void recalcularTotalBaseIcmsSt() {
		info("Recalcular Total Base Icms St ----------");
		this.setValorBaseIcmsSt(this.calcularTotalBaseIcmsSt());
	}

	private BigDecimal calcularTotalBaseIcmsSt() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getBaseIcmsSt());
			}
		}

		info("Base icms st total : " + total);
		return total.setScale(2);
	}

	public void recalcularTotalIcms() {
		info("Recalcular Total Icms ----------");
		this.setValorIcms(this.calcularTotalIcms());
	}

	private BigDecimal calcularTotalIcms() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getValorIcms());
			}
		}
		info("icms total : " + total);
		return total.setScale(2);
	}

	public void recalcularTotalIcmsSt() {
		info("Recalcular Total Icm St ----------");
		this.setValorIcmsSt(this.calcularTotalIcmsSt());
	}

	private BigDecimal calcularTotalIcmsSt() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (isProdutoValido(item)) {
				total = total.add(item.getValorIcmsSt());
			}
		}

		info("icms st total : " + total);

		return total.setScale(2);
	}

	public void recalcularValorTotalDesconto() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item) && item.getValorDesconto() != null) {
				total = total.add(item.getValorDesconto().setScale(2));
			}
		}

		this.setValorDesconto(total.setScale(2));

	}

	@Transient
	public BigDecimal getValorTotalUnitario() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item) && item.getValorUnitario() != null) {
				total = total.add(item.getValorUnitario().setScale(2));
			}
		}

		return total.setScale(2);
	}

	@Transient
	public BigDecimal getValorTotalSemDesconto() {
		BigDecimal total = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item) && item.getValorUnitario() != null) {
				total = total.add(item.getValorTotalProdutosSemDesconto());
			}
		}

		return total.setScale(2);
	}

	@Transient
	public BigDecimal getValorTotalParcelas() {
		BigDecimal total = BigDecimal.ZERO;

		try {
			for (ItemDuplicata item : this.getItensDuplicatas()) {
				total = total.add(item.getValorParcela());
			}
		} catch (Exception e) {
			error(e.getCause() + " - " + e.getMessage());
			this.setItensDuplicatas(new ArrayList<ItemDuplicata>());
		}

		return total.setScale(2);
	}

	@Transient
	public TipoDocumento getTipo() {
		return this.getTipoDocumento();
	}

	public void rateiaFrete() {

		if (this.getValorFrete() == null) {
			this.setValorFrete(BigDecimal.ZERO);
		}

		BigDecimal totalProdutos = this.calcularTotalProdutos();

		if ((totalProdutos.compareTo(BigDecimal.ZERO) > 0)) {
			BigDecimal media = this.getValorFrete().divide(totalProdutos, 4, RoundingMode.HALF_EVEN);
			for (ItemProduto item : this.getItensProdutos()) {
				if (this.isProdutoValido(item)) {
					item.setValorFrete(media.multiply(item.getValorTotal()).setScale(4, RoundingMode.HALF_EVEN));
				}
			}
		}
	}

	public void ajustaFrete() {

		BigDecimal dif = dif(this.getTotalRateioFrete(), this.getValorFrete());

		info("Dif frete: " + dif.toString() + " ******************************");

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {

				if (isMaiorQZero(dif)) {
					item.setValorFrete(item.getValorFrete().subtract(dif));
				} else if (isMenorQZero(dif)) {
					item.setValorFrete(item.getValorFrete().add(dif.multiply(new BigDecimal(-1))));
				}

				return;
			}
		}
	}

	@Transient
	public BigDecimal getTotalRateioFrete() {

		BigDecimal ret = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				ret = ret.add(item.getValorFrete());
			}
		}

		return ret;
	}

	public void rateiaDespesas() {

		if (this.getValorDespesas() == null) {
			this.setValorDespesas(BigDecimal.ZERO);
		}

		BigDecimal totalProdutos = this.calcularTotalProdutos();

		if ((totalProdutos.compareTo(BigDecimal.ZERO) > 0)) {

			BigDecimal media = this.getValorDespesas().divide(totalProdutos, 4, RoundingMode.HALF_EVEN);

			for (ItemProduto item : this.getItensProdutos()) {
				if (this.isProdutoValido(item)) {
					item.setValorDespesa(media.multiply(item.getValorTotal()).setScale(4, RoundingMode.HALF_EVEN));
				}
			}
		}
	}

	public void ajustaDespesas() {

		BigDecimal dif = dif(this.getTotalRateioDespesas(), this.getValorDespesas());

		info("Dif despesas: " + dif.toString() + " ******************************");

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				if (isMaiorQZero(dif)) {
					item.setValorDespesa(item.getValorDespesa().subtract(dif));
				} else if (isMenorQZero(dif)) {
					item.setValorDespesa(item.getValorDespesa().add(dif.multiply(new BigDecimal(-1))));
				}

				return;
			}
		}
	}

	@Transient
	public BigDecimal getTotalRateioDespesas() {

		BigDecimal ret = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				ret = ret.add(item.getValorDespesa());
			}
		}

		return ret;
	}

	public void rateiaSeguro() {

		if (this.getValorSeguro() == null) {
			this.setValorSeguro(BigDecimal.ZERO);
		}

		BigDecimal totalProdutos = this.calcularTotalProdutos();

		if ((totalProdutos.compareTo(BigDecimal.ZERO) > 0)) {

			BigDecimal media = this.getValorSeguro().divide(totalProdutos, 4, RoundingMode.HALF_EVEN);

			for (ItemProduto item : this.getItensProdutos()) {
				if (this.isProdutoValido(item)) {
					item.setValorSeguro(media.multiply(item.getValorTotal()).setScale(4, RoundingMode.HALF_EVEN));
				}
			}
		}
	}

	public void ajustaSeguro() {

		BigDecimal dif = dif(this.getTotalRateioSeguro(), this.getValorSeguro());

		info("Dif seguro: " + dif.toString() + " ******************************");

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {

				if (isMaiorQZero(dif)) {
					item.setValorSeguro(item.getValorSeguro().subtract(dif));
				} else if (isMenorQZero(dif)) {
					item.setValorSeguro(item.getValorSeguro().add(dif.multiply(new BigDecimal(-1))));
				}

				return;
			}
		}
	}

	@Transient
	public BigDecimal getTotalRateioSeguro() {

		BigDecimal ret = BigDecimal.ZERO;

		for (ItemProduto item : this.getItensProdutos()) {
			if (this.isProdutoValido(item)) {
				ret = ret.add(item.getValorSeguro());
			}
		}

		return ret;
	}

	static BigDecimal dif(BigDecimal x, BigDecimal y) {
		return x.subtract(y);
	}

	static boolean isMaiorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) > 0);
	}

	static boolean isMenorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) < 0);
	}

	static void error(String error) {
		System.err.println("| ERROR: " + error);
	}

	static void info(String info) {
		System.out.println("| INFO: " + info);
	}

	@Transient
	public boolean isOrcamento() {
		return (StatusNFe.CADASTRO.equals(this.getStatus()) || StatusNFe.FALHA.equals(this.getStatus()));
	}

	@Transient
	public boolean isInutilizada() {
		return (StatusNFe.INUTILIZADA.equals(this.getStatus()));
	}

	@Transient
	public boolean isAlteravel() {
		return this.isOrcamento();
	}

	@Transient
	public boolean isNaoAlteravel() {
		return !this.isAlteravel();
	}

	@Transient
	public boolean isEmitido() {
		return StatusNFe.AUTORIZADA.equals(this.getStatus());
	}

	@Transient
	public boolean isEmProcessamento() {
		return StatusNFe.EMPROCESSAMENTO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoEmitido() {
		return !this.isEmitido();
	}

	@Transient
	public boolean isNaoEmissivel() {
		return !this.isEmissivel();
	}

	@Transient
	public boolean isEmissivel() {
		return this.isExistente() && this.isOrcamento();
	}

	@Transient
	public boolean isCancelavel() {
		return this.isExistente() && !this.isCancelado();
	}

	@Transient
	private boolean isCancelado() {
		return StatusNFe.CANCELADA.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoCancelavel() {
		return !this.isCancelavel();
	}

	@Transient
	public boolean isNovo() {
		return getId() == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	@Transient
	public boolean isDesabilitaCorrecao() {
		boolean ret = false;

		if (getCce() == null) {
			return ret;
		}

		return getCce().length() < 15;
	}
}