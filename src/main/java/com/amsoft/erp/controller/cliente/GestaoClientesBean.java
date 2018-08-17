package com.amsoft.erp.controller.cliente;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.Pais;
import com.amsoft.erp.model.StatusCliente;
import com.amsoft.erp.model.Veiculo;
import com.amsoft.erp.model.cep.CepCidade;
import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.enun.TipoCliente;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.enun.TributoCliente;
import com.amsoft.erp.model.vo.Webservicecep;
import com.amsoft.erp.repository.Cidades;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.repository.Marcas;
import com.amsoft.erp.repository.Paises;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.BuscaCNPJ;
import com.amsoft.erp.service.BuscaCep;
import com.amsoft.erp.service.CadastroClienteService;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jpa.FacesMessages;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GestaoClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;


	@Inject
	private CadastroClienteService cadastroCliente;

	@Inject
	private BuscaCNPJ cnpjService;

	@Inject
	private BuscaCep cepService;

	@Inject
	private Paises paises;

	@Inject
	private Cidades cidades;

	@Inject
	private Marcas marcas;

	@Inject
	private Estados estados;
	


	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private List<CepEstado> listaEstados = new ArrayList<>();
	private List<CepCidade> listaCidades = new ArrayList<>();

	@Inject
	private FacesMessages messages;

	private ClienteFilter filtro;

	private List<Cliente> todosClientes;

	private Cliente clienteEdicao = new Cliente();
	private Cliente clienteSelecionado;

	private Endereco enderecoSelecionado = new Endereco();
	private Veiculo veiculoSelecionado = new Veiculo();

	private LazyDataModel<Cliente> model;

	public LazyDataModel<Cliente> getModel() {
		return model;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			if (!this.clienteEdicao.getEmpresas().contains(this.usuarioLogado.getUsuario().getEmpresa())) {
				this.clienteEdicao.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
			}
		}
	}

	public void setTodosClientes(List<Cliente> todosClientes) {
		this.todosClientes = todosClientes;
	}

	public GestaoClientesBean() {
		filtro = new ClienteFilter();

		// consultar();
	}

	public void prepararNovoCadastro() {
		this.clienteEdicao = new Cliente();
		this.clienteEdicao.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
	}

	public void salvar() {
		try {

			this.clienteEdicao = cadastroCliente.salvar(clienteEdicao);
			FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
		} catch (Exception e) {

			String err = e.getMessage();

			if (err.contains("CPF já cadastrado") || err.contains("CNPJ já cadastrado")
					|| err.contains("Documento já cadastrado")) {
				abrirDialog();
			} else {
				throw new NegocioException(e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	static void abrirDialog() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('myDialogVar').show();");
	}

	boolean isExportacao(Cliente cliente) {
		return cliente.getExterior().equals(true);
	}

	boolean isNotExportacao(Cliente cliente) {
		return !this.isExportacao(cliente);
	}

	public void excluir(Cliente cliente) {
		cliente.setStatus(StatusCliente.EXCUIDO);
		cliente = cadastroCliente.salvar(cliente);
	
		messages.info("Cliente excluído com sucesso!");
		
		consultar();
	}

	public void consultar() {
		todosClientes = clientes.pesquisaCliente(filtro);
	}

	public List<Cliente> getTodosClientes() {
		return todosClientes;
	}

	public Cliente getClienteEdicao() {
		return clienteEdicao;
	}

	public void setClienteEdicao(Cliente clienteEdicao) {
		this.clienteEdicao = clienteEdicao;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public boolean isEditando() {
		return this.clienteEdicao.getId() != null;
	}

	public TipoPessoa[] getTipoPessoa() {
		return TipoPessoa.values();
	}

	public TipoCliente[] getTipoCliente() {
		return TipoCliente.values();
	}

	public TributoCliente[] getTributoClientes() {
		return TributoCliente.values();
	}

	public boolean isPessoaFisica() {
		return this.clienteEdicao.getTipoPessoa().equals(TipoPessoa.JURIDICA);
	}

	public void incluirVeiculo() {
		this.veiculoSelecionado = new Veiculo();
		this.veiculoSelecionado.setCliente(this.clienteEdicao);
	}

	public void onConfirmaVeiculo() {
		Boolean existe = false;

		if (!this.clienteEdicao.getVeiculos().contains(this.veiculoSelecionado)) {
			this.clienteEdicao.getVeiculos().add(this.veiculoSelecionado);
			existe = true;
		}

		if (existe == false) {
			for (Veiculo veiculo : this.clienteEdicao.getVeiculos()) {
				if (veiculo.getPlaca() == this.veiculoSelecionado.getPlaca()) {
					existe = true;
				}
			}
		}

		if (existe == false) {
			this.clienteEdicao.getVeiculos().add(this.veiculoSelecionado);
		}
	}

	public void novoEndereco() {
		this.enderecoSelecionado = new Endereco();
		this.enderecoSelecionado.setCliente(this.clienteEdicao);
	}

	public void onConfirmaEndereco() {

		Boolean existe = false;

		if (!this.clienteEdicao.getEnderecos().contains(this.enderecoSelecionado)) {
			this.clienteEdicao.getEnderecos().add(this.enderecoSelecionado);
			existe = true;
		}

		if (existe == false) {
			for (Endereco endereco : this.clienteEdicao.getEnderecos()) {
				if (endereco.getCep() == this.enderecoSelecionado.getCep()) {
					existe = true;
				}
			}
		}

		if (existe == false) {
			this.clienteEdicao.getEnderecos().add(this.enderecoSelecionado);
		}
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Cliente obj = (Cliente) event.getObject();
		this.setClienteEdicao(obj);
		FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroCliente.xhtml?cliente=" + obj.getId());
	}

	public ClienteFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ClienteFilter filtro) {
		this.filtro = filtro;
	}

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public List<String> completarMarca(String descricao) {
		return this.marcas.porNome(descricao);
	}

	public void automatizaNomeFantazia() {
		if ((this.clienteEdicao.getNomeFantasia() == null) || (this.clienteEdicao.getNomeFantasia() == "")) {
			this.clienteEdicao.setNomeFantasia(this.clienteEdicao.getNome());
		}
	}

	public void bairroSelecionado(SelectEvent event) {

		try {
			this.enderecoSelecionado.setBairro(event.getObject().toString());
			info("bairroSelecionado", event.getObject().toString());
		} catch (Exception e) {
			error("bairroSelecionado", e.toString());

		}
	}

	public List<Pais> completarPais(String nome) {
		return this.paises.porNome(nome);
	}

	public void paisSelecionado(SelectEvent event) {

		try {
			Pais pais = (Pais) event.getObject();
			this.clienteEdicao.setPais(pais);
			info("paisSelecionado", pais.getNome());
		} catch (Exception e) {
			error("paisSelecionado", e.getMessage() + " " + e.getCause());
		}
	}

	static void info(String metodo, String valor) {
		System.out.println("INFO | " + metodo + " | " + valor);
	}

	static void error(String metodo, String valor) {
		System.err.println("ERRO | " + metodo + " | " + valor);
	}

	public List<CepEstado> getListaEstados() {
		listaEstados = estados.todos();
		return listaEstados;
	}

	public void atualizarExportacao() {
		this.clienteEdicao.setExterior(false);
	}

	public void atualizarContribuinte() {

		if (TipoPessoa.FISICA.equals(clienteEdicao.getTipoPessoa())) {
			this.clienteEdicao.setContribuinte(false);
		} else {
			this.clienteEdicao.setContribuinte(true);
		}

	}

	public void buscarClientePorDoc() {
		if (this.clienteEdicao.getDocReceitaFederal() != null) {
			this.clienteEdicao = this.clientes.clientePorDoc(this.clienteEdicao.getDocReceitaFederal());
		}
	}

	public List<CepCidade> getListaCidades() {
		return listaCidades;
	}

	public void setListaCidades(List<CepCidade> listaCidades) {
		this.listaCidades = listaCidades;
	}

	public void setListaEstados(List<CepEstado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public void pesquisarCepEnderecos() {
		try {

			Webservicecep cepServiceVO = cepService.getEndereco(this.enderecoSelecionado.getCep());

			String ibgeCidade = cidades.ibgePorCidade(cepServiceVO.getCidade().toUpperCase(),
					cepServiceVO.getUf().toUpperCase());

			String ibgeUF = estados.ibgePorUF(cepServiceVO.getUf().toUpperCase());

			this.enderecoSelecionado.setUf(cepServiceVO.getUf());
			this.enderecoSelecionado.setCidade(cepServiceVO.getCidade());
			this.enderecoSelecionado.setIbgeCidade(ibgeCidade);
			this.enderecoSelecionado.setIbgeEstado(ibgeUF);

			String bairro = cepServiceVO.getBairro();

			bairro = AmsoftUtils.removeCaracteresEspeciais(bairro);

			if (AmsoftUtils.isNotVogal(bairro)) {
				bairro = bairro.substring(0, bairro.length() - 1);
			}

			this.enderecoSelecionado.setBairro(bairro);

			this.enderecoSelecionado.setLogradouro(cepServiceVO.getLogradouro());
			this.enderecoSelecionado.setNumero(cepServiceVO.getResultado());
			this.enderecoSelecionado.setComplemento(cepServiceVO.getResultado_txt());
			this.enderecoSelecionado.setCliente(this.clienteEdicao);
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - " + e.getCause());
		}
	}

	public void concultarCadastro() {

		try {

			Cliente cliente = exixteCNPJ();
			if (cliente == null) {
				this.clienteEdicao = cnpjService.getCadastro(this.clienteEdicao);

				Endereco endereco = this.clienteEdicao.getEnderecos().get(0);

				String ibgeCidade = cidades.ibgePorCidade(endereco.getCidade().toUpperCase(),
						endereco.getUf().toUpperCase());
				String ibgeUF = estados.ibgePorUF(endereco.getUf().toUpperCase());

				this.clienteEdicao.getEnderecos().get(0).setIbgeCidade(ibgeCidade);
				this.clienteEdicao.getEnderecos().get(0).setIbgeEstado(ibgeUF);

			} else {
				this.clienteEdicao = cliente;
			}

		} catch (Exception e) {
			error("concultarCadastro", e.toString());
		}
	}

	private Cliente exixteCNPJ() {
		return clientes.porCNPJ(this.clienteEdicao.getDocReceitaFederal());
	}

}