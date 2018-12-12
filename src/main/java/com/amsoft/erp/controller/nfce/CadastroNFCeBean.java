package com.amsoft.erp.controller.nfce;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.Ibpt;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.nfce.FormaPagamentoNFCe;
import com.amsoft.erp.model.nfce.ItemDuplicataNFCe;
import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Cfop;
import com.amsoft.erp.model.nfe.EnderecoEntrega;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.Cfops;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.Ibpts;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.repository.filter.NfceFilter;
import com.amsoft.erp.repository.filter.ProdutoFilter;
import com.amsoft.erp.repository.produtos.Produtos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfce.CadastroNFCeService;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String bookName;

	@Produces
	@NFCeEdicao
	private NFCe nfce;

	@Inject
	private Ibpts ibpts;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private CadastroNFCeService nfceServise;

	@Inject
	private NFCes nfces;

	private List<NFCe> filtrados;

	private NfceFilter filtro;

	private ClienteFilter filtroCliente;
	private List<Cliente> clientesFiltrados;

	private List<Produto> produtosFiltrados;

	private Produto produtoLinhaEditavel;
	private FormaPagamentoNFCe formaPagamentoLinhaEditavel;

	@Inject
	private Produtos produtos;

	private String codigoProduto;

	@Inject
	private Cfops cfops;

	@SuppressWarnings("unused")
	private String codigoCfop;

	@SuppressWarnings("unused")
	private String descricaoCfop;
	private List<Cfop> cfopFiltrados;
	private String descricaoPesquisa;

	private ItemProdutoNFCe produtoSelecionado;
	private ProdutoFilter filtroProduto;

	private Endereco enderecoSelecionado = new Endereco();
	private List<Endereco> todosEnderecos = new ArrayList<Endereco>();
	private List<ItemDuplicataNFCe> listaDuplicatas = new ArrayList<ItemDuplicataNFCe>();

	@Inject
	private Clientes clientes;

	public NFCe getNfce() {
		return nfce;
	}

	public void setNfce(NFCe nfce) {
		this.nfce = nfce;
	}

	public void inicializar() {

		if (FacesUtil.isNotPostback()) {
			
			if (this.isnfceNova()) {
				this.incremetarNumeronfce();
				this.nfce.setEmpresa(usuarioLogado.getUsuario().getEmpresa());
				this.nfce.setCfop(usuarioLogado.getUsuario().getEmpresa().getCfop_nfce());
				this.nfce.setCliente(usuarioLogado.getUsuario().getEmpresa().getCliente());
				this.loadEnderecoSelecionado(this.nfce.getCliente());
				this.loadEnderecoEntrega(this.enderecoSelecionado);
			}

			this.carregarCliente();
			this.nfce.adicionarItemVazio();
			this.nfce.adicionarItemVazioFormaPagamento();
		}
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome, this.usuarioLogado.getUsuario().getEmpresa());
	}

	public void prepararNovoCadastro() {
		this.nfce = new NFCe();
		this.nfce.adicionarItemVazio();
		this.nfce.adicionarItemVazioFormaPagamento();
		this.pesquisar();

		if (this.filtrados.size() == 0) {
			this.nfce.setNumero(1);
		} else {
			Integer novoNumero = (this.filtrados.get(0).getNumero() + 1);
			this.nfce.setNumero(novoNumero);
		}
	}

	private boolean isPrimeiraNfeEmitente() {
		this.pesquisar();
		return this.filtrados.size() == 0;
	}

	public void pesquisar() {
		filtro.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		filtrados = this.nfces.pesquisa(filtro);
	}

	public CadastroNFCeBean() {
		limpar();
	}

	private void limpar() {
		this.nfce = new NFCe();
		filtro = new NfceFilter();
		descricaoPesquisa = new String();
		this.codigoCfop = "";
	}

	private void incremetarNumeronfce() {
		if (this.isPrimeiraNfeEmitente()) {
			this.nfce.setNumero(1);
		} else {
			Integer novoNumero = (this.filtrados.get(0).getNumero() + 1);
			this.nfce.setNumero(novoNumero);
		}
	}

	private boolean isnfceNova() {
		return this.isnfceValida() && this.nfce.getId() == null;
	}

	private boolean isnfceValida() {

		return this.nfce != null;
	}

	static void info(String metodo, String valor) {
		System.out.println("INFO | " + metodo + " | " + valor);
	}

	static void error(String metodo, String valor) {
		System.err.println("ERRO | " + metodo + " | " + valor);
	}

	public void salvar() {

		this.nfce.removerItemVazio();
		this.nfce.removerItemVazioFormaPagamento();

		try {

			this.nfce.setEmissao(new Date());
			this.nfce = nfceServise.salvar(nfce);

			FacesUtil.addInfoMessage("NFC-e salva com sucesso!");
		} catch (Exception e) {
			throw new NegocioException(e.getMessage());
		} finally {
			this.nfce.adicionarItemVazio();
			this.nfce.adicionarItemVazioFormaPagamento();
		}
	}

	public void removerItem(ItemProdutoNFCe item, int linha) {

		item.setQuantidade(BigDecimal.ZERO);

		if (isMenorQueUm(item.getQuantidade())) {
			if (linha == 0) {
				item.setQuantidade(BigDecimal.ONE);
			} else {
				this.getNfce().getItensProdutos().remove(linha);

				if (this.isNenhumItem()) {
					this.limparCamposCustosDesconto();
				}
			}
		}

		this.recalcularNota();
	}

	public void pesquisaClientes() {
		filtroCliente = new ClienteFilter();
		this.clientesFiltrados = this.clientes.pesquisaCliente(filtroCliente);
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);
	}

	public void clienteSelecionado(SelectEvent event) {

		try {

			Cliente cliente = (Cliente) event.getObject();
			this.nfce.setCliente(cliente);

			if (this.isEstrangeiro(cliente)) {
				this.loadEnderecoEntregaEstrangeiro(cliente);
			} else {
				this.loadEnderecoSelecionado(cliente);
				this.loadEnderecoEntrega(this.enderecoSelecionado);
			}

			info("clienteSelecionado", "Cliente selecionado: " + cliente.getNome());
		} catch (Exception e) {
			error("clienteSelecionado", e.getMessage() + " " + e.getCause());
			this.nfce.setCliente(null);
			this.todosEnderecos.removeAll(todosEnderecos);
			this.enderecoSelecionado = new Endereco();
		}
	}

	private void loadEnderecoEntregaEstrangeiro(Cliente cliente) {
		try {
			this.nfce.setEnderecoEntrega(new EnderecoEntrega());

			// this.nfe.getEnderecoEntrega().setUf(cliente.getUfExterior());
			// this.nfe.getEnderecoEntrega().setCidade(cliente.getCidadeExterior());
			this.nfce.getEnderecoEntrega().setBairro(cliente.getBairroExterior());
			this.nfce.getEnderecoEntrega().setLogradouro(cliente.getLogradouroExterior());
			this.nfce.getEnderecoEntrega().setNumero(cliente.getNumeroExterior());
		} catch (Exception e) {
			error("loadEnderecoEntregaEstrangeiro", e.getStackTrace().toString());
		}
	}

	private boolean isEstrangeiro(Cliente cliente) {
		return this.isClienteValido() && cliente.getEstrangeiro().equals(true);
	}

	private void carregarCliente() {
		if (this.isClienteValido()) {
			this.todosEnderecos = this.nfce.getCliente().getEnderecos();

			if (this.isEnderecoEntregaValido()) {
				this.carregarEnderecoCliente();
			}
		}
	}

	private void loadEnderecoEntrega(Endereco endereco) {
		try {
			this.nfce.setEnderecoEntrega(new EnderecoEntrega());
			this.nfce.getEnderecoEntrega().setUf(endereco.getUf());
			this.nfce.getEnderecoEntrega().setCidade(endereco.getCidade());
			this.nfce.getEnderecoEntrega().setIbgeEstado(endereco.getIbgeEstado());
			this.nfce.getEnderecoEntrega().setIbgeCidade(endereco.getIbgeCidade());
			this.nfce.getEnderecoEntrega().setBairro(endereco.getBairro());
			this.nfce.getEnderecoEntrega().setComplemento(endereco.getComplemento());
			this.nfce.getEnderecoEntrega().setCep(endereco.getCep());
			this.nfce.getEnderecoEntrega().setLogradouro(endereco.getLogradouro());
			this.nfce.getEnderecoEntrega().setNumero(endereco.getNumero());
		} catch (Exception e) {
			error("loadEnderecoEntrega", e.getStackTrace().toString());
		}
	}

	private void loadEnderecoSelecionado(Cliente cliente) {
		try {
			this.todosEnderecos = cliente.getEnderecos();
			this.enderecoSelecionado.setCep(this.todosEnderecos.get(0).getCep());
			this.enderecoSelecionado.setCidade(this.todosEnderecos.get(0).getCidade());
			this.enderecoSelecionado.setIbgeCidade(this.todosEnderecos.get(0).getIbgeCidade());
			this.enderecoSelecionado.setIbgeEstado(this.todosEnderecos.get(0).getIbgeEstado());
			this.enderecoSelecionado.setLogradouro(this.todosEnderecos.get(0).getLogradouro());
			this.enderecoSelecionado.setNumero(this.todosEnderecos.get(0).getNumero());
			this.enderecoSelecionado.setUf(this.todosEnderecos.get(0).getUf());
			this.enderecoSelecionado.setBairro(this.todosEnderecos.get(0).getBairro());
			this.enderecoSelecionado.setComplemento(this.todosEnderecos.get(0).getComplemento());
		} catch (Exception e) {
			error("loadEnderecoSelecionado", e.getStackTrace().toString());
		}
	}

	public void selecionaEndereco() {
		System.out.println(this.enderecoSelecionado.getLogradouro());

		this.nfce.setEnderecoEntrega(new EnderecoEntrega());
		this.nfce.getEnderecoEntrega().setUf(this.enderecoSelecionado.getUf());
		this.nfce.getEnderecoEntrega().setCidade(this.enderecoSelecionado.getCidade());
		this.nfce.getEnderecoEntrega().setBairro(this.enderecoSelecionado.getBairro());
		this.nfce.getEnderecoEntrega().setComplemento(this.enderecoSelecionado.getComplemento());
		this.nfce.getEnderecoEntrega().setCep(this.enderecoSelecionado.getCep());
		this.nfce.getEnderecoEntrega().setLogradouro(this.enderecoSelecionado.getLogradouro());
		this.nfce.getEnderecoEntrega().setNumero(this.enderecoSelecionado.getNumero());
		this.nfce.getEnderecoEntrega().setIbgeCidade(this.enderecoSelecionado.getIbgeCidade());
		this.nfce.getEnderecoEntrega().setIbgeEstado(this.enderecoSelecionado.getIbgeEstado());
	}

	private boolean isClienteValido() {
		return this.isnfceValida() && this.nfce.getCliente() != null;
	}

	private void carregarEnderecoCliente() {

		this.enderecoSelecionado.setCep(this.nfce.getEnderecoEntrega().getCep());
		this.enderecoSelecionado.setCidade(this.nfce.getEnderecoEntrega().getCidade());
		this.enderecoSelecionado.setIbgeCidade(this.nfce.getEnderecoEntrega().getIbgeCidade());
		this.enderecoSelecionado.setLogradouro(this.nfce.getEnderecoEntrega().getLogradouro());

		if (this.isNumeroEndereco()) {
			this.enderecoSelecionado.setNumero(this.nfce.getEnderecoEntrega().getNumero());
		}

		this.enderecoSelecionado.setUf(this.nfce.getEnderecoEntrega().getUf());
		this.enderecoSelecionado.setIbgeEstado(this.nfce.getEnderecoEntrega().getIbgeEstado());
		this.enderecoSelecionado.setBairro(this.nfce.getEnderecoEntrega().getBairro());

		if (this.isComplementoEndereco()) {
			this.enderecoSelecionado.setComplemento(this.nfce.getEnderecoEntrega().getComplemento());
		}
	}

	private boolean isComplementoEndereco() {
		return this.isnfceValida() && this.isEnderecoEntregaValido()
				&& this.nfce.getEnderecoEntrega().getComplemento() != null;
	}

	private boolean isEnderecoEntregaValido() {
		return this.isnfceValida() && this.nfce.getEnderecoEntrega() != null;
	}

	private boolean isNumeroEndereco() {
		return this.isnfceValida() && this.isEnderecoEntregaValido()
				&& this.nfce.getEnderecoEntrega().getNumero() != null;
	}

	public void clienteSelecionar(Cliente cliente) {
		this.nfce.setCliente(cliente);

		if (this.isEstrangeiro(cliente)) {
			this.loadEnderecoEntregaEstrangeiro(cliente);
		} else {
			this.loadEnderecoSelecionado(cliente);
			this.loadEnderecoEntrega(this.enderecoSelecionado);
		}

		// info("clienteSelecionar", cliente.getNome());

	}

	public void limparCliente() {
		this.nfce.setCliente(null);

		this.nfce.setEnderecoEntrega(null);
		this.enderecoSelecionado = null;

		info("limparCliente", "Ok");

	}

	public void pesquisaProdutos() {
		filtroProduto = new ProdutoFilter();
		filtroProduto.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		this.produtosFiltrados = this.produtos.pesquisaPorEmpresa(filtroProduto);
	}

	public List<Cfop> getCfopFiltrados() {
		return cfopFiltrados;
	}

	public void cfopSelecionar(Cfop cfop) {
		this.nfce.setCfop(cfop);
	}

	public void novaPesquisa() {
		cfopPesquisar();
	}

	public void cfopPesquisar() {
		cfopFiltrados = cfops.todosConcumidorFinal();
	}

	public void setCfopFiltrados(List<Cfop> cfopFiltrados) {
		this.cfopFiltrados = cfopFiltrados;
	}

	@NotBlank
	public String getCodigoCfop() {
		return (this.nfce.getCfop() == null) ? null : this.nfce.getCfop().getCodigo().toString();
	}

	public void setCodigoCfop(String codigoCfop) {
		this.codigoCfop = codigoCfop;
	}

	@NotBlank
	public String getDescricaoCfop() {
		return (this.nfce.getCfop() == null) ? "" : this.nfce.getCfop().getDescricao();
	}

	public String getDescricaoPesquisa() {
		return descricaoPesquisa;
	}

	public void setDescricaoPesquisa(String descricaoPesquisa) {
		this.descricaoPesquisa = descricaoPesquisa;
	}

	public void setDescricaoCfop(String descricaoCfop) {
		this.descricaoCfop = descricaoCfop;
	}

	public List<NFCe> getFiltrados() {
		return filtrados;
	}

	public void setFiltrados(List<NFCe> filtrados) {
		this.filtrados = filtrados;
	}

	public NfceFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(NfceFilter filtro) {
		this.filtro = filtro;
	}

	public ClienteFilter getFiltroCliente() {
		return filtroCliente;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public List<Endereco> getTodosEnderecos() {
		return todosEnderecos;
	}

	public void setTodosEnderecos(List<Endereco> todosEnderecos) {
		this.todosEnderecos = todosEnderecos;
	}

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public ItemProdutoNFCe getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(ItemProdutoNFCe produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public ProdutoFilter getFiltroProduto() {
		return filtroProduto;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public void atualizarValorProduto(ItemProdutoNFCe item, int linha) {

		if (linha == 0 || item.getValorUnitario() == null) {
			item.setValorUnitario(BigDecimal.ZERO);
		}

		this.recalcularNota();

	}

	public void atualizarValorDesconto(ItemProdutoNFCe item, int linha) {

		if (linha == 0 || item.getValorDesconto() == null) {
			item.setValorDesconto(BigDecimal.ZERO);
		}

		this.recalcularNota();
	}

	static boolean isMenorQueUm(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) < 1;
	}

	private boolean isNenhumItem() {
		return this.getNfce().getItensProdutos().size() == 1;
	}

	public void atualizarQuantidade(ItemProdutoNFCe item, int linha) {
		if (isMenorQueUm(item.getQuantidade())) {
			if (linha == 0 || item.getQuantidade() == null) {
				item.setQuantidade(BigDecimal.ONE);
			} else {
				this.getNfce().getItensProdutos().remove(linha);

				if (this.isNenhumItem()) {
					this.limparCamposCustosDesconto();
				}
			}
		}

		this.recalcularNota();
	}

	private void limparCamposCustosDesconto() {
		this.getNfce().setValorSeguro(BigDecimal.ZERO);
		this.getNfce().setValorFrete(BigDecimal.ZERO);
		this.getNfce().setValorDespesas(BigDecimal.ZERO);
		this.getNfce().setValorDesconto(BigDecimal.ZERO);
	}

	public void atualizarValorFrete(ItemProdutoNFCe item, int linha) {

		if (linha == 0 || item.getValorFrete() == null) {
			item.setValorFrete(BigDecimal.ZERO);
		}

		this.recalcularNota();
	}

	public void recalcularNota() {
		if (this.nfce != null) {
			this.nfce.recalcularValorTotalDesconto();
			this.nfce.recalcularValorTotal();
			this.nfce.getValorTotalNota();

			this.calcularImpostoLeiTransparencia();

			FacesUtil.addInfoMessage("Valores atualizados!");
		}
	}

	private boolean isProdutoValido(ItemProdutoNFCe item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	private boolean isCalcularIBPT() {
		return (this.nfce.getCalcularConformeIBPT() != null) ? this.nfce.getCalcularConformeIBPT() : false;
	}

	private boolean isOrigemNacional(String origem) {
		return origem.equals("0") || origem.equals("3") || origem.equals("4") || origem.equals("5");
	}

	public void calcularImpostoLeiTransparencia() {

		BigDecimal total = BigDecimal.ZERO;

		if (isCalcularIBPT()) {
			for (ItemProdutoNFCe item : this.nfce.getItensProdutos()) {
				if (isProdutoValido(item)) {

					Ibpt ibpt = ibpts.porNcm(item.getProduto().getNcm());

					if (ibpt != null) {
						String origem = item.getProduto().getOrigemProduto().getCodigo();

						BigDecimal imposto = BigDecimal.ZERO;

						if (this.isOrigemNacional(origem)) {
							imposto = this.onCalcularImpostoNacional(item, ibpt);
						} else {
							imposto = this.onCalcularImpostoImportado(item, ibpt);
						}

						item.setValorTransparencia(imposto);

						total = total.add(imposto);
					}
				}
			}
		}

		// if (isNotBigDecimalZeroOuNullo(total)) {
		// String complementar = "Tributos aproximados R$" + total + " fonte
		// IBPT";
		// this.nfce.setDadosComplementares(complementar);
		// }

		this.nfce.setValorTransparencia(total);

	}

	static boolean isNotBigDecimalZeroOuNullo(BigDecimal num) {
		return num != null && isMaiorQZero(num);
	}

	static boolean isMaiorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) > 0);
	}

	private BigDecimal onCalcularImpostoNacional(ItemProdutoNFCe item, Ibpt ibpt) {

		BigDecimal impostos = ibpt.getNacionalfederal().add(ibpt.getEstadual().add(ibpt.getMunicipal()));

		return item.getValorTotal().multiply(impostos).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal onCalcularImpostoImportado(ItemProdutoNFCe item, Ibpt ibpt) {
		BigDecimal impostos = ibpt.getImportadosfederal().add(ibpt.getEstadual().add(ibpt.getMunicipal()));
		return item.getValorTotal().multiply(impostos).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.codigoProduto)) {
			this.produtoLinhaEditavel = this.produtos.porSku(this.codigoProduto);
			this.carregarProdutoLinhaEditavel();
		}
	}

	public void produtosSelecionar(Produto produto) {
		info("", "Produto selecionado: " + produto.getNome());

		if (produto != null) {
			this.produtoLinhaEditavel = produto;
			this.carregarProdutoLinhaEditavel();
		}
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;

		for (ItemProdutoNFCe item : this.getNfce().getItensProdutos()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	private boolean isSimplesNacional(NFCe nfce) {
		return nfce.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public void carregarProdutoLinhaEditavel() {

		try {
			ItemProdutoNFCe item = this.nfce.getItensProdutos().get(0);

			if (this.produtoLinhaEditavel != null) {
				if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
					FacesUtil.addErrorMessage("Já existe um item na NFC-e com o produto informado.");

				} else {

					item.setProduto(this.produtoLinhaEditavel);
					item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

					if (this.nfce.getCfop() != null)
						item.setCfop(this.nfce.getCfop().getCodigo());
					else
						item.setCfop(null);

					if (this.isSimplesNacional(nfce))
						item.setCsosn(this.produtoLinhaEditavel.getCsosn().getCodigo());
					else
						item.setCstIcms(this.produtoLinhaEditavel.getCsticms().getCodigo());

					item.setAliquotaPis(produtoLinhaEditavel.getAliquotaPis());
					item.setCstPis(this.produtoLinhaEditavel.getCstpiscofins().getCodigo());
					item.setAliquotaCofins(this.produtoLinhaEditavel.getAliquotaCofins());
					item.setCstCofins(this.produtoLinhaEditavel.getCstpiscofins().getCodigo());
					item.setCstIpi(this.produtoLinhaEditavel.getCstipi().getCodigo());
					item.setAliquotaIpi(this.produtoLinhaEditavel.getAliquotaIpi());
					item.setNcm(this.produtoLinhaEditavel.getNcm());
					item.setSomarIpiBcIcms(this.produtoLinhaEditavel.getSomarIpiBcIcms());
					item.setAliquotaIcmsSt(this.produtoLinhaEditavel.getAliquotaIcmsSt());
					item.setReducaoBaseCalculoIcms(this.produtoLinhaEditavel.getReducaoBaseCalculoIcms());
					item.setReducaoBaseCalculoIcmsSt(this.produtoLinhaEditavel.getReducaoBaseCalculoIcmsSt());
					
					item.setAliquotaIcms(this.produtoLinhaEditavel.getAliquotaIcms());
					item.setMva(this.produtoLinhaEditavel.getMva());
					item.setPauta(this.produtoLinhaEditavel.getPauta());

					this.nfce.adicionarItemVazio();
					this.produtoLinhaEditavel = null;
					this.codigoProduto = null;

					this.recalcularNota();
				}
			}
		} catch (Exception e) {
			error("", e.getCause() + " - " + e.getMessage());
		}
	}

	public void pesquisarCfopProduto(ItemProdutoNFCe item, int linha) {
		if (item.getCfop() != null) {
			try {
				Integer cfop = this.cfops.porCodigo(item.getCfop()).getCodigo();

				if (cfop != 5101 && cfop != 5102 && cfop != 5103 && cfop != 5104 && cfop != 5115 && cfop != 5405
						&& cfop != 5656 && cfop != 5667 && cfop != 5933) {
					FacesUtil.addErrorMessage("CFOP para NFC-e inválido");
				} else {
					if (cfop == null) {
						item.setCfop(null);
					} else {
						item.setCfop(cfop);
						FacesUtil.addInfoMessage("CFOP para NFC-e atualizado!");
					}
				}

			} catch (Exception e) {
				item.setCfop(null);
				FacesUtil.addErrorMessage("CFOP inválido");
			}
		}
	}

	public void exportarNfe() throws IOException {
		escritorXml(nfce.getXml());
	}

	public void escritorXml(String texto) {
		try {
			FileWriter fw = new FileWriter(getRaizProjeto() + "resources/xml/nfe.xml");
			BufferedWriter bw = new BufferedWriter(fw);
			texto = ajustaXmlParaDanfe(texto);
			bw.write(texto);
			bw.close();

			downloadDicionario();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public String getRaizProjeto() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	}

	private String ajustaXmlParaDanfe(String texto) {
		texto = texto.replaceAll("enviNFe", "nfeProc");
		texto = texto.replaceAll("<idLote>1</idLote><indSinc>0</indSinc>", "");

		return texto;
	}

	public void downloadDicionario() {
		try {
			// OutputStream out = null;
			String filename = this.nfce.getChave() + ".xml";
			File file = new File(getRaizProjeto() + "resources/xml/nfe.xml");

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			// out = response.getOutputStream();

			response.setContentType("text/xml");
			response.addHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentLength((int) file.length());

			FileInputStream fileInputStream = new FileInputStream(file);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}

			responseOutputStream.flush();
			responseOutputStream.close();

			fileInputStream.close();

			FacesUtil.addInfoMessage("Download do xml da NFC-e realizado com sucesso!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isEditando() {
		return this.nfce.getId() != null;
	}

	public boolean isProducao() {
		return this.nfce.getEmpresa().getWsAmbiente() == 1;
	}

	public FormaPagamentoNFCe[] getFormasPagamento() {
		return FormaPagamentoNFCe.values();
	}

	public List<ItemDuplicataNFCe> getListaDuplicatas() {
		return listaDuplicatas;
	}

	public void setListaDuplicatas(List<ItemDuplicataNFCe> listaDuplicatas) {
		this.listaDuplicatas = listaDuplicatas;
	}

	public void nfeAlterada(@Observes NFCeAlteradaEvent event) {
		this.nfce = event.getNFCe();
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	@SuppressWarnings("deprecation")
	public void showRatingDialog() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentWidth", 480);
		options.put("contentHeight", 180);
		options.put("includeViewParams", true);

		Map<String, List<String>> params = new HashMap<String, List<String>>();
		List<String> values = new ArrayList<String>();

		values.add(bookName);
		params.put("bookName", values);

		RequestContext.getCurrentInstance().openDialog("/nfce/bookRating", options, params);
	}

	public void onDialogReturn(SelectEvent event) {
		Object rating = event.getObject();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "You rated the book with " + rating, null);

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void carregarPagamentoLinhaEditavel() {

		try {
			ItemDuplicataNFCe item = this.nfce.getItensDuplicatas().get(0);

			if (this.formaPagamentoLinhaEditavel != null) {

				item.setFormaPagamento(formaPagamentoLinhaEditavel);

				if (this.nfce.getItensDuplicatas().size() == 1) {
					item.setValor(this.nfce.getValorTotalNota());
				}

				this.nfce.adicionarItemVazioFormaPagamento();
				this.formaPagamentoLinhaEditavel = null;

			}
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}
	}

	public FormaPagamentoNFCe getFormaPagamentoLinhaEditavel() {
		return formaPagamentoLinhaEditavel;
	}

	public void setFormaPagamentoLinhaEditavel(FormaPagamentoNFCe formaPagamentoLinhaEditavel) {
		this.formaPagamentoLinhaEditavel = formaPagamentoLinhaEditavel;
	}

}
