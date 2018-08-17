package com.amsoft.erp.controller.grupos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

import com.amsoft.erp.model.GrupoPermissao;

@Named
@ApplicationScoped
public class GrupoPermissoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GrupoPermissao> todasPermissoes = new ArrayList<GrupoPermissao>();

	public  List<GrupoPermissao> getTodaspermissoes() {
		
		 GrupoPermissao clientePesquisar = new GrupoPermissao();
		 clientePesquisar.setDescricao("Pesquisar clientes");
		 clientePesquisar.setNome("CLIENTE_PESQUISAR");
		 clientePesquisar.setId(new Long(1));
		
		 GrupoPermissao clienteIncluir = new GrupoPermissao();
		 clienteIncluir.setDescricao("Incluir novos clientes");
		 clienteIncluir.setNome("CLIENTE_INCLUIR");
		 clienteIncluir.setId(new Long(2));
		 
		 
		 GrupoPermissao clienteEditar = new GrupoPermissao();
		 clienteEditar.setDescricao("Editar clientes");
		 clienteEditar.setNome("CLIENTE_EDITAR");
		 clienteEditar.setId(new Long(3));
		 
		 GrupoPermissao clienteExcluir = new GrupoPermissao();
		 clienteExcluir.setDescricao("Excluir clientes");
		 clienteExcluir.setNome("CLIENTE_EXCLUIR");
		 clienteExcluir.setId(new Long(4));
		 
		 todasPermissoes.add(clientePesquisar);
		 todasPermissoes.add(clienteIncluir);
		 todasPermissoes.add(clienteEditar);
		 todasPermissoes.add(clienteExcluir);
		
		
		return todasPermissoes;
	}
	

	     
}