package com.amsoft.erp.controller.grupos;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.Grupo;
import com.amsoft.erp.model.GrupoPermissao;
import com.amsoft.erp.repository.Grupos;
import com.amsoft.erp.service.CadastroGrupoService;
import com.amsoft.erp.util.jpa.FacesMessages;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class GestaoGruposBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesMessages messages;

	@Inject
	private Grupos grupos;

	@Inject
	private CadastroGrupoService grupoService;

	private List<Grupo> todosGupos;
	private Grupo grupoEdicao = new Grupo();

	private List<String> selectedClientes;
	private List<String> selectedTrasportadores;
	private List<String> selectedFornecedores;
	private List<String> selectedEmpresas;
	private List<String> selectedUsuarios;
	private List<String> selectedProdutos;
	private List<String> selectedLogs;
	private List<String> selectedGrupos;
	private List<String> selectedNcm;

	private List<String> selectedNF;
	private List<String> selectedNFE;
	private List<String> selectedCF;

	private List<GrupoPermissao> todasPermissoes = new ArrayList<GrupoPermissao>();

	public List<GrupoPermissao> getTodaspermissoes() {
		return todasPermissoes;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			selectedClientes = new ArrayList<String>();
			selectedEmpresas = new ArrayList<String>();
			selectedUsuarios = new ArrayList<String>();
			selectedProdutos = new ArrayList<String>();

			selectedNcm = new ArrayList<String>();

			selectedGrupos = new ArrayList<String>();
			selectedLogs = new ArrayList<String>();
			selectedNF = new ArrayList<String>();
			selectedNFE = new ArrayList<String>();
			selectedCF = new ArrayList<String>();

			selectedTrasportadores = new ArrayList<String>();
			selectedFornecedores = new ArrayList<String>();

			if (this.grupoEdicao != null) {

				for (Integer i = 0; i < this.grupoEdicao.getPermissoes().size(); i++) {

					String permissao = this.grupoEdicao.getPermissoes().get(i).getNome();

					if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("CLIENTE_")) {
						this.selectedClientes.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("EMPRESA_")) {
						this.selectedEmpresas.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("PRODUTO_")) {
						this.selectedProdutos.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("USUARIO_")) {
						this.selectedUsuarios.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("LOG_")) {
						this.selectedLogs.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("FORNECEDOR_")) {
						this.selectedFornecedores.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("GRUPO_")) {
						this.selectedGrupos.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("NF_")) {
						this.selectedNF.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("NFE_")) {
						this.selectedNFE.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("CF_")) {
						this.selectedCF.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("TRANSPORTADOR_")) {
						this.selectedTrasportadores.add(permissao);
					} else if (this.grupoEdicao.getPermissoes().get(i).getNome().contains("NCM_")) {
						this.selectedNcm.add(permissao);
					}

					System.out.println(this.grupoEdicao.getPermissoes().get(i).getNome());
				}

			}

		}

	}

	public void consultar() {
		this.todosGupos = this.grupos.todos();
	}

	public void excluir(Grupo grupo) {

		try {
			grupoService.excluir(grupo);
			consultar();
			FacesUtil.addInfoMessage("Grupo excluído com sucesso!");
		} catch (Exception e) {
			messages.error("Este grupo não pode ser excluído");
		}

	}

	public void salvar() {

		List<GrupoPermissao> permite = new ArrayList<GrupoPermissao>();

		for (Integer i = 0; i < this.selectedClientes.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedClientes.get(i));
			permissao.setNome(this.selectedClientes.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedClientes.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedFornecedores.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedFornecedores.get(i));
			permissao.setNome(this.selectedFornecedores.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedFornecedores.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedTrasportadores.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedTrasportadores.get(i));
			permissao.setNome(this.selectedTrasportadores.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedTrasportadores.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedEmpresas.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedEmpresas.get(i));
			permissao.setNome(this.selectedEmpresas.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedEmpresas.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedUsuarios.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedUsuarios.get(i));
			permissao.setNome(this.selectedUsuarios.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedUsuarios.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedProdutos.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedProdutos.get(i));
			permissao.setNome(this.selectedProdutos.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedProdutos.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedLogs.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedLogs.get(i));
			permissao.setNome(this.selectedLogs.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedLogs.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedGrupos.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedGrupos.get(i));
			permissao.setNome(this.selectedGrupos.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedGrupos.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedNF.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedNF.get(i));
			permissao.setNome(this.selectedNF.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedNF.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedNFE.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedNFE.get(i));
			permissao.setNome(this.selectedNFE.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			// System.out.println(this.selectedNFE.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedCF.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedCF.get(i));
			permissao.setNome(this.selectedCF.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedCF.get(i).toString());
		}

		for (Integer i = 0; i < this.selectedNcm.size(); i++) {

			GrupoPermissao permissao = new GrupoPermissao();
			permissao.setDescricao(this.selectedNcm.get(i));
			permissao.setNome(this.selectedNcm.get(i));
			permissao.setGrupo(grupoEdicao);
			permite.add(permissao);

			System.out.println(this.selectedNcm.get(i).toString());
		}

		this.grupoEdicao.setPermissoes(permite);

		this.grupoEdicao = this.grupoService.salvar(this.grupoEdicao);

		FacesUtil.addInfoMessage("Grupo salvo com sucesso!");


	}

	public boolean isEditando() {
		return this.grupoEdicao.getId() != null;
	}

	public List<Grupo> getTodosGupos() {
		return todosGupos;
	}

	public void setTodosGupos(List<Grupo> todosGupos) {
		this.todosGupos = todosGupos;
	}

	public Grupo getGrupoEdicao() {
		return grupoEdicao;
	}

	public void setGrupoEdicao(Grupo grupoEdicao) {
		this.grupoEdicao = grupoEdicao;
	}

	public List<String> getSelectedClientes() {
		return selectedClientes;
	}

	public void setSelectedClientes(List<String> selectedClientes) {
		this.selectedClientes = selectedClientes;
	}

	public List<String> getSelectedTrasportadores() {
		return selectedTrasportadores;
	}

	public void setSelectedTrasportadores(List<String> selectedTrasportadores) {
		this.selectedTrasportadores = selectedTrasportadores;
	}

	public List<String> getSelectedFornecedores() {
		return selectedFornecedores;
	}

	public void setSelectedFornecedores(List<String> selectedFornecedores) {
		this.selectedFornecedores = selectedFornecedores;
	}

	public List<String> getSelectedEmpresas() {
		return selectedEmpresas;
	}

	public void setSelectedEmpresas(List<String> selectedEmpresas) {
		this.selectedEmpresas = selectedEmpresas;
	}

	public List<String> getSelectedUsuarios() {
		return selectedUsuarios;
	}

	public void setSelectedUsuarios(List<String> selectedUsuarios) {
		this.selectedUsuarios = selectedUsuarios;
	}

	public List<String> getSelectedProdutos() {
		return selectedProdutos;
	}

	public void setSelectedProdutos(List<String> selectedProdutos) {
		this.selectedProdutos = selectedProdutos;
	}

	public List<String> getSelectedLogs() {
		return selectedLogs;
	}

	public void setSelectedLogs(List<String> selectedLogs) {
		this.selectedLogs = selectedLogs;
	}

	public List<String> getSelectedGrupos() {
		return selectedGrupos;
	}

	public void setSelectedGrupos(List<String> selectedGrupos) {
		this.selectedGrupos = selectedGrupos;
	}

	public List<String> getSelectedNF() {
		return selectedNF;
	}

	public void setSelectedNF(List<String> selectedNF) {
		this.selectedNF = selectedNF;
	}

	public List<String> getSelectedNFE() {
		return selectedNFE;
	}

	public void setSelectedNFE(List<String> selectedNFE) {
		this.selectedNFE = selectedNFE;
	}

	public List<String> getSelectedCF() {
		return selectedCF;
	}

	public void setSelectedCF(List<String> selectedCF) {
		this.selectedCF = selectedCF;
	}

	public List<GrupoPermissao> getTodasPermissoes() {
		return todasPermissoes;
	}

	public void setTodasPermissoes(List<GrupoPermissao> todasPermissoes) {
		this.todasPermissoes = todasPermissoes;
	}

	public void prepararNovoCadastro() {
		this.grupoEdicao = new Grupo();
	}

	public List<String> getSelectedNcm() {
		return selectedNcm;
	}

	public void setSelectedNcm(List<String> selectedNcm) {
		this.selectedNcm = selectedNcm;
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Grupo obj = (Grupo) event.getObject();
		this.setGrupoEdicao(obj);
		FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroGrupo.xhtml?grupo=" + obj.getId());
	}
}