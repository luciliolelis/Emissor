package com.amsoft.erp.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amsoft.erp.controller.usuario.GestaoUsuariosBean;
import com.amsoft.erp.model.EmpresaGrupo;
import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@SessionScoped
public class LoginEmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private Usuarios usuarios;

	@Inject
	private Empresas empresas;

	@Inject
	private GestaoUsuariosBean gestaoUsuariosBean;

	@Inject
	@Any
	private Event<Usuario> eventLog;

	private Empresa empresa;

	private Usuario usuario = new Usuario();

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

	private List<Empresa> empresasUsuario;

	public List<Empresa> getEmpresasUsuario() {
		return empresasUsuario;
	}

	public void setEmpresasUsuario(List<Empresa> empresasUsuario) {
		this.empresasUsuario = empresasUsuario;
	}

	private List<Empresa> todasEmpresas;

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

	public void preRender() {
		todasEmpresas = empresas.todas();
		this.empresasUsuario = this.consultaEmpresasUsuario();
	}

	public void consultar() {
		this.usuario = usuarios.porEmail(usuarioLogado.getUsuario().getEmail());
	}

	@PostConstruct
	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	public void acessaEmpresa() {
		this.usuario.setEmpresaGrupo(this.retGrupoEmpresaByUsuario());
		this.usuario = this.gestaoUsuariosBean.salvar(usuario);
		this.usuarioLogado.getUsuario().setEmpresa(this.usuario.getEmpresa());
		this.usuarioLogado.getUsuario().setEmpresaGrupo(this.usuario.getEmpresaGrupo());
		System.out.println(this.usuario.getEmpresa().getRazao_social());
	}

	
	public void onGravaLogIn() {

		LogAcesso log = new LogAcesso();
		log.setData_hora(new Date());
		log.setEmail(this.usuario.getEmail());
		log.setNome(this.usuario.getNome());
		log.setNome_maquina(this.retNomeMaquinaCliente());
		log.setIp_cliente(request.getRemoteHost());
		log.setEmpresa(this.usuario.getEmpresa().getNome_fantasia());
		log.setAcao("LOGIN");
		this.gestaoUsuariosBean.salvarLogAcesso(log);

		System.out.println(usuarioLogado.getUsername() + "Login na empresa " + log.getEmpresa());
	}



	public String retNomeMaquinaCliente() {

		InetAddress address = null;

		try {
			address = InetAddress.getByName(request.getRemoteHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return address.getHostName();
	}

	public void onGravaLogOut() throws ServletException, IOException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_logout");
		dispatcher.forward(request, response);
		facesContext.responseComplete();
	}

	public void logout() throws ServletException, IOException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_logout");
		dispatcher.forward(request, response);
		facesContext.responseComplete();
	}

	public List<Empresa> consultaEmpresasUsuario() {
		List<Empresa> ret = new ArrayList<>();

		for (Empresa empresa : this.todasEmpresas) {
			for (EmpresaGrupo grupo : this.usuario.getEmpresasGrupos()) {
				if (grupo.getEmpresa().getId().equals(empresa.getId())) {
					ret.add(empresa);
				}
			}
		}
		return ret;
	}

	private EmpresaGrupo retGrupoEmpresaByUsuario() {
		EmpresaGrupo ret = null;

		for (EmpresaGrupo grupo : this.usuario.getEmpresasGrupos()) {
			if (grupo.getEmpresa().getId().equals(this.usuario.getEmpresa().getId())) {
				ret = grupo;
			}
		}

		return ret;
	}
}