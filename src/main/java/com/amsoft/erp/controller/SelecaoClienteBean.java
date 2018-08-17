package com.amsoft.erp.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.repository.Clientes;

@Named
@ViewScoped
public class SelecaoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;
	
	private String nome;
	
	private List<Cliente> clientesFiltrados;
	
	public void pesquisar() {
		clientesFiltrados = clientes.porNome(nome);
	}

	@SuppressWarnings("deprecation")
	public void selecionar(Cliente cliente) {
		RequestContext.getCurrentInstance().closeDialog(cliente);
	}
	
	@SuppressWarnings("deprecation")
	public void abrirDialogo() {
		Map<String, Object> opcoes = new HashMap<>();
		opcoes.put("modal", true);
		opcoes.put("resizable", true);
		opcoes.put("contentHeight", 400);
		
		RequestContext.getCurrentInstance().openDialog("/dialogos/SelecaoCliente", opcoes, null);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

}