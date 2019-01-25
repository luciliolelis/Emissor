package com.amsoft.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroEmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Empresas empresas;
	
	@Transactional
	public Empresa salvar(Empresa empresa) {
		return empresas.guardar(empresa);
	}
	
	@Transactional
	public void excluir(Empresa empresa) {
		empresas.remover(empresa);
	}

}