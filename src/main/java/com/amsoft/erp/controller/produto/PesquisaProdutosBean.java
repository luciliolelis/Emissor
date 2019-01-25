package com.amsoft.erp.controller.produto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.amsoft.erp.model.StatusProduto;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.filter.ProdutoFilter;
import com.amsoft.erp.repository.produtos.Produtos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.CadastroProdutoService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos produtos;
	
	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private ProdutoFilter filtro;
	private List<Produto> produtosFiltrados;
	private List<Produto> filtrados;

	private Produto produtoSelecionado;

	private LazyDataModel<Produto> model;
	
	public LazyDataModel<Produto> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Produto> model) {
		this.model = model;
	}

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public PesquisaProdutosBean() {
		filtro = new ProdutoFilter();
		
		
		model = new LazyDataModel<Produto>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Produto> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));

				setRowCount(produtos.quantidadeFiltrados(filtro));

				return produtos.filtrados(filtro);
			}

		};
		
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	public void excluir(Produto produto) {

		produto.setStatus(StatusProduto.EXCUIDO);
		produto =  cadastroProdutoService.salvar(produto);

		FacesUtil.addInfoMessage("Produto " + produto.getSku() + " excluído com sucesso.");

		pesquisar();
	}

	public void pesquisar() {
		filtro.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		filtrados = produtos.pesquisa(filtro);
	}

	public List<Produto> getFiltrados() {
		return filtrados;
	}

	public void setFiltrados(List<Produto> filtrados) {
		this.filtrados = filtrados;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

}