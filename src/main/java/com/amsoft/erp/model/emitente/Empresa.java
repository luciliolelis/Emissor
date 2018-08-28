package com.amsoft.erp.model.emitente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.StatusEmpresa;
import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.nfe.Cfop;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty
	@Column(name = "nome_fantasia", nullable = false, length = 80)
	private String nome_fantasia;

	private String dadosadicionais;

	private StatusEmpresa status = StatusEmpresa.CADASTRO;

	@Column(name = "aliquota_credito_icms", precision = 10, scale = 2)
	private BigDecimal aliquotaCreditoIcms = BigDecimal.ZERO;

	@NotEmpty
	@Column(name = "razao_social", nullable = false, length = 120)
	private String razao_social;

	@Column(columnDefinition = "text")
	public String getDadosadicionais() {
		return dadosadicionais;
	}

	public void setDadosadicionais(String dadosadicionais) {
		this.dadosadicionais = dadosadicionais;
	}

	private byte[] logo;
	private String logo_nome;

	@Column(name = "certificado")
	private byte[] certificado;

	@Column(name = "nome_certificado")
	private String nomeCertificado;

	@Column(name = "senha_certificado")
	private String senhaCertificado;

	// @CNPJ
	@NotEmpty
	@Column(nullable = false, length = 18)
	private String cnpj;

	@Email
	private String mail;

	@NotEmpty
	private String logradouro;

	@NotEmpty
	private String bairro;

	@NotEmpty
	private String numero;
	private String complemento;

	@NotEmpty
	@Column(name = "cep", length = 9)
	private String cep;

	@NotEmpty
	@Column(name = "cidade", length = 2)
	private String cidade;

	@NotEmpty
	@Column(name = "uf", length = 2)
	private String uf;

	@Column(name = "ibge_cidade", length = 10)
	private String ibgeCidade;

	@Column(name = "ibge_estado", length = 10)
	private String ibgeEstado;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "regime_tributario", nullable = false)
	private RegimeTributario regimeTributario;

	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<InscricaoEstadualST> inscricoesEstaduais = new ArrayList<>();

	@OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<FundoCombatePobreza> fundoCombatePobrezaItens = new ArrayList<>();

	private String fone;
	private Long ultima_alteracao;
	private String inscricao_municipal;
	private String inscricao_estadual;

	@ManyToMany(mappedBy = "empresas")
	private List<Cliente> clientes = new ArrayList<>();

	public byte[] getCertificado() {
		return certificado;
	}

	public void setCertificado(byte[] certificado) {
		this.certificado = certificado;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	public StatusEmpresa getStatus() {
		return status;
	}

	public void setStatus(StatusEmpresa status) {
		this.status = status;
	}

	public String getNomeCertificado() {
		return nomeCertificado;
	}

	public void setNomeCertificado(String nomeCertificado) {
		this.nomeCertificado = nomeCertificado;
	}

	public String getSenhaCertificado() {
		return senhaCertificado;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		this.senhaCertificado = senhaCertificado;
	}

	@Column(name = "chave_nfe_consulta")
	private String chaveNfeConculta;

	@Column(name = "ws_ambiente")
	private Integer wsAmbiente;

	@Column(name = "ws_qrcode")
	private String wsQRCode;

	@Column(name = "csc")
	private String csc;

	@Column(name = "csc_homologacao")
	private String cscHomologacao;

	@Column(name = "token")
	private String token;

	@ManyToOne
	@JoinColumn(name = "cfop_id", nullable = true)
	private Cfop cfop_nfce;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = true)
	private Cliente cliente;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cfop getCfop_nfce() {
		return cfop_nfce;
	}

	public void setCfop_nfce(Cfop cfop_nfce) {
		this.cfop_nfce = cfop_nfce;
	}

	public String getCsc() {
		return csc;
	}

	public void setCsc(String csc) {
		this.csc = csc;
	}

	public String getCscHomologacao() {
		return cscHomologacao;
	}

	public void setCscHomologacao(String cscHomologacao) {
		this.cscHomologacao = cscHomologacao;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getWsQRCode() {
		return wsQRCode;
	}

	public void setWsQRCode(String wsQRCode) {
		this.wsQRCode = wsQRCode;
	}

	public String getChaveNfeConculta() {
		return chaveNfeConculta;
	}

	public void setChaveNfeConculta(String chaveNfeConculta) {
		this.chaveNfeConculta = chaveNfeConculta;
	}

	public Integer getWsAmbiente() {
		return wsAmbiente;
	}

	public void setWsAmbiente(Integer wsAmbiente) {
		this.wsAmbiente = wsAmbiente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getLogo_nome() {
		return logo_nome;
	}

	public void setLogo_nome(String logo_nome) {
		this.logo_nome = logo_nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbgeCidade() {
		return ibgeCidade;
	}

	public void setIbgeCidade(String ibgeCidade) {
		this.ibgeCidade = ibgeCidade;
	}

	public String getIbgeEstado() {
		return ibgeEstado;
	}

	public void setIbgeEstado(String ibgeEstado) {
		this.ibgeEstado = ibgeEstado;
	}

	public RegimeTributario getRegimeTributario() {
		return regimeTributario;
	}

	public void setRegimeTributario(RegimeTributario regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public List<InscricaoEstadualST> getInscricoesEstaduais() {
		return inscricoesEstaduais;
	}

	public void setInscricoesEstaduais(List<InscricaoEstadualST> inscricoesEstaduais) {
		this.inscricoesEstaduais = inscricoesEstaduais;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public Long getUltima_alteracao() {
		return ultima_alteracao;
	}

	public void setUltima_alteracao(Long ultima_alteracao) {
		this.ultima_alteracao = ultima_alteracao;
	}

	public String getInscricao_municipal() {
		return inscricao_municipal;
	}

	public void setInscricao_municipal(String inscricao_municipal) {
		this.inscricao_municipal = inscricao_municipal;
	}

	public String getInscricao_estadual() {
		return inscricao_estadual;
	}

	public void setInscricao_estadual(String inscricao_estadual) {
		this.inscricao_estadual = inscricao_estadual;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<FundoCombatePobreza> getFundoCombatePobrezaItens() {
		return fundoCombatePobrezaItens;
	}

	public void setFundoCombatePobrezaItens(List<FundoCombatePobreza> fundoCombatePobrezaItens) {
		this.fundoCombatePobrezaItens = fundoCombatePobrezaItens;
	}

	
	public BigDecimal getAliquotaCreditoIcms() {
		return aliquotaCreditoIcms;
	}

	public void setAliquotaCreditoIcms(BigDecimal aliquotaCreditoIcms) {
		this.aliquotaCreditoIcms = aliquotaCreditoIcms;
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void adicionarItemVazio() {

		if (this.getInscricoesEstaduais().isEmpty()) {
			CepEstado uf = new CepEstado();
			InscricaoEstadualST item = new InscricaoEstadualST();
			item.setUf(uf);
			item.setEmpresa(this);
			this.getInscricoesEstaduais().add(0, item);
		} else if (this.getInscricoesEstaduais().get(0).getUf().getUf() != null) {
			CepEstado uf = new CepEstado();
			InscricaoEstadualST item = new InscricaoEstadualST();
			item.setUf(uf);
			item.setEmpresa(this);
			this.getInscricoesEstaduais().add(0, item);
		}
	}

	public void adicionarItemVazioFcp() {

		if (this.getFundoCombatePobrezaItens().isEmpty()) {
			CepEstado uf = new CepEstado();
			FundoCombatePobreza item = new FundoCombatePobreza();
			item.setUf(uf);
			item.setEmpresa(this);
			this.getFundoCombatePobrezaItens().add(0, item);
		} else if (this.getFundoCombatePobrezaItens().get(0).getUf().getUf() != null) {
			CepEstado uf = new CepEstado();
			FundoCombatePobreza item = new FundoCombatePobreza();
			item.setUf(uf);
			item.setEmpresa(this);
			this.getFundoCombatePobrezaItens().add(0, item);
		}
	}

	public void removerItemVazio() {
		if (!this.getInscricoesEstaduais().isEmpty()) {
			InscricaoEstadualST primeiroItem = this.getInscricoesEstaduais().get(0);
			if (primeiroItem != null && primeiroItem.getUf().getUf() == null) {
				this.getInscricoesEstaduais().remove(0);
			}
		}
	}

	public void removerItemVazioFcp() {
		if (!this.getFundoCombatePobrezaItens().isEmpty()) {
			FundoCombatePobreza primeiroItem = this.getFundoCombatePobrezaItens().get(0);
			if (primeiroItem != null && primeiroItem.getUf().getUf() == null) {
				this.getFundoCombatePobrezaItens().remove(0);
			}
		}
	}

}