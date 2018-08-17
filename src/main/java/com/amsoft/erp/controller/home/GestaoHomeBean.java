package com.amsoft.erp.controller.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.repository.LogAcessos;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@SessionScoped
public class GestaoHomeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<LogAcesso> acessos = new ArrayList<>();

	public List<LogAcesso> getAcessos() {
		return acessos;
	}

	@Inject
	private LogAcessos logAcessos;

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			System.out.println("Inicializa home");
		}
	}

	public void consultarLogAcesso() {
		acessos = logAcessos.filtrados("");
	}

}