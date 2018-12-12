package com.amsoft.erp.controller.nfe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import com.amsoft.erp.cacerts.NFeBuildAllCacerts;
import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.Ibpt;
import com.amsoft.erp.model.UnidadesFederativas;
import com.amsoft.erp.model.Veiculo;
import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.nfe.Cfop;
import com.amsoft.erp.model.nfe.EnderecoEntrega;
import com.amsoft.erp.model.nfe.EnderecoTransportador;
import com.amsoft.erp.model.nfe.FinalidadeOperacao;
import com.amsoft.erp.model.nfe.FormaPagamento;
import com.amsoft.erp.model.nfe.ItemComplemento;
import com.amsoft.erp.model.nfe.ItemDuplicata;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.TipoDocumento;
import com.amsoft.erp.model.nfe.TipoNota;
import com.amsoft.erp.model.nfe.VeiculoEntrega;
import com.amsoft.erp.model.produto.ItemIcmsUf;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.Cfops;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.repository.filter.NfeFilter;
import com.amsoft.erp.repository.filter.ProdutoFilter;
import com.amsoft.erp.repository.produtos.Produtos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfe.CadastroNFeService;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroNFeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Produces
	@NFeEdicao
	private Nfe nfe;

	@Inject
	private Empresas empresas;
	

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private Estados estados;
	private List<CepEstado> listaEstados = new ArrayList<>();

	public List<CepEstado> getListaEstados() {
		listaEstados = estados.todos();
		return listaEstados;
	}

	public void setListaEstados(List<CepEstado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public void testes() {
		System.out.println(nfe.getFinalidadeOperacao().getDescricao());

		if (FinalidadeOperacao.DEVOLUCAO.equals(nfe.getFinalidadeOperacao())) {
			nfe.setTipoDocumento(TipoDocumento.ENTRADA);
		}

	}

	@Inject
	private Produtos produtos;

	@Inject
	private Nfes nfes;

	@Inject
	private NFeBuildAllCacerts allCacerts;

	@Inject
	private Clientes clientes;

	private String codigoProduto;

	@Inject
	private Cfops cfops;

	@SuppressWarnings("unused")
	private String descricaoCfop;
	private String codigoCfop;
	private String descricaoPesquisa;

	private List<Cfop> cfopFiltrados;
	private List<Cliente> clientesFiltrados;
	private List<Produto> produtosFiltrados;
	private List<Cliente> transportadoresFiltrados;
	private List<ItemDuplicata> listaDuplicatas = new ArrayList<ItemDuplicata>();
	private List<ItemComplemento> listaNotaComplementar = new ArrayList<ItemComplemento>();
	private List<Endereco> todosEnderecos = new ArrayList<Endereco>();
	private List<Endereco> todosEnderecosTransportador = new ArrayList<Endereco>();
	private List<Veiculo> todosVeiculos = new ArrayList<Veiculo>();
	private List<Ibpt> todosIbpt = new ArrayList<Ibpt>();
	private List<Nfe> filtrados;

	private NfeFilter filtro;
	private ClienteFilter filtroCliente;
	private ProdutoFilter filtroProduto;

	private Endereco enderecoSelecionado = new Endereco();
	private Endereco enderecoTransportadorSelecionado = new Endereco();
	private Veiculo veiculoSelecionado = new Veiculo();
	private Produto produtoLinhaEditavel;
	private ItemProduto produtoSelecionado;

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			if (this.isNfeNova()) {

				this.incremetarNumeroNfe();
				this.nfe.setEmpresa(usuarioLogado.getUsuario().getEmpresa());

				Empresa empresa = this.empresas.porId(usuarioLogado.getUsuario().getEmpresa().getId());
				this.nfe.setDadosComplementares(empresa.getDadosadicionais());

			}

			this.carregarCliente();
			this.carregarTransportador();

			this.nfe.adicionarItemVazio();

			if (this.isAprazo(nfe)) {
				this.carregarDuplicatas();
			}
		}
	}

	static void info(String metodo, String valor) {
		System.out.println("INFO | " + metodo + " | " + valor);
	}

	static void error(String metodo, String valor) {
		System.err.println("ERRO | " + metodo + " | " + valor);
	}

	private void carregarCliente() {
		if (this.isClienteValido()) {
			this.todosEnderecos = this.nfe.getCliente().getEnderecos();

			if (this.isEnderecoEntregaValido()) {
				this.carregarEnderecoCliente();
			}
		}
	}

	private void carregarEnderecoCliente() {

		this.enderecoSelecionado.setCep(this.nfe.getEnderecoEntrega().getCep());
		this.enderecoSelecionado.setIbgeCidade(this.nfe.getEnderecoEntrega().getIbgeCidade());
		this.enderecoSelecionado.setCidade(this.nfe.getEnderecoEntrega().getCidade());
		this.enderecoSelecionado.setLogradouro(this.nfe.getEnderecoEntrega().getLogradouro());

		if (this.isNumeroEndereco()) {
			this.enderecoSelecionado.setNumero(this.nfe.getEnderecoEntrega().getNumero());
		}

		this.enderecoSelecionado.setUf(this.nfe.getEnderecoEntrega().getUf());

		this.enderecoSelecionado.setBairro(this.nfe.getEnderecoEntrega().getBairro());

		if (this.isComplementoEndereco()) {
			this.enderecoSelecionado.setComplemento(this.nfe.getEnderecoEntrega().getComplemento());
		}
	}

	public String getDescricaoPesquisa() {
		return descricaoPesquisa;
	}

	public void setDescricaoPesquisa(String descricaoPesquisa) {
		this.descricaoPesquisa = descricaoPesquisa;
	}

	public List<ItemComplemento> getListaNotaComplementar() {
		return listaNotaComplementar;
	}

	public void setListaNotaComplementar(List<ItemComplemento> listaNotaComplementar) {
		this.listaNotaComplementar = listaNotaComplementar;
	}

	public List<ItemDuplicata> getListaDuplicatas() {
		return listaDuplicatas;
	}

	public void setListaDuplicatas(List<ItemDuplicata> listaDuplicatas) {
		this.listaDuplicatas = listaDuplicatas;
	}

	private void carregarDuplicatas() {
		this.listaDuplicatas = this.nfes.perquisaDuplicatas(this.nfe);
	}

	public ItemProduto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(ItemProduto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	private boolean isNumeroEndereco() {
		return this.isNfeValida() && this.isEnderecoEntregaValido()
				&& this.nfe.getEnderecoEntrega().getNumero() != null;
	}

	private boolean isUfEndereco() {
		return this.isNfeValida() && this.isEnderecoEntregaValido() && this.nfe.getEnderecoEntrega().getUf() != null;
	}

	private boolean isComplementoEndereco() {
		return this.isNfeValida() && this.isEnderecoEntregaValido()
				&& this.nfe.getEnderecoEntrega().getComplemento() != null;
	}

	private boolean isEnderecoEntregaValido() {
		return this.isNfeValida() && this.nfe.getEnderecoEntrega() != null;
	}

	private boolean isClienteValido() {
		return this.isNfeValida() && this.nfe.getCliente() != null;
	}

	private boolean isNfeValida() {
		return this.nfe != null;
	}

	private boolean isNfeNova() {
		return this.isNfeValida() && this.nfe.getId() == null;
	}

	private boolean isPrimeiraNfeEmitente() {
		this.pesquisar();
		return this.filtrados.size() == 0;
	}

	public void pesquisar() {
		filtro.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		filtrados = this.nfes.pesquisa(filtro);
	}

	private void incremetarNumeroNfe() {
		if (this.isPrimeiraNfeEmitente()) {
			this.nfe.setNumero(1);
		} else {
			Integer novoNumero = (this.filtrados.get(0).getNumero() + 1);
			this.nfe.setNumero(novoNumero);
		}
	}

	private void carregarTransportador() {

		if (this.isTransportadorValido()) {
			this.carregaEnderecoTransportador();
			this.carregaVeiculoTransportador();
		}
	}

	private void carregaEnderecoTransportador() {
		this.todosEnderecosTransportador = this.nfe.getTransportador().getEnderecos();

		if (isEnderecoTransportadorValido()) {
			this.enderecoTransportadorSelecionado.setCep(this.nfe.getEnderecoTransportador().getCepTransportador());

			// this.enderecoTransportadorSelecionado.setCidade(this.nfe.getEnderecoTransportador().getCidadeTransportador());

			// this.enderecoTransportadorSelecionado.setUf(this.nfe.getEnderecoTransportador().getUfTransportador());
			this.enderecoTransportadorSelecionado
					.setLogradouro(this.nfe.getEnderecoTransportador().getLogradouroTransportador());
			this.enderecoTransportadorSelecionado
					.setNumero(this.nfe.getEnderecoTransportador().getNumeroTransportador());
			this.enderecoTransportadorSelecionado
					.setBairro(this.nfe.getEnderecoTransportador().getBairroTransportador());
			this.enderecoTransportadorSelecionado
					.setComplemento(this.nfe.getEnderecoTransportador().getComplementoTransportador());
		}
	}

	private void carregaVeiculoTransportador() {
		this.todosVeiculos = this.nfe.getTransportador().getVeiculos();

		if (this.isVeiculoValido()) {
			this.veiculoSelecionado.setPlaca(this.nfe.getVeiculoEntrega().getPlaca());
			this.veiculoSelecionado.setCidade(this.nfe.getVeiculoEntrega().getCidade_veiculo());

			this.veiculoSelecionado.setUf(this.nfe.getVeiculoEntrega().getUf_veiculo());

			this.veiculoSelecionado.setUf(this.nfe.getVeiculoEntrega().getUf_veiculo());
			this.veiculoSelecionado.setMarca(this.nfe.getVeiculoEntrega().getMarca());
			this.veiculoSelecionado.setRenavam(this.nfe.getVeiculoEntrega().getRenavam());

			if (this.isRntrc())
				this.veiculoSelecionado.setRntrc(this.nfe.getVeiculoEntrega().getRntrc());

			if (this.isCiot())
				this.veiculoSelecionado.setCiot(this.nfe.getVeiculoEntrega().getCiot());

			if (this.isAntt())
				this.veiculoSelecionado.setAntt(this.nfe.getVeiculoEntrega().getAntt());

		}
	}

	public CadastroNFeBean() {
		limpar();
	}

	public void notaComplementar() {
		ItemComplemento notaComplemetar = new ItemComplemento();

		notaComplemetar.setCodigo("0");
		notaComplemetar.setDescricao(this.getDescricaoNotaComplementar(this.getNfe()));
		notaComplemetar.setNcm("0000000");
		notaComplemetar.setCst("000");
		notaComplemetar.setCfop(this.getNfe().getNfe().getCfop().getCodigo());
		notaComplemetar.setUnidade("UND");
		notaComplemetar.setQuantidade(BigDecimal.ZERO);
		notaComplemetar.setValor(BigDecimal.ZERO);
		notaComplemetar.setDesconto(BigDecimal.ZERO);
		notaComplemetar.setTotal(BigDecimal.ZERO);

		notaComplemetar.setAliquotaIcms(this.getNfe().getAliquotaIcmsComp());
		notaComplemetar.setAliquotaIcmsSt(this.getNfe().getAliquotaIcmsStComp());
		notaComplemetar.setAliquotaIpi(this.getNfe().getAliquotaIpiComp());

		notaComplemetar.setBcIcms(this.getNfe().getBcIcmsComp());
		notaComplemetar.setBcIcmsSt(this.getNfe().getBcIcmsStComp());
		notaComplemetar.setBcIpi(this.getNfe().getBcIpiComp());

		notaComplemetar.setIcms(this.getNfe().getIcmsCompelentar());
		notaComplemetar.setIcmsSt(this.getNfe().getIcmsStCompelentar());
		notaComplemetar.setIpi(this.getNfe().getIpiCompelentar());

		notaComplemetar.setNfe(this.getNfe());

		this.listaNotaComplementar.removeAll(this.listaNotaComplementar);
		this.listaNotaComplementar.add(notaComplemetar);

		this.getNfe().getItensComplemento().removeAll(this.getNfe().getItensComplemento());

		this.getNfe().setItensComplemento(this.listaNotaComplementar);

		this.getNfe().setValorBaseIcms(this.getNfe().getBcIcmsComp());
		this.getNfe().setValorIcms(this.getNfe().getIcmsCompelentar());

		this.getNfe().setValorBaseIcmsSt(this.getNfe().getAliquotaIcmsStComp());
		this.getNfe().setValorIcmsSt(this.getNfe().getIcmsStCompelentar());

		this.getNfe().setValorIpi(this.getNfe().getIpiCompelentar());

		this.getNfe().setValorTotal(BigDecimal.ZERO);
		this.getNfe().setValorFrete(BigDecimal.ZERO);
		this.getNfe().setValorSeguro(BigDecimal.ZERO);
		this.getNfe().setValorDespesas(BigDecimal.ZERO);
		this.getNfe().setValorDesconto(BigDecimal.ZERO);
		this.getNfe().setValorTotalProdutos(BigDecimal.ZERO);
	}

	public boolean isEditando() {
		return this.nfe.getId() != null;
	}

	public boolean isExportacao() {

		try {
			return this.isClienteValido() && (this.getNfe().getCliente().getEstrangeiro().equals(true)
					|| this.getNfe().getCliente().getExterior().equals(true));
		} catch (Exception e) {
			erro(e.getMessage());
		}

		return false;
	}

	public boolean isNotExportacao() {
		return !this.isExportacao();
	}

	String getDescricaoNotaComplementar(Nfe nfe) {

		String imposto = "";

		if (isMaiorQZero(nfe.getIcmsCompelentar())) {
			imposto = "ICMS";
		}

		if (isMaiorQZero(nfe.getIcmsStCompelentar())) {
			if (imposto.equals("")) {
				imposto = "ICMS ST";
			} else {
				imposto = imposto + " / ICMS ST";
			}
		}

		if (isMaiorQZero(nfe.getIpiCompelentar())) {
			if (imposto.equals("")) {
				imposto = "IPI";
			} else {
				imposto = imposto + " / IPI";
			}
		}

		return "Complemento de " + imposto;
	}

	private boolean isRntrc() {
		return this.isVeiculoValido() && this.nfe.getVeiculoEntrega().getRntrc() != null;
	}

	private boolean isCiot() {
		return this.isVeiculoValido() && this.nfe.getVeiculoEntrega().getCiot() != null;
	}

	private boolean isAntt() {
		return this.isVeiculoValido() && this.nfe.getVeiculoEntrega().getAntt() != null;
	}

	private boolean isTransportadorValido() {
		return this.isNfeValida() && this.nfe.getTransportador() != null;
	}

	private boolean isEnderecoTransportadorValido() {
		return this.isNfeValida() && this.isTransportadorValido() && this.nfe.getEnderecoTransportador() != null;
	}

	private boolean isVeiculoValido() {
		return this.isNfeValida() && this.nfe.getVeiculoEntrega() != null;
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.codigoProduto)) {
			this.produtoLinhaEditavel = this.produtos.porSku(this.codigoProduto);
			this.carregarProdutoLinhaEditavel();
		}
	}

	public void produtosSelecionar(Produto produto) {
		info("Produto selecionado: " + produto.getNome());

		if (produto != null) {
			this.produtoLinhaEditavel = produto;
			this.carregarProdutoLinhaEditavel();
		}
	}

	private boolean isSimplesNacional(Nfe nfe) {
		return nfe.getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	@SuppressWarnings("deprecation")
	public void carregarProdutoLinhaEditavel() {

		try {
			ItemProduto item = this.nfe.getItensProdutos().get(0);

			// int index = this.getIndex();

			ItemIcmsUf icms = this.getConfiguracaoIcmsDestino(produtoLinhaEditavel);

			// if (icms == null) {
			// FacesUtil.addErrorMessage("Este produdo não possui configurações
			// de ICMS
			// para o estado do "
			// + this.getNfe().getEnderecoEntrega().getUf().getEstado());
			//
			// RequestContext.getCurrentInstance().update(Arrays.asList("frm:msg"));
			// }

			if (this.produtoLinhaEditavel != null) {
				if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
					FacesUtil.addErrorMessage("Já existe um item na Nf-e com o produto informado.");
					RequestContext.getCurrentInstance().update(Arrays.asList("frm:msg"));
				} else if (icms == null && isNotConsumidorFinal() && isNotExportacao()) {
					FacesUtil.addErrorMessage("Este produdo não possui configurações de ICMS para o estado do cliente");
					RequestContext.getCurrentInstance().update(Arrays.asList("frm:msg"));
				} else if (this.isConsumidorFinal() && this.isNotCstConcumidorFinal()) {
					FacesUtil.addErrorMessage("Este produdo não possui configurações de ICMS para concumidor final");
					RequestContext.getCurrentInstance().update(Arrays.asList("frm:msg"));
				} else {

					String infoComplementar = "";

					if (this.produtoLinhaEditavel.getComplementos() != null
							&& !this.produtoLinhaEditavel.getComplementos().equals("")) {
						if ((this.getNfe().getDadosComplementares() != null)
								&& (!this.getNfe().getDadosComplementares().equals(""))) {
							infoComplementar = this.getNfe().getDadosComplementares() + " "
									+ this.produtoLinhaEditavel.getComplementos();
						} else {
							infoComplementar = this.produtoLinhaEditavel.getComplementos();
						}

						if (!infoComplementar.equals("") && !infoComplementar.equals(" ")) {
							this.getNfe().setDadosComplementares(infoComplementar);
						}

					}

					item.setProduto(this.produtoLinhaEditavel);
					item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

					if (this.nfe.getCfop() != null)
						item.setCfop(this.nfe.getCfop().getCodigo());
					else
						item.setCfop(null);

					item.setAliquotaPis(produtoLinhaEditavel.getAliquotaPis());
					item.setCstPis(this.produtoLinhaEditavel.getCstpiscofins().getCodigo());
					item.setAliquotaCofins(this.produtoLinhaEditavel.getAliquotaCofins());
					item.setCstCofins(this.produtoLinhaEditavel.getCstpiscofins().getCodigo());
					item.setCstIpi(this.produtoLinhaEditavel.getCstipi().getCodigo());
					item.setAliquotaIpi(this.produtoLinhaEditavel.getAliquotaIpi());
					item.setNcm(this.produtoLinhaEditavel.getNcm());
					item.setSomarIpiBcIcms(this.produtoLinhaEditavel.getSomarIpiBcIcms());
					item.setSomarIpiBcIcmsSt(this.produtoLinhaEditavel.getSomarIpiBcIcmsSt());

					if (this.isUfEndereco())
						item.setUf(this.nfe.getEnderecoEntrega().getUf());

					if (this.isConsumidorFinal()) {

						if (this.isSimplesNacional(nfe))
							item.setCsosn(this.produtoLinhaEditavel.getCsosn().getCodigo());
						else
							item.setCstIcms(this.produtoLinhaEditavel.getCsticms().getCodigo());

						item.setAliquotaIcms(this.produtoLinhaEditavel.getAliquotaIcms());
						item.setAliquotaIcmsSt(this.produtoLinhaEditavel.getAliquotaIcmsSt());
						item.setMva(this.produtoLinhaEditavel.getMva());
						item.setPauta(this.produtoLinhaEditavel.getPauta());
						item.setReducaoBaseCalculoIcms(this.produtoLinhaEditavel.getReducaoBaseCalculoIcms());
						item.setReducaoBaseCalculoIcmsSt(this.produtoLinhaEditavel.getReducaoBaseCalculoIcmsSt());
					} else if (this.isExportacao()) {

						if (this.isSimplesNacional(nfe))
							item.setCsosn(this.produtoLinhaEditavel.getCsosnExterior().getCodigo());
						else
							item.setCstIcms(this.produtoLinhaEditavel.getCsticmsExterior().getCodigo());
					} else {
						// ItemIcmsUf icms =
						// this.getProdutoLinhaEditavel().getItensIcmsUf().get(index);

						if (this.isSimplesNacional(nfe))
							item.setCsosn(icms.getCsosn().getCodigo());
						else
							item.setCstIcms(icms.getCstIcms().getCodigo());

						item.setAliquotaIcms(icms.getAliquotaIcms());
						item.setAliquotaIcmsSt(icms.getAliquotaIcmsSt());
						item.setMva(icms.getMva());
						item.setPauta(icms.getValorPauta());
						item.setReducaoBaseCalculoIcms(icms.getReducaoIcms());
						item.setReducaoBaseCalculoIcmsSt(icms.getReducaoIcmsSt());
					}

					this.nfe.adicionarItemVazio();
					this.produtoLinhaEditavel = null;
					this.codigoProduto = null;

					this.recalcularNota();
				}
			}
		} catch (Exception e) {
			erro(e.getCause() + " - " + e.getMessage());
		}
	}

	int getIndex() {
		int index = -1;

		if (this.isExportacao())
			return index;

		for (ItemIcmsUf itemIcs : this.produtoLinhaEditavel.getItensIcmsUf()) {
			index++;
			if (this.getNfe().getEnderecoEntrega().getUf().equals(itemIcs.getUf())) {
				return index;
			}
		}
		return index;
	}

	private ItemIcmsUf getConfiguracaoIcmsDestino(Produto produto) {

		ItemIcmsUf icms = null;

		try {
			for (ItemIcmsUf itemIcs : produto.getItensIcmsUf()) {
				if (this.getNfe().getEnderecoEntrega().getUf().equals(itemIcs.getUf().getUf())) {
					return itemIcs;
				}
			}

		} catch (Exception e) {

		}

		return icms;
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;

		for (ItemProduto item : this.getNfe().getItensProdutos()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	private boolean isConsumidorFinal() {
		return this.getNfe().isVendaConsumidorFinal();
	}

	private boolean isCstConcumidorFinal() {
		if (isSimplesNacional(nfe))
			return this.produtoLinhaEditavel.getCsosn() != null;
		else
			return this.produtoLinhaEditavel.getCsticms() != null;
	}

	private boolean isNotCstConcumidorFinal() {
		return !isCstConcumidorFinal();
	}

	private boolean isNotConsumidorFinal() {
		return !this.isConsumidorFinal();
	}

	public void adicionaParcela() {
		this.listaDuplicatas = this.nfe.adicionarItemParcela();
	}

	public void selecionaEndereco() {
		System.out.println(this.enderecoSelecionado.getLogradouro());

		this.nfe.setEnderecoEntrega(new EnderecoEntrega());
		this.nfe.getEnderecoEntrega().setUf(this.enderecoSelecionado.getUf());
		this.nfe.getEnderecoEntrega().setIbgeCidade(this.enderecoSelecionado.getIbgeCidade());
		this.nfe.getEnderecoEntrega().setCidade(this.enderecoSelecionado.getCidade());
		this.nfe.getEnderecoEntrega().setBairro(this.enderecoSelecionado.getBairro());
		this.nfe.getEnderecoEntrega().setComplemento(this.enderecoSelecionado.getComplemento());
		this.nfe.getEnderecoEntrega().setCep(this.enderecoSelecionado.getCep());
		this.nfe.getEnderecoEntrega().setLogradouro(this.enderecoSelecionado.getLogradouro());
		this.nfe.getEnderecoEntrega().setNumero(this.enderecoSelecionado.getNumero());
	}

	public void selecionaEnderecoTransportador() {

		this.nfe.setEnderecoTransportador(new EnderecoTransportador());
		if (this.enderecoTransportadorSelecionado != null && this.enderecoTransportadorSelecionado.getCep() != null) {
			System.out.println(this.enderecoTransportadorSelecionado.getCidade());

			this.nfe.getEnderecoTransportador().setUfTransportador(this.enderecoTransportadorSelecionado.getUf());

			this.nfe.getEnderecoTransportador()
					.setCidadeTransportador(this.enderecoTransportadorSelecionado.getCidade());
			this.nfe.getEnderecoTransportador()
					.setBairroTransportador(this.enderecoTransportadorSelecionado.getBairro());
			this.nfe.getEnderecoTransportador()
					.setComplementoTransportador(this.enderecoTransportadorSelecionado.getComplemento());
			this.nfe.getEnderecoTransportador().setCepTransportador(this.enderecoTransportadorSelecionado.getCep());
			this.nfe.getEnderecoTransportador()
					.setLogradouroTransportador(this.enderecoTransportadorSelecionado.getLogradouro());
			this.nfe.getEnderecoTransportador()
					.setNumeroTransportador(this.enderecoTransportadorSelecionado.getNumero());

		}

	}

	public void selecionaVeiculo() {

		this.nfe.setVeiculoEntrega(new VeiculoEntrega());
		if (this.veiculoSelecionado != null && this.veiculoSelecionado.getPlaca() != null) {
			System.out.println(this.veiculoSelecionado.getPlaca());

			this.nfe.getVeiculoEntrega().setPlaca(this.veiculoSelecionado.getPlaca());
			this.nfe.getVeiculoEntrega().setCidade_veiculo(this.veiculoSelecionado.getCidade());
			this.nfe.getVeiculoEntrega().setUf_veiculo(this.veiculoSelecionado.getUf());
			this.nfe.getVeiculoEntrega().setMarca(this.veiculoSelecionado.getMarca());
			this.nfe.getVeiculoEntrega().setRenavam(this.veiculoSelecionado.getRenavam());
			this.nfe.getVeiculoEntrega().setAntt(this.veiculoSelecionado.getAntt());
			this.nfe.getVeiculoEntrega().setRntrc(this.veiculoSelecionado.getRntrc());
			this.nfe.getVeiculoEntrega().setCiot(this.veiculoSelecionado.getCiot());

		}

	}

	private void limpar() {
		this.nfe = new Nfe();
		this.listaDuplicatas = new ArrayList<ItemDuplicata>();
		filtro = new NfeFilter();
		descricaoPesquisa = new String();
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);
	}

	public List<Nfe> completarChave(String chave) {
		return this.nfes.porChave(chave, usuarioLogado.getUsuario().getEmpresa());
	}

	public List<Cliente> completarTransportador(String nome) {
		return this.clientes.transportadorPorNome(nome);
	}

	public Nfe getNfe() {
		return nfe;
	}

	public void setNfe(Nfe nfe) {
		this.nfe = nfe;
	}

	public TipoDocumento[] getTipoDocumento() {
		return TipoDocumento.values();
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public FinalidadeOperacao[] getFinalidadeOperacaos() {
		return FinalidadeOperacao.values();
	}

	public TipoNota[] gettTipoNotas() {
		return TipoNota.values();
	}

	public UnidadesFederativas[] getUnidadesFederativas() {
		return UnidadesFederativas.values();
	}

	public void cfopSelecionado(SelectEvent event) {
		try {
			this.nfe.setCfop((Cfop) event.getObject());
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - " + e.getCause());
		}
	}

	private void carregarVeiculoTransportador(Cliente transportador) {

		try {
			this.todosVeiculos = transportador.getVeiculos();
			this.veiculoSelecionado = new Veiculo();

			if (!this.todosVeiculos.isEmpty()) {
				Veiculo veiculo = this.todosVeiculos.get(0);
				this.veiculoSelecionado.setPlaca(veiculo.getPlaca());
				this.veiculoSelecionado.setCidade(veiculo.getCidade());
				this.veiculoSelecionado.setUf(veiculo.getUf());
				this.veiculoSelecionado.setMarca(veiculo.getMarca());
				this.veiculoSelecionado.setRenavam(veiculo.getRenavam());
				this.veiculoSelecionado.setRntrc(veiculo.getRntrc());
				this.veiculoSelecionado.setAntt(veiculo.getAntt());
				this.veiculoSelecionado.setCiot(veiculo.getCiot());
			}

		} catch (Exception e) {
			erro("Erro ao carregar veiculo do transportador " + e.getMessage() + e.getCause());
		}

	}

	private void carregarEnderecoTransportador(Cliente transportador) {

		try {

			this.todosEnderecosTransportador = transportador.getEnderecos();
			this.enderecoTransportadorSelecionado = new Endereco();

			if (!this.todosEnderecosTransportador.isEmpty()) {
				Endereco endereco = this.todosEnderecosTransportador.get(0);
				this.enderecoTransportadorSelecionado.setCep(endereco.getCep());
				this.enderecoTransportadorSelecionado.setCidade(endereco.getCidade());
				this.enderecoTransportadorSelecionado.setLogradouro(endereco.getLogradouro());
				this.enderecoTransportadorSelecionado.setNumero(endereco.getNumero());
				this.enderecoTransportadorSelecionado.setUf(endereco.getUf());
				this.enderecoTransportadorSelecionado.setBairro(endereco.getBairro());
				this.enderecoTransportadorSelecionado.setComplemento(endereco.getComplemento());
			}

		} catch (Exception e) {
			erro("Erro ao carregar endereço do transportador " + e.getMessage() + e.getCause());
		}
	}

	public void transportadorSelecionado(SelectEvent event) {
		try {
			Cliente cliente = (Cliente) event.getObject();
			this.nfe.setTransportador(cliente);
			this.carregarVeiculoTransportador(cliente);
			this.carregarEnderecoTransportador(cliente);
			selecionaEnderecoTransportador();
			selecionaVeiculo();
			System.out.println("Trasnportador selecionado: " + cliente.getNome());
		} catch (Exception e) {
			System.out.println("Erro ao selecionar transportador: " + e.getCause() + " - " + e.getMessage());
		}
	}

	public void limparTransportador() {
		this.nfe.setTransportador(null);
		this.veiculoSelecionado = new Veiculo();
		this.enderecoTransportadorSelecionado = new Endereco();
		this.todosVeiculos.removeAll(todosVeiculos);
		this.todosEnderecosTransportador.removeAll(todosEnderecosTransportador);
	}

	private void loadEnderecoSelecionado(Cliente cliente) {
		try {
			this.todosEnderecos = cliente.getEnderecos();
			this.enderecoSelecionado.setCep(this.todosEnderecos.get(0).getCep());
			this.enderecoSelecionado.setIbgeCidade(this.todosEnderecos.get(0).getIbgeCidade());
			this.enderecoSelecionado.setCidade(this.todosEnderecos.get(0).getCidade());
			this.enderecoSelecionado.setLogradouro(this.todosEnderecos.get(0).getLogradouro());
			this.enderecoSelecionado.setNumero(this.todosEnderecos.get(0).getNumero());
			this.enderecoSelecionado.setUf(this.todosEnderecos.get(0).getUf());
			this.enderecoSelecionado.setBairro(this.todosEnderecos.get(0).getBairro());
			this.enderecoSelecionado.setComplemento(this.todosEnderecos.get(0).getComplemento());
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	private void loadEnderecoEntrega(Endereco endereco) {
		try {
			this.nfe.setEnderecoEntrega(new EnderecoEntrega());
			this.nfe.getEnderecoEntrega().setUf(endereco.getUf());
			this.nfe.getEnderecoEntrega().setIbgeCidade(endereco.getIbgeCidade());
			this.nfe.getEnderecoEntrega().setCidade(endereco.getCidade());
			this.nfe.getEnderecoEntrega().setBairro(endereco.getBairro());
			this.nfe.getEnderecoEntrega().setComplemento(endereco.getComplemento());
			this.nfe.getEnderecoEntrega().setCep(endereco.getCep());
			this.nfe.getEnderecoEntrega().setLogradouro(endereco.getLogradouro());
			this.nfe.getEnderecoEntrega().setNumero(endereco.getNumero());
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	private void loadEnderecoEntregaComplementar(EnderecoEntrega endereco) {
		try {
			this.nfe.setEnderecoEntrega(new EnderecoEntrega());
			this.nfe.getEnderecoEntrega().setUf(endereco.getUf());
			this.nfe.getEnderecoEntrega().setCidade(endereco.getCidade());
			this.nfe.getEnderecoEntrega().setBairro(endereco.getBairro());
			this.nfe.getEnderecoEntrega().setComplemento(endereco.getComplemento());
			this.nfe.getEnderecoEntrega().setCep(endereco.getCep());
			this.nfe.getEnderecoEntrega().setLogradouro(endereco.getLogradouro());
			this.nfe.getEnderecoEntrega().setNumero(endereco.getNumero());
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	private void loadEnderecoEntregaEstrangeiro(Cliente cliente) {
		try {
			this.nfe.setEnderecoEntrega(new EnderecoEntrega());

			// this.nfe.getEnderecoEntrega().setUf(cliente.getUfExterior());
			// this.nfe.getEnderecoEntrega().setCidade(cliente.getCidadeExterior());
			this.nfe.getEnderecoEntrega().setBairro(cliente.getBairroExterior());
			this.nfe.getEnderecoEntrega().setLogradouro(cliente.getLogradouroExterior());
			this.nfe.getEnderecoEntrega().setNumero(cliente.getNumeroExterior());
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	private boolean isEstrangeiro(Cliente cliente) {
		return this.isClienteValido() && cliente.getEstrangeiro().equals(true);
	}

	public void clienteSelecionado(SelectEvent event) {

		try {

			Cliente cliente = (Cliente) event.getObject();
			this.nfe.setCliente(cliente);

			if (cliente.getTipoPessoa().equals(TipoPessoa.FISICA)) {
				this.nfe.setVendaConsumidorFinal(true);
			}

			if (this.isEstrangeiro(cliente)) {
				this.loadEnderecoEntregaEstrangeiro(cliente);
			} else {
				this.loadEnderecoSelecionado(cliente);
				this.loadEnderecoEntrega(this.enderecoSelecionado);
			}

			info("Cliente selecionado: " + cliente.getNome());
		} catch (Exception e) {
			erro(e.getMessage() + " " + e.getCause());
			this.nfe.setCliente(null);
			this.todosEnderecos.removeAll(todosEnderecos);
			this.enderecoSelecionado = new Endereco();
		}
	}

	public void setaPrimeiraAba(SelectEvent event) {

	}

	public void chaveSelecionado(SelectEvent event) {

		try {

			Nfe nfe = (Nfe) event.getObject();

			this.getNfe().setNfe(nfe);

			if (this.isNotaComplementar()) {
				this.getNfe().setCfop(nfe.getCfop());
			}

			this.getNfe().setCliente(nfe.getCliente());
			this.getNfe().setVendaConsumidorFinal(nfe.isVendaConsumidorFinal());

			if (this.isEstrangeiro(nfe.getCliente())) {
				this.loadEnderecoEntregaEstrangeiro(nfe.getCliente());
			} else {
				this.loadEnderecoSelecionado(nfe.getCliente());
				this.loadEnderecoEntregaComplementar(nfe.getEnderecoEntrega());
			}

			this.getNfe().setModalidadeFrete(nfe.getModalidadeFrete());
			this.getNfe().setTransportador(nfe.getTransportador());
			this.carregarEnderecoTransportador(nfe.getTransportador());
			this.carregarVeiculoTransportador(nfe.getTransportador());
			this.getNfe().setFormaPagamento(FormaPagamento.OUTROS);

			if (!this.isNotaComplementar()) {
				this.nfe.removerItemVazio();

				for (ItemProduto item : nfe.getItensProdutos()) {
					if (this.isProdutoValido(item)) {
						item.setId(null);
						item.setCfop(null);
						item.setNfe(this.getNfe());
						this.getNfe().getItensProdutos().add(item);
					}
				}

				this.nfe.adicionarItemVazio();
			}

			this.recalcularNota();
			info("Cliente selecionado: " + nfe.getChave());
		} catch (Exception e) {
			erro(e.getMessage() + " " + e.getCause());
			this.getNfe().setNfe(null);
		}
	}

	String getInfNotaComplementar(Nfe nfe) {

		String imposto = "";

		if (isMaiorQZero(nfe.getIcmsCompelentar())) {
			imposto = "ICMS";
		}

		if (isMaiorQZero(nfe.getIcmsStCompelentar())) {
			if (imposto.equals("")) {
				imposto = "ICMS ST";
			} else {
				imposto = imposto + " / ICMS ST";
			}
		}

		if (isMaiorQZero(nfe.getIpiCompelentar())) {
			if (imposto.equals("")) {
				imposto = "IPI";
			} else {
				imposto = imposto + " / IPI";
			}
		}

		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");

		String dadosComplementares = "Nota Fiscal Complementar emitida para correcao de erro ocorrido na emissao da Nota Fiscal Eletronica n : "
				+ nfe.getNfe().getNumero() + ", Chave de Acesso da NF-e: " + nfe.getNfe().getChave() + ", emitida em "
				+ dt.format(nfe.getNfe().getEmissao()) + " onde o " + imposto + " foi destacado incorreto.";

		return dadosComplementares;
	}

	boolean isNotaComplementar() {
		return this.getNfe().getFinalidadeOperacao() == FinalidadeOperacao.COMPLEMENTA;

	}

	static boolean isMaiorQZero(BigDecimal valor) {
		return (valor.compareTo(BigDecimal.ZERO) > 0);
	}

	static void info(String texto) {
		System.out.println("Info | " + texto);
	}

	static void erro(String texto) {
		System.err.println("Erro | " + texto);
	}

	public boolean isExisteProduto() {
		return this.getNfe().getItensProdutos().size() > 1;
	}

	public boolean isExisteCleinte() {
		return this.isNfeValida() && this.getNfe().getCliente() != null;
	}

	public void prepararNovoCadastro() {
		this.nfe = new Nfe();
		this.nfe.adicionarItemVazio();

		this.pesquisar();

		if (this.filtrados.size() == 0) {
			this.nfe.setNumero(1);
		} else {
			Integer novoNumero = (this.filtrados.get(0).getNumero() + 1);
			this.nfe.setNumero(novoNumero);
		}
	}

	private boolean isAprazo(Nfe nfe) {
		return nfe.getFormaPagamento() != null && nfe.getFormaPagamento() == FormaPagamento.DUPLICATA_MERCANTIL;
	}

	public void nfeAlterada(@Observes NFeAlteradaEvent event) {
		this.nfe = event.getNFe();
	}

	public void salvar() {

		this.nfe.removerItemVazio();

		try {

			if (!isAprazo(this.nfe)) {
				this.nfe.setQuantidadeParcelas(0);
				this.listaDuplicatas.removeAll(this.listaDuplicatas);
			}

			this.nfe.setItensDuplicatas(this.listaDuplicatas);

			this.nfe = nfeServise.salvar(nfe);

			FacesUtil.addInfoMessage("Nf-e salva com sucesso!");

		} catch (Exception e) {
			throw new NegocioException(e.getMessage());
		} finally {
			this.nfe.adicionarItemVazio();
		}
	}

	@NotBlank
	public String getCodigoCfop() {
		return (this.nfe.getCfop() == null) ? null : this.nfe.getCfop().getCodigo().toString();
	}

	public void setCodigoCfop(String codigoCfop) {
		this.codigoCfop = codigoCfop;
	}

	@NotBlank
	public String getDescricaoCfop() {
		return (this.nfe.getCfop() == null) ? "" : this.nfe.getCfop().getDescricao();
	}

	public void setDescricaoCfop(String descricaoCfop) {
		this.descricaoCfop = descricaoCfop;
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Nfe obj = (Nfe) event.getObject();
		this.setNfe(obj);
		FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroNfe.xhtml?nfe=" + obj.getId());
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome, this.usuarioLogado.getUsuario().getEmpresa());
	}

	public void recalcularNota() {

		if (this.nfe != null) {

			// this.nfe.recalcularValorTotalDesconto();
			// this.nfe.recalcularValorTotal();
			//
			// this.nfe.rateiaDespesas();
			// this.nfe.rateiaSeguro();
			// this.nfe.rateiaFrete();
			//
			// this.nfe.ajustaDespesas();
			// this.nfe.ajustaSeguro();
			// this.nfe.ajustaFrete();
			//
			// this.nfe.recalcularTotalIpi();
			// this.nfe.recalcularTotalBaseIcms();
			// this.nfe.recalcularTotalBaseIcmsSt();
			// this.nfe.recalcularTotalIcms();
			// this.nfe.recalcularTotalIcmsSt();
			//
			// this.nfe.recalcularTotalPis();
			// this.nfe.recalcularTotalCofins();
			// this.calcularImpostoLeiTransparencia();

			FacesUtil.addInfoMessage("Valores atualizados!");

		}
	}

	private boolean isProdutoValido(ItemProduto item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	public void atualizarQuantidade(ItemProduto item, int linha) {
		if (isMenorQueUm(item.getQuantidade())) {
			if (linha == 0 || item.getQuantidade() == null) {
				item.setQuantidade(BigDecimal.ONE);
			} else {
				this.getNfe().getItensProdutos().remove(linha);

				if (this.isNenhumItem()) {
					this.limparCamposCustosDesconto();
				}
			}
		}

		this.recalcularNota();
	}

	static boolean isMenorQueUm(BigDecimal valor) {
		return valor.compareTo(BigDecimal.ZERO) < 1;
	}

	public void removerItem(ItemProduto item, int linha) {

		item.setQuantidade(BigDecimal.ZERO);

		if (isMenorQueUm(item.getQuantidade())) {
			if (linha == 0) {
				item.setQuantidade(BigDecimal.ONE);
			} else {
				this.getNfe().getItensProdutos().remove(linha);

				if (this.isNenhumItem()) {
					this.limparCamposCustosDesconto();
				}
			}
		}

		this.recalcularNota();
	}

	private void limparCamposCustosDesconto() {
		this.getNfe().setValorSeguro(BigDecimal.ZERO);
		this.getNfe().setValorFrete(BigDecimal.ZERO);
		this.getNfe().setValorDespesas(BigDecimal.ZERO);
		this.getNfe().setValorDesconto(BigDecimal.ZERO);
	}

	private boolean isNenhumItem() {
		return this.getNfe().getItensProdutos().size() == 1;
	}

	public void atualizarValorProduto(ItemProduto item, int linha) {

		if (linha == 0 || item.getValorUnitario() == null) {
			item.setValorUnitario(BigDecimal.ZERO);
		}

		this.recalcularNota();
	}

	public void atualizarValorDesconto(ItemProduto item, int linha) {

		if (linha == 0 || item.getValorDesconto() == null) {
			item.setValorDesconto(BigDecimal.ZERO);
		}

		this.recalcularNota();
	}

	public void atualizarValorFrete(ItemProduto item, int linha) {

		if (linha == 0 || item.getValorFrete() == null) {
			item.setValorFrete(BigDecimal.ZERO);
		}

		this.recalcularNota();
	}

	public void pesquisarCfop() {

		try {
			Integer codigo = new Integer(codigoCfop);
			Cfop cfop = this.cfops.porCodigo(codigo);

			if (cfop != null) {
				if (this.nfe.getTipoDocumento() == TipoDocumento.SAIDA) {
					if ((cfop.getCodigo() > 4999) && (cfop.getCodigo() < 8000)) {

						this.nfe.setCfop(cfop);
					} else {
						cfop = null;
						FacesUtil.addErrorMessage("Cfop para notas de saída deve iniciar em 5, 6 ou 7");
					}
				} else {
					if (cfop.getCodigo() < 4000) {

						this.nfe.setCfop(cfop);
					} else {
						cfop = null;
						FacesUtil.addErrorMessage("Cfop para nota de entrada deve iniciar em 1, 2 ou 3");
					}
				}
			}

		} catch (Exception e) {
			this.nfe.setCfop(null);
			FacesUtil.addErrorMessage("CFOP inválido");
		}

	}

	public void pesquisarCfopProduto(ItemProduto item, int linha) {
		if (item.getCfop() != null) {
			try {
				Integer cfop = this.cfops.porCodigo(item.getCfop()).getCodigo();

				if (cfop == null) {
					item.setCfop(null);
				} else {
					if (this.nfe.getTipoDocumento() == TipoDocumento.SAIDA) {
						if ((cfop > 4999) && (cfop < 8000)) {
							item.setCfop(cfop);
						} else {
							item.setCfop(null);
							FacesUtil.addErrorMessage("Cfop para notas de saída deve iniciar em 5, 6 ou 7");
						}
					} else {
						if (cfop < 4000) {
							item.setCfop(cfop);
						} else {
							item.setCfop(null);
							FacesUtil.addErrorMessage("Cfop para nota de entrada deve iniciar em 1, 2 ou 3");
						}
					}
				}

			} catch (Exception e) {
				item.setCfop(null);
				FacesUtil.addErrorMessage("CFOP inválido");
			}
		}
	}

	public void atualizarTotalParcelas() {
		this.nfe.getValorTotalParcelas();
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

	public Endereco getEnderecoTransportadorSelecionado() {
		return enderecoTransportadorSelecionado;
	}

	public void setEnderecoTransportadorSelecionado(Endereco enderecoTransportadorSelecionado) {
		this.enderecoTransportadorSelecionado = enderecoTransportadorSelecionado;
	}

	public List<Endereco> getTodosEnderecosTransportador() {
		return todosEnderecosTransportador;
	}

	public void setTodosEnderecosTransportador(List<Endereco> todosEnderecosTransportador) {
		this.todosEnderecosTransportador = todosEnderecosTransportador;
	}

	public Veiculo getVeiculoSelecionado() {
		return veiculoSelecionado;
	}

	public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
		this.veiculoSelecionado = veiculoSelecionado;
	}

	public List<Veiculo> getTodosVeiculos() {
		return todosVeiculos;
	}

	public void setTodosVeiculos(List<Veiculo> todosVeiculos) {
		this.todosVeiculos = todosVeiculos;
	}

	public List<Ibpt> getTodosIbpt() {
		return todosIbpt;
	}

	public List<Nfe> getFiltrados() {
		return filtrados;
	}

	public void setFiltrados(List<Nfe> filtrados) {
		this.filtrados = filtrados;
	}

	public NfeFilter getFiltro() {
		return filtro;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		this.nfe.setEmissao((Date) event.getObject());
		facesContext.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Data selecionada", format.format(event.getObject())));
	}

	public List<Cfop> getCfopFiltrados() {
		return cfopFiltrados;
	}

	public void cfopPesquisar() {
		cfopFiltrados = cfops.porTipo(descricaoPesquisa, this.nfe);
	}

	public void setCfopFiltrados(List<Cfop> cfopFiltrados) {
		this.cfopFiltrados = cfopFiltrados;
	}

	public void cfopSelecionar(Cfop cfop) {
		this.nfe.setCfop(cfop);
	}

	public void clienteSelecionar(Cliente cliente) {
		this.nfe.setCliente(cliente);

		if (this.isEstrangeiro(cliente)) {
			this.loadEnderecoEntregaEstrangeiro(cliente);
		} else {
			this.loadEnderecoSelecionado(cliente);
			this.loadEnderecoEntrega(this.enderecoSelecionado);
		}

		if (cliente.getTipoPessoa().equals(TipoPessoa.FISICA)) {
			this.nfe.setVendaConsumidorFinal(true);
		}

		info("Cliente selecionado: " + cliente.getNome());

	}

	public void transportadorSelecionar(Cliente cliente) {
		this.nfe.setTransportador(cliente);

		this.carregarVeiculoTransportador(cliente);
		this.carregarEnderecoTransportador(cliente);
		selecionaEnderecoTransportador();
		selecionaVeiculo();

		info("Transportador selecionado: " + cliente.getNome());
	}

	public void limpaCfop() {

		info(this.nfe.getTipoDocumento().getDescricao());

		this.nfe.setCfop(null);

		for (ItemProduto item : this.nfe.getItensProdutos()) {
			item.setCfop(null);
		}

		if (nfe.getTipoDocumento() == TipoDocumento.SAIDA) {
			this.nfe.setTipoDocumento(TipoDocumento.ENTRADA);
		} else {
			this.nfe.setTipoDocumento(TipoDocumento.SAIDA);
		}

		info(nfe.getTipoDocumento().getDescricao());
	}

	public void novaPesquisa() {
		cfopPesquisar();
	}

	public void gerarCacerts() {
		try {
			this.allCacerts.gerarCacerts();
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	public void atualizarComplementos() {
		try {
			this.notaComplementar();

			if (this.isNotaComplementar()) {
				this.getNfe().setDadosComplementares(this.getInfNotaComplementar(nfe));
			}
		} catch (Exception e) {
			erro(e.getStackTrace().toString());
		}
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public List<Cliente> getTransportadoresFiltrados() {
		return transportadoresFiltrados;
	}

	public ClienteFilter getFiltroCliente() {
		return filtroCliente;
	}

	public ProdutoFilter getFiltroProduto() {
		return filtroProduto;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public void pesquisaClientes() {
		filtroCliente = new ClienteFilter();
		this.clientesFiltrados = this.clientes.pesquisaCliente(filtroCliente);
	}

	public void pesquisaTransportadores() {
		filtroCliente = new ClienteFilter();
		this.transportadoresFiltrados = this.clientes.pesquisaTransportador(filtroCliente);
	}

	public void pesquisaProdutos() {
		filtroProduto = new ProdutoFilter();
		filtroProduto.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		this.produtosFiltrados = this.produtos.pesquisaPorEmpresa(filtroProduto);
	}

	public void exportarNfe() throws IOException {
		escritorXml(nfe.getXml());
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
			String filename = this.nfe.getChave() + ".xml";
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

			FacesUtil.addInfoMessage("Download do xml realizado com sucesso!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isProducao() {
		return this.nfe.getEmpresa().getWsAmbiente() == 1;
	}

	public void atualizaImpostos() {

		for (ItemProduto item : this.getNfe().getItensProdutos()) {
			if (isProdutoValido(item))

				if (item.getProduto().getId() == null) {

				} else {

					AmsoftUtils.info(item.getProduto().getNome());

					this.nfe.setCliente(this.nfe.getCliente());

					if (this.isEstrangeiro(this.nfe.getCliente())) {
						this.loadEnderecoEntregaEstrangeiro(this.nfe.getCliente());
					} else {
						this.loadEnderecoSelecionado(this.nfe.getCliente());
						this.loadEnderecoEntrega(this.enderecoSelecionado);
					}

					Produto produto = produtos.porId(item.getProduto().getId());

					ItemIcmsUf icms = this.getConfiguracaoIcmsDestino(produto);

					item.setProduto(produto);

					item.setValorUnitario(produto.getValorUnitario());

					if (this.nfe.getCfop() != null)
						item.setCfop(this.nfe.getCfop().getCodigo());
					else
						item.setCfop(null);

					item.setAliquotaPis(produto.getAliquotaPis());
					item.setCstPis(produto.getCstpiscofins().getCodigo());
					item.setAliquotaCofins(produto.getAliquotaCofins());
					item.setCstCofins(produto.getCstpiscofins().getCodigo());
					item.setCstIpi(produto.getCstipi().getCodigo());
					item.setAliquotaIpi(produto.getAliquotaIpi());
					item.setNcm(produto.getNcm());
					item.setSomarIpiBcIcms(produto.getSomarIpiBcIcms());
					item.setSomarIpiBcIcmsSt(produto.getSomarIpiBcIcmsSt());

					if (this.isUfEndereco())
						item.setUf(this.nfe.getEnderecoEntrega().getUf());

					if (this.isConsumidorFinal()) {

						if (this.isSimplesNacional(nfe)) {

							// validar a existencia de csosn para cf
							if (produto.getCsosn() != null) {
								item.setCsosn(produto.getCsosn().getCodigo());
							} else {
								FacesUtil.addErrorMessageDetail(
										"Não exixte configuração de icms para consumidor para este produso", "");
								return;
							}

						} else {

							// validar a existencia de csosn para cf
							if (produto.getCsticms() != null) {
								item.setCstIcms(produto.getCsticms().getCodigo());
							} else {
								FacesUtil.addErrorMessageDetail(
										"Não exixte configuração de icms para consumidor para este produso", "");
								return;
							}

						}

						item.setAliquotaIcms(produto.getAliquotaIcms());
						item.setAliquotaIcmsSt(produto.getAliquotaIcmsSt());
						item.setMva(produto.getMva());
						item.setPauta(produto.getPauta());
						item.setReducaoBaseCalculoIcms(produto.getReducaoBaseCalculoIcms());
						item.setReducaoBaseCalculoIcmsSt(produto.getReducaoBaseCalculoIcmsSt());
					} else if (this.isExportacao()) {

						if (this.isSimplesNacional(nfe))
							item.setCsosn(produto.getCsosnExterior().getCodigo());
						else
							item.setCstIcms(produto.getCsticmsExterior().getCodigo());
					} else {
						// ItemIcmsUf icms =
						// this.getProdutoLinhaEditavel().getItensIcmsUf().get(index);

						if (this.isSimplesNacional(nfe))
							item.setCsosn(icms.getCsosn().getCodigo());
						else
							item.setCstIcms(icms.getCstIcms().getCodigo());

						item.setAliquotaIcms(icms.getAliquotaIcms());
						item.setAliquotaIcmsSt(icms.getAliquotaIcmsSt());
						item.setMva(icms.getMva());
						item.setPauta(icms.getValorPauta());
						item.setReducaoBaseCalculoIcms(icms.getReducaoIcms());
						item.setReducaoBaseCalculoIcmsSt(icms.getReducaoIcmsSt());
					}

					produto = null;
					this.codigoProduto = null;

					this.recalcularNota();

				}

		}
	}
}
