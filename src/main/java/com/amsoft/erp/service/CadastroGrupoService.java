package com.amsoft.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.Grupo;
import com.amsoft.erp.repository.Grupos;
import com.amsoft.erp.util.jpa.Transactional;


public class CadastroGrupoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Grupos grupos;

	@Transactional
	public Grupo salvar(Grupo grupo) {
		return this.grupos.guardar(grupo);
	}

	@Transactional
	public void excluir(Grupo grupo) {
		this.grupos.remover(grupo);

	}

}