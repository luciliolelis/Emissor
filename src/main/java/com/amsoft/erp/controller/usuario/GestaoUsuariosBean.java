package com.amsoft.erp.controller.usuario;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.EmpresaGrupo;
import com.amsoft.erp.model.Grupo;
import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.repository.Grupos;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.service.CadastroUsuarioService;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GestaoUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios usuarios;

	@Inject
	private Empresas empresas;

	@Inject
	private CadastroUsuarioService cadastroUsuario;

	private List<EmpresaGrupo> gruposEmprsasUsuario;

	public List<EmpresaGrupo> getGruposEmprsasUsuario() {
		return gruposEmprsasUsuario;
	}

	public void setGruposEmprsasUsuario(List<EmpresaGrupo> gruposEmprsasUsuario) {
		this.gruposEmprsasUsuario = gruposEmprsasUsuario;
	}

	private List<Usuario> todosUsuarios;
	private Usuario usuarioEdicao = new Usuario();
	private Usuario usuarioSelecionado;

	private Empresa empresaSelecionada;

	@Inject
	private Grupos grupos;
	private List<Grupo> gruposTodos;
	private Grupo grupoSelecionado;

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	@PostConstruct
	public void init() {
		gruposTodos = this.grupos.todos();
	}

	public List<Grupo> getGruposTodos() {
		return gruposTodos;
	}

	public void setGruposTodos(List<Grupo> gruposTodos) {
		this.gruposTodos = gruposTodos;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	private List<Empresa> todasEmpresas;

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			this.onEmpresasUsuario();
		}
	}

	private void onEmpresasUsuario() {
		List<Empresa> aux = new ArrayList<>();
		todasEmpresas = empresas.todas();
		aux.addAll(this.todasEmpresas);

		if (this.usuarioEdicao != null) {
			for (EmpresaGrupo x : this.usuarioEdicao.getEmpresasGrupos()) {
				for (Empresa y : aux) {
					if (x.getEmpresa().getId().equals(y.getId())) {
						this.todasEmpresas.remove(y);
					}
				}
			}
		}
	}

	public void prepararNovoCadastro() {
		usuarioEdicao = new Usuario();
	}

	public void log() {
		System.out.println("onGravaLogAcesso");
	}

	public void salvar() {

		try {

			if (this.usuarioEdicao.getEmpresasGrupos().isEmpty()) {
				throw new NegocioException("Informe pelo menos um emitente");
			}

			if (this.usuarioEdicao.getEmpresa() == null) {
				this.usuarioEdicao.setEmpresa(this.usuarioEdicao
						.getEmpresasGrupos().get(0).getEmpresa());
			}

			if (this.usuarioEdicao.getEmpresaGrupo() == null) {
				this.usuarioEdicao.setEmpresaGrupo(this.retGrupoEmpresaByUsuario());
			}

			this.usuarioEdicao = cadastroUsuario.salvar(usuarioEdicao);
			consultar();
			FacesUtil.addInfoMessage("Usuário salvo com sucesso!");

			this.onEmpresasUsuario();

		} catch (Exception e) {
			e.printStackTrace();
			throw new NegocioException(e.getMessage());
		}
	}

	private EmpresaGrupo retGrupoEmpresaByUsuario() {
		EmpresaGrupo ret = null;

		for (EmpresaGrupo grupo : this.usuarioEdicao.getEmpresasGrupos()) {

			System.out.println(grupo.getEmpresa().getId());
			System.out.println(this.usuarioEdicao.getEmpresa().getId());

			if (grupo.getEmpresa().getId()
					.equals(this.usuarioEdicao.getEmpresa().getId())) {
				ret = grupo;
			}
		}

		return ret;
	}

	public Usuario salvar(Usuario usuario) {
		return this.cadastroUsuario.salvar(usuario);
	}

	public LogAcesso salvarLogAcesso(LogAcesso log) {
		return this.cadastroUsuario.salvarLogAcesso(log);
	}

	public void removerEmpresa() {
		FacesUtil.addInfoMessage("Empresa removida com sucesso!");
	}

	public void excluir(Usuario usuario) {
		cadastroUsuario.excluir(usuario);
		usuarioSelecionado = null;
		consultar();
		FacesUtil.addInfoMessage("Usuário excluído com sucesso!");
	}

	public void consultar() {
		todosUsuarios = usuarios.todos();
	}

	public void consultarEmpresas() {
		todasEmpresas = empresas.todas();
	}

	public List<Usuario> getTodosusuarios() {
		return todosUsuarios;
	}

	public Usuario getUsuarioEdicao() {
		return usuarioEdicao;
	}

	public void setUsuarioEdicao(Usuario usuarioEdicao) {
		this.usuarioEdicao = usuarioEdicao;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public boolean isEditando() {
		return this.usuarioEdicao.getId() != null;
	}

	public void onConfirmaGrupo() {
		System.out.println("Vincula Grupo de usuários");

		EmpresaGrupo empresaGrupo = new EmpresaGrupo();
		empresaGrupo.setEmpresa(empresaSelecionada);
		empresaGrupo.setGrupo(grupoSelecionado);
		empresaGrupo.setUsuario(usuarioEdicao);

		this.usuarioEdicao.getEmpresasGrupos().add(empresaGrupo);

		this.onEmpresasUsuario();
	}

	public void selecionaEmpresasPossiveis() {
		this.onEmpresasUsuario();
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Usuario obj = (Usuario) event.getObject();
		this.setUsuarioEdicao(obj);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("CadastroUsuario.xhtml?usuario=" + obj.getId());
	}
}