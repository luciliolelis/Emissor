package com.amsoft.erp.model;

import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.enun.TipoCliente;
import com.amsoft.erp.model.enun.TipoPessoa;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private StatusCliente status = StatusCliente.CADASTRO;
	
	@Column(name = "nome", length = 100)
	private String nome;

	@Column(name = "nome_fantasia", length = 100)
	private String nomeFantasia;

	@Column(name = "doc_receita_federal", length = 18)
	private String docReceitaFederal;

	@Column(name = "rg", length = 12)
	private String rg;

	@Column(name = "inscricao_estadual", length = 20)
	private String inscricaoEstadual;

	@Column(name = "inscricao_municipal", length = 20)
	private String inscricaoMunicipal;

	@Column(name = "inscricao_suframa", length = 20)
	private String inscricaoSuframa;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "telefone", length = 14)
	private String telefone;

	@Column(name = "celular", length = 14)
	private String celular;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa", nullable = false)
	private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cliente", nullable = false)
	private TipoCliente tipoCliente = TipoCliente.CLIENTE;

	private Boolean contribuinte = true;
	private Boolean exterior = false;
	private Boolean estrangeiro = false;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Endereco> enderecos = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "cliente_empresa", joinColumns = @JoinColumn(name = "cliente_id"), inverseJoinColumns = @JoinColumn(name = "empresa_id"))
	private List<Empresa> empresas = new ArrayList<>();

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Veiculo> veiculos = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "pais_id", nullable = true)
	private Pais pais;

	@Column(name = "logradouro_exterior")
	private String logradouroExterior = "Indefinido";

	@Column(name = "numero_exterior")
	private String numeroExterior = "000";

	@Column(name = "cidade_exterior")
	private String cidadeExterior = "EXTERIOR";

	@Column(name = "bairro_exterior")
	private String bairroExterior = "Indefinido";

	@Column(name = "uf_exterior", length = 2)
	private String ufExterior = "EX";

	@Column(name = "codigo_postal")
	private String codigoPostal;

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	public StatusCliente getStatus() {
		return status;
	}

	public void setStatus(StatusCliente status) {
		this.status = status;
	}


	@Length(max = 100, message = "é muito longo; máximo 100 caracteres!")
	@Column(nullable = true, length = 100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocReceitaFederal() {
		return docReceitaFederal;
	}

	public void setDocReceitaFederal(String docReceitaFederal) {
		this.docReceitaFederal = docReceitaFederal;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	@Length(max = 100, message = "é muito longo; máximo 100 caracteres!")
	@Column(nullable = true, length = 100)
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getInscricaoSuframa() {
		return inscricaoSuframa;
	}

	public void setInscricaoSuframa(String inscricaoSuframa) {
		this.inscricaoSuframa = inscricaoSuframa;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Boolean getContribuinte() {
		return contribuinte;
	}

	public void setContribuinte(Boolean contribuinte) {
		this.contribuinte = contribuinte;
	}

	public Boolean getExterior() {
		return exterior;
	}

	public void setExterior(Boolean exterior) {
		this.exterior = exterior;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getLogradouroExterior() {
		return logradouroExterior;
	}

	public void setLogradouroExterior(String logradouroExterior) {
		this.logradouroExterior = logradouroExterior;
	}

	public String getNumeroExterior() {
		return numeroExterior;
	}

	public void setNumeroExterior(String numeroExterior) {
		this.numeroExterior = numeroExterior;
	}

	public String getCidadeExterior() {
		return cidadeExterior;
	}

	public void setCidadeExterior(String cidadeExterior) {
		this.cidadeExterior = cidadeExterior;
	}

	public String getBairroExterior() {
		return bairroExterior;
	}

	public void setBairroExterior(String bairroExterior) {
		this.bairroExterior = bairroExterior;
	}

	public String getUfExterior() {
		return ufExterior;
	}

	public void setUfExterior(String ufExterior) {
		this.ufExterior = ufExterior;
	}

	public Boolean getEstrangeiro() {
		return estrangeiro;
	}

	public void setEstrangeiro(Boolean estrangeiro) {
		this.estrangeiro = estrangeiro;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}