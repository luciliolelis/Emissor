package com.amsoft.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.repository.LogAcessos;
import com.amsoft.erp.util.jpa.Transactional;

public class LogAcesssoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LogAcessos logAcessos;
	
	@Transactional
	public LogAcesso salvar(LogAcesso logAcesso ) {
		return logAcessos.guardar(logAcesso);
	}
	
	
	
	
	

}
