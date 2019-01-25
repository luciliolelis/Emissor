package com.amsoft.erp.controller.empresa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.StatusEmpresa;
import com.amsoft.erp.model.cep.CepCidade;
import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.emitente.FundoCombatePobreza;
import com.amsoft.erp.model.emitente.InscricaoEstadualST;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.nfe.Cfop;
import com.amsoft.erp.model.vo.Webservicecep;
import com.amsoft.erp.repository.Cfops;
import com.amsoft.erp.repository.Cidades;
import com.amsoft.erp.repository.Clientes;
import com.amsoft.erp.repository.Empresas;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.BuscaCNPJEmitente;
import com.amsoft.erp.service.BuscaCep;
import com.amsoft.erp.service.CadastroEmpresaService;
import com.amsoft.erp.service.UploadCertificadoService;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jpa.FacesMessages;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Inject
	private Estados estados;

	@Inject
	private Cidades cidades;

	@Inject
	private BuscaCep cepService;

	@Inject
	private CadastroEmpresaService cadastroEmpresa;

	@Inject
	private FacesMessages messages;

	private CepEstado estadoLinhaEditavel;

	@Inject
	private UploadCertificadoService certificadoService;

	@Inject
	private BuscaCNPJEmitente cnpjService;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private Cfops cfops;

	private String descricaoCfop;
	private List<Cfop> cfopFiltrados;

	private ClienteFilter filtroCliente;
	private List<Cliente> clientesFiltrados;

	private List<Empresa> todasEmpresas;
	private Empresa empresaEdicao = new Empresa();
	private Empresa empresaSelecionada;
	private InscricaoEstadualST inscricaoEstadualSTSelecionada = new InscricaoEstadualST();

	private List<CepEstado> listaEstados = new ArrayList<>();
	private List<CepCidade> listaCidades = new ArrayList<>();

	@Inject
	private Clientes clientes;

	public ClienteFilter getFiltroCliente() {
		return filtroCliente;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public CepEstado getEstadoLinhaEditavel() {
		return estadoLinhaEditavel;
	}

	public void setEstadoLinhaEditavel(CepEstado estadoLinhaEditavel) {
		this.estadoLinhaEditavel = estadoLinhaEditavel;
	}

	public List<CepEstado> completarEstado(String nome) {
		return this.estados.porEstadoNomeObj(nome);
	}

	public void carregarEstadoLinhaEditavelFcp() {

		try {
			FundoCombatePobreza item = this.empresaEdicao.getFundoCombatePobrezaItens().get(0);
			if (this.estadoLinhaEditavel != null) {
				if (this.existeEstadoFcp(this.estadoLinhaEditavel)) {
					FacesUtil.addErrorMessage("Já existem alíquotas de Fundo de combate a pobreza para este estado");
				} else {
					item.setUf(this.estadoLinhaEditavel);
					this.empresaEdicao.adicionarItemVazioFcp();
					this.estadoLinhaEditavel = null;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getCause() + " - " + e.getMessage());
		}
	}

	public void carregarEstadoLinhaEditavel() {

		try {
			InscricaoEstadualST item = this.empresaEdicao.getInscricoesEstaduais().get(0);
			if (this.estadoLinhaEditavel != null) {
				if (this.existeEstadoComIcms(this.estadoLinhaEditavel)) {
					FacesUtil.addErrorMessage("Já existe IE para este estado");
				} else {
					item.setUf(this.estadoLinhaEditavel);
					this.empresaEdicao.adicionarItemVazio();
					this.estadoLinhaEditavel = null;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getCause() + " - " + e.getMessage());
		}
	}

	public void atualizarFcp(FundoCombatePobreza item, int linha) {

		if (linha == 0) {
			item.setUf(null);
		}
	}

	public void atualizarFcpSt(FundoCombatePobreza item, int linha) {
		if (linha == 0) {
			item.setUf(null);
		}
	}

	public void atualizarIE(InscricaoEstadualST item, int linha) {
		if (linha == 0) {
			item.setUf(null);
		}
	}

	private boolean existeEstadoComIcms(CepEstado estado) {
		boolean existeItem = false;

		for (InscricaoEstadualST item : this.getEmpresaEdicao().getInscricoesEstaduais()) {
			if (estado.equals(item.getUf())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	private boolean existeEstadoFcp(CepEstado estado) {
		boolean existeItem = false;

		for (FundoCombatePobreza item : this.getEmpresaEdicao().getFundoCombatePobrezaItens()) {
			if (estado.equals(item.getUf())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);
	}

	public void prepararNovoCadastro() {
		empresaEdicao = new Empresa();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			if (empresaEdicao.getId() != null) {

			}

			this.empresaEdicao.adicionarItemVazio();
			this.empresaEdicao.adicionarItemVazioFcp();
			;
		}
	}

	public void salvar() {

		this.empresaEdicao.removerItemVazio();
		this.empresaEdicao.removerItemVazioFcp();
		this.empresaEdicao = cadastroEmpresa.salvar(empresaEdicao);

		usuarioLogado.getUsuario().setEmpresa(this.empresaEdicao);

		consultar();
		FacesUtil.addInfoMessage("Empresa salva com sucesso!");
		this.empresaEdicao.adicionarItemVazio();
		this.empresaEdicao.adicionarItemVazioFcp();
	}

	public String getFoto() {
		if (this.empresaEdicao != null) {
			if (this.empresaEdicao.getLogo_nome() != null) {
				return this.empresaEdicao.getLogo_nome();
			} else {
				return "logo.png";
			}
		}
		return "logo.png";
	}

	public void handleFileUpload(FileUploadEvent event) {

		try {
			this.empresaEdicao.setLogo(event.getFile().getContents());
			this.empresaEdicao.setLogo_nome(event.getFile().getFileName());
			FacesUtil.addInfoMessage("Upload " + event.getFile().getFileName() + " realizado com sucesso!");
		} catch (Exception e) {

		}
	}

	public void uploadCertificado(FileUploadEvent event) {

		try {
			this.certificadoService.upload(event.getFile());
			this.empresaEdicao.setCertificado(event.getFile().getContents());
			this.empresaEdicao.setNomeCertificado(event.getFile().getFileName());
			FacesUtil.addInfoMessage("Upload " + event.getFile().getFileName() + " realizado com sucesso!");
		} catch (Exception e) {
		}
	}

	public void excluir(Empresa empresa) {

		empresa.setStatus(StatusEmpresa.EXCUIDO);
		empresa = cadastroEmpresa.salvar(empresa);

		messages.info("Empresa excluída com sucesso!");
		consultar();

	}

	public void consultar() {
		todasEmpresas = empresas.todas();
	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

	public RegimeTributario[] getRegimeTributarios() {
		return RegimeTributario.values();
	}

	public Empresa getEmpresaEdicao() {
		return empresaEdicao;
	}

	public void setEmpresaEdicao(Empresa empresaEdicao) {
		this.empresaEdicao = empresaEdicao;
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public InscricaoEstadualST getInscricaoEstadualSTSelecionada() {
		return inscricaoEstadualSTSelecionada;
	}

	public void setInscricaoEstadualSTSelecionada(InscricaoEstadualST inscricaoEstadualSTSelecionada) {
		this.inscricaoEstadualSTSelecionada = inscricaoEstadualSTSelecionada;
	}

	public boolean isEditando() {
		return this.empresaEdicao.getId() != null;
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Empresa obj = (Empresa) event.getObject();
		this.setEmpresaEdicao(obj);
		FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroEmpresa.xhtml?empresa=" + obj.getId());
	}

	public void handleFileUploadOld(FileUploadEvent event) {

		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();

		String absoluteDiskPath = servletContext.getRealPath("/resources/fotos/");
		File targetFolder = new File(absoluteDiskPath);

		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}

		try (InputStream inputStream = event.getFile().getInputstream()) {

			OutputStream out;

			out = new FileOutputStream(new File(targetFolder, event.getFile().getFileName()));

			int read;

			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			out.flush();
			out.close();

			this.empresaEdicao.setLogo(event.getFile().getContents());
			this.empresaEdicao.setLogo_nome(event.getFile().getFileName());
			FacesUtil.addInfoMessage("Upload da logo " + event.getFile().getFileName() + " realizado com sucesso!");

		} catch (IOException ex) {

		}
	}

	static void info(String metodo, String valor) {
		System.out.println("INFO | " + metodo + " | " + valor);
	}

	static void error(String metodo, String valor) {
		System.err.println("ERRO | " + metodo + " | " + valor);
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

	public List<CepEstado> getListaEstados() {
		listaEstados = estados.todos();
		return listaEstados;
	}

	public List<Cfop> getCfopFiltrados() {
		return cfopFiltrados;
	}

	public void cfopSelecionar(Cfop cfop) {
		this.empresaEdicao.setCfop_nfce(cfop);
	}

	public void novaPesquisa() {
		cfopPesquisar();
	}

	public String getDescricaoCfop() {
		return descricaoCfop;
	}

	public void setDescricaoCfop(String descricaoCfop) {
		this.descricaoCfop = descricaoCfop;
	}

	public void cfopPesquisar() {
		cfopFiltrados = cfops.todosConcumidorFinal();
	}

	public void setCfopFiltrados(List<Cfop> cfopFiltrados) {
		this.cfopFiltrados = cfopFiltrados;
	}

	public void limparCliente() {
		this.empresaEdicao.setCliente(null);

		info("limparCliente", "Ok");

	}

	public void limparCFOP() {
		this.empresaEdicao.setCfop_nfce(null);

		info("limparCFOP", "Ok");

	}

	@NotBlank
	public String getCodigoCfop() {
		return (this.empresaEdicao.getCfop_nfce() == null) ? null
				: this.empresaEdicao.getCfop_nfce().getCodigo().toString();
	}

	public void pesquisaClientes() {
		filtroCliente = new ClienteFilter();
		this.clientesFiltrados = this.clientes.pesquisaCliente(filtroCliente);
	}

	public void clienteSelecionar(Cliente cliente) {
		this.empresaEdicao.setCliente(cliente);

		info("clienteSelecionar", cliente.getNome());

	}

	public void clienteSelecionado(SelectEvent event) {

		try {

			Cliente cliente = (Cliente) event.getObject();
			this.empresaEdicao.setCliente(cliente);

			info("clienteSelecionado", "Cliente selecionado: " + cliente.getNome());
		} catch (Exception e) {
			error("clienteSelecionado", e.getMessage() + " " + e.getCause());

		}
	}

	public void pesquisarCepEnderecos() {
		try {

			Webservicecep cepServiceVO = cepService.getEndereco(this.empresaEdicao.getCep());
			String ibgeCidade = cidades.ibgePorCidade(cepServiceVO.getCidade().toUpperCase(),
					cepServiceVO.getUf().toUpperCase());
			String ibgeUF = estados.ibgePorUF(cepServiceVO.getUf().toUpperCase());
			this.empresaEdicao.setUf(cepServiceVO.getUf());
			this.empresaEdicao.setCidade(cepServiceVO.getCidade());
			this.empresaEdicao.setIbgeCidade(ibgeCidade);
			this.empresaEdicao.setIbgeEstado(ibgeUF);

			String bairro = cepServiceVO.getBairro();

			bairro = AmsoftUtils.removeCaracteresEspeciais(bairro);

			if (AmsoftUtils.isNotVogal(bairro)) {
				bairro = bairro.substring(0, bairro.length() - 1);
			}

			this.empresaEdicao.setBairro(bairro);

			this.empresaEdicao.setLogradouro(cepServiceVO.getLogradouro());
			this.empresaEdicao.setNumero(cepServiceVO.getResultado());
			this.empresaEdicao.setComplemento(cepServiceVO.getResultado_txt());
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - " + e.getCause());
		}
	}

	public void concultarCadastro() {

		try {

			this.empresaEdicao = cnpjService.getCadastro(this.empresaEdicao);

			String ibgeCidade = cidades.ibgePorCidade(empresaEdicao.getCidade().toUpperCase(),
					empresaEdicao.getUf().toUpperCase());
			String ibgeUF = estados.ibgePorUF(empresaEdicao.getUf().toUpperCase());

			this.empresaEdicao.setIbgeCidade(ibgeCidade);
			this.empresaEdicao.setIbgeEstado(ibgeUF);

		} catch (Exception e) {
			error("concultarCadastro", e.toString());
		}
	}

}