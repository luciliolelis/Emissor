package com.amsoft.erp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 80)
	private String nome;
	
	@Column(nullable = false, unique = true, length = 255)
	private String email;
	
	@Column(nullable = false, length = 80)
	private String senha;

	@ManyToOne
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;
	
	@ManyToOne
	@JoinColumn(name = "empresa_grupo_id")
	private EmpresaGrupo empresaGrupo;


	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<EmpresaGrupo> empresasGrupos = new ArrayList<>();

	public EmpresaGrupo getEmpresaGrupo() {
		return empresaGrupo;
	}

	public void setEmpresaGrupo(EmpresaGrupo empresaGrupo) {
		this.empresaGrupo = empresaGrupo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<EmpresaGrupo> getEmpresasGrupos() {
		return empresasGrupos;
	}

	public void setEmpresasGrupos(List<EmpresaGrupo> empresasGrupos) {
		this.empresasGrupos = empresasGrupos;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}