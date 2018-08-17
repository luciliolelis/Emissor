package com.amsoft.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios usuarios;
	
	@Transactional
	public Usuario salvar(Usuario usuario ) {
		return usuarios.guardar(usuario);
	}
	
	@Transactional
	public void excluir(Usuario usuario) {
		usuarios.remover(usuario);
	}
	
	@Transactional
	public LogAcesso salvarLogAcesso(LogAcesso log ) {
		return usuarios.guardarLogAcesso(log);
	}

}
