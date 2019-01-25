package com.amsoft.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.produtos.Produtos;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos produtos;
	
	@Transactional
	public Produto salvar(Produto produto) {
		Produto produtoExistente = produtos.porCodigo(produto.getSku(), produto.getEmpresa());
		
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			throw new NegocioException("Já existe um produto com o código informado.");
		}
		
		return produtos.guardar(produto);
	}
	
}
