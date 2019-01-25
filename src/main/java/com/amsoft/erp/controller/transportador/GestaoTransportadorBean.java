package com.amsoft.erp.controller.transportador;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Endereco;
import com.amsoft.erp.model.StatusCliente;
import com.amsoft.erp.model.Veiculo;
import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.enun.TipoCliente;
import com.amsoft.erp.model.enun.TipoPessoa;
import com.amsoft.erp.model.enun.TributoCliente;
import com.amsoft.erp.model.vo.Webservicecep;
import com.amsoft.erp.repository.Cidades;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.repository.Marcas;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.BuscaCNPJ;
import com.amsoft.erp.service.BuscaCep;
import com.amsoft.erp.service.CadastroClienteService;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GestaoTransportadorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;


	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private Estados estados;

	@Inject
	private Cidades cidades;

	@Inject
	private Marcas marcas;

	@Inject
	private CadastroClienteService cadastroCliente;

	private ClienteFilter filtro;
	@Inject
	private BuscaCep cepService;

	@Inject
	private BuscaCNPJ cnpjService;

	private List<Cliente> todosClientes;

	private Cliente clienteEdicao = new Cliente();
	private Cliente clienteSelecionado;

	private Endereco enderecoSelecionado = new Endereco();
	private Veiculo veiculoSelecionado = new Veiculo();

	private List<CepEstado> listaEstados = new ArrayList<>();
	private List<String> listaCidades = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			if (!this.clienteEdicao.getEmpresas().contains(this.usuarioLogado.getUsuario().getEmpresa())) {
				this.clienteEdicao.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
			}
		}

		this.clienteEdicao.setTipoCliente(TipoCliente.TRANSPORTADOR);
	}

	public void setTodosClientes(List<Cliente> todosClientes) {
		this.todosClientes = todosClientes;
	}

	public GestaoTransportadorBean() {
		filtro = new ClienteFilter();
	}

	public void prepararNovoCadastro() {
		this.clienteEdicao = new Cliente();
		this.clienteEdicao.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
		this.clienteEdicao.setTipoCliente(TipoCliente.TRANSPORTADOR);
	}

	public void salvar() {

		if (this.clienteEdicao.getEmpresas().isEmpty()) {
			this.clienteEdicao.getEmpresas().add(this.usuarioLogado.getUsuario().getEmpresa());
		}

		this.clienteEdicao = cadastroCliente.salvar(clienteEdicao);
		consultar();
		FacesUtil.addInfoMessage("Transportador salvo com sucesso!");
	}

	public void excluir(Cliente cliente) {
		cliente.setStatus(StatusCliente.EXCUIDO);
		cliente = cadastroCliente.salvar(cliente);

		FacesUtil.addInfoMessage("Transportador excluído com sucesso!");

		consultar();
	}

	public void consultar() {
		todosClientes = clientes.pesquisaTransportador(filtro);
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
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("CadastroTransportador.xhtml?cliente=" + obj.getId());
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

	public Veiculo getVeiculoSelecionado() {
		return veiculoSelecionado;
	}

	public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
		this.veiculoSelecionado = veiculoSelecionado;
	}

	public List<String> completarMarca(String descricao) {
		return this.marcas.porNome(descricao);
	}

	public void automatizaNomeFantazia() {
		if ((this.clienteEdicao.getNomeFantasia() == null) || (this.clienteEdicao.getNomeFantasia() == "")) {
			this.clienteEdicao.setNomeFantasia(this.clienteEdicao.getNome());
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

	public void carregarCidadesVeiculos() {
		listaCidades.clear();
		listaCidades = this.cidades.cidadesByEstado(this.veiculoSelecionado.getUf());
	}

	public List<String> getListaCidades() {
		return listaCidades;
	}

	public void setListaCidades(List<String> listaCidades) {
		this.listaCidades = listaCidades;
	}

	public void setListaEstados(List<CepEstado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public void ufSelecionado(SelectEvent event) {
		try {
			String estado = (String) event.getObject();
			this.enderecoSelecionado.setUf(estado);
			info("ufSelecionado", estado);
		} catch (Exception e) {
			error("ufSelecionado", e.getMessage() + " " + e.getCause());
		}
	}

	public void cidadeSelecionada(SelectEvent event) {
		try {
			String cidade = (String) event.getObject();
			this.enderecoSelecionado.setCidade(cidade);
			info("cidadeSelecionada", cidade);

		} catch (Exception e) {
			error("cidadeSelecionada", e.toString());
		}
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

			Cliente c = exixteCNPJ();
			if (c == null) {
				this.clienteEdicao = cnpjService.getCadastro(this.clienteEdicao);

				Endereco endereco = this.clienteEdicao.getEnderecos().get(0);

				String ibgeCidade = cidades.ibgePorCidade(endereco.getCidade().toUpperCase(),
						endereco.getUf().toUpperCase());
				String ibgeUF = estados.ibgePorUF(endereco.getUf().toUpperCase());

				this.clienteEdicao.getEnderecos().get(0).setIbgeCidade(ibgeCidade);
				this.clienteEdicao.getEnderecos().get(0).setIbgeEstado(ibgeUF);

			} else {
				this.clienteEdicao = c;
			}

		} catch (Exception e) {
			error("concultarCadastro", e.toString());
		}
	}

	private Cliente exixteCNPJ() {
		return clientes.porCNPJ(this.clienteEdicao.getDocReceitaFederal());
	}
}