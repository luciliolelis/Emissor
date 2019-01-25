package com.amsoft.erp.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.amsoft.erp.model.ncmcest.Ncm;
import com.amsoft.erp.repository.produtos.Ncms;

@Named
@ViewScoped
public class SelecaoNcmBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Ncms ncms;
	
	private String descricao;
	
	private List<Ncm> filtrados;
	
	public void pesquisar() {
		filtrados = ncms.porDescricao(descricao);
	}

	@SuppressWarnings("deprecation")
	public void selecionar(Ncm ncm) {
		RequestContext.getCurrentInstance().closeDialog(ncm);
	}
	
	@SuppressWarnings("deprecation")
	public void abrirDialogo() {
		Map<String, Object> opcoes = new HashMap<>();
		opcoes.put("modal", true);
		opcoes.put("resizable", true);
		opcoes.put("contentHeight", 400);
		
		RequestContext.getCurrentInstance().openDialog("/dialogos/SelecaoNcm", opcoes, null);
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Ncm> getFiltrados() {
		return filtrados;
	}

}