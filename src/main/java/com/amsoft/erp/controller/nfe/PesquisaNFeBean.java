package com.amsoft.erp.controller.nfe;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.repository.filter.NfeFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.nfe.ExcluirNfeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaNFeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	private List<Nfe> filtrados;
	private NfeFilter filtro;


	private LazyDataModel<Nfe> model;

	@Inject
	private ExcluirNfeService excluirNfeService;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public PesquisaNFeBean() {
		filtro = new NfeFilter();

		model = new LazyDataModel<Nfe>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Nfe> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));

				setRowCount(nfes.quantidadeFiltrados(filtro));

				return nfes.filtrados(filtro);
			}

		};
	}

	public LazyDataModel<Nfe> getModel() {
		return model;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	public void excluir(Nfe nfe) {

		nfe = excluirNfeService.excluir(nfe);
		FacesUtil.addInfoMessage("Nf-e " + nfe.getNumero() + " excluída com sucesso.");
	}

	public void pesquisar() {
		filtro.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		filtrados = this.nfes.pesquisa(filtro);
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

	public void setFiltro(NfeFilter filtro) {
		this.filtro = filtro;
	}

	public StatusNFe[] getStatuses() {
		return StatusNFe.values();
	}

}