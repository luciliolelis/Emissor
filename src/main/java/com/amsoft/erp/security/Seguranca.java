package com.amsoft.erp.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.amsoft.erp.model.Empresa;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;
	
	
	public String getNomeUsuario() {
		String nome = null;
		
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		
		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getNome();
			
		}
		
		return nome;
	}
	
	public Empresa getEmpresa() {
		Empresa empresa = null;
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		try {
			if (usuarioLogado != null) {
				if (usuarioLogado.getUsuario().getEmpresa() != null) {
					empresa = usuarioLogado.getUsuario().getEmpresa();
				}
			}
		} catch (Exception e) {
			System.out.println("ERRO: getEmpresa" );
		}
		return empresa;
	}
	
	public String getEmpresaLogada() {
		String nome = "";
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		try {
			if (usuarioLogado != null) {
				if (usuarioLogado.getUsuario().getEmpresa() != null) {
					nome = usuarioLogado.getUsuario().getEmpresa().getNome_fantasia();
				}
			}
		} catch (Exception e) {
			System.out.println("ERRO: getEmpresaLogada" );
		}
		return nome;
	}
	
	
	public String getRegimeTributario() {
		String ret = "";
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		try {
			if (usuarioLogado != null) {
				if (usuarioLogado.getUsuario().getEmpresa() != null) {
					if (usuarioLogado.getUsuario().getEmpresa().getRegimeTributario() != null) {
						ret = usuarioLogado.getUsuario().getEmpresa().getRegimeTributario().getDescricao();
					}
					
				}
			}
		} catch (Exception e) {
			System.out.println("ERRO: getRegimeTributario" );
		}
		return ret;
	}
	public String getGrupoUsuarioLogado() {
		String nome = "";
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		try {
			if (usuarioLogado != null) {
				if (usuarioLogado.getUsuario().getEmpresaGrupo() != null) {
					nome = usuarioLogado.getUsuario().getEmpresaGrupo().getGrupo().getNome();
				}
			}
		} catch (Exception e) {
			System.out.println("ERRO: getGrupoUsuarioLogado" );
		}
		return nome;
	}
	
	@Produces
	@UsuarioLogado
	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) 
				FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		
		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
		}
		
		return usuario;
	}
	
	
	public boolean isAdm() {
		return externalContext.isUserInRole("ADMINISTRADORES");
	}
	
	public boolean isEmitirPedidoPermitido() {
		return externalContext.isUserInRole("ADMINISTRADORES") 
				|| externalContext.isUserInRole("VENDEDORES");
	}
	
	public boolean isCancelarPedidoPermitido() {
		return externalContext.isUserInRole("ADMINISTRADORES") 
				|| externalContext.isUserInRole("VENDEDORES");
	}
	
	
	public boolean isExcluirCliente() {
		return externalContext.isUserInRole("CLIENTE_EXCLUIR");
	}
	
	public boolean isEditarCliente() {
		return externalContext.isUserInRole("CLIENTE_EDITAR");
	}
	
	public boolean isIncluirCliente() {
		return externalContext.isUserInRole("CLIENTE_INCLUIR");
	}
	
	
	public boolean isExcluirProduto() {
		return externalContext.isUserInRole("PRODUTO_EXCLUIR");
	}
	
	public boolean isEditarProduto() {
		return externalContext.isUserInRole("PRODUTO_EDITAR");
	}
	
	public boolean isIncluirProduto() {
		return externalContext.isUserInRole("PRODUTO_INCLUIR");
	}
	
	
	public boolean isExcluirEmpresa() {
		return externalContext.isUserInRole("EMPRESA_EXCLUIR");
	}
	
	public boolean isEditarEmpresa() {
		return externalContext.isUserInRole("EMPRESA_EDITAR");
	}
	
	public boolean isIncluirEmpresa() {
		return externalContext.isUserInRole("EMPRESA_INCLUIR");
	}
	
	public boolean isExcluirUsuario() {
		return externalContext.isUserInRole("USUARIO_EXCLUIR");
	}
	
	public boolean isEditarUsuario() {
		return externalContext.isUserInRole("USUARIO_EDITAR");
	}
	
	public boolean isIncluirUsuario() {
		return externalContext.isUserInRole("USUARIO_INCLUIR");
	}
}
