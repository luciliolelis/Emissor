package com.amsoft.erp.controller.nfce;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.repository.filter.NfceFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.nfce.ExcluirNFCeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFCes nfes;

	private List<NFCe> filtrados;
	private NfceFilter filtro;

	private LazyDataModel<NFCe> model;

	@Inject
	private ExcluirNFCeService excluirNFCeService;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public PesquisaNFCeBean() {
		filtro = new NfceFilter();

		model = new LazyDataModel<NFCe>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<NFCe> load(int first, int pageSize, String sortField, SortOrder sortOrder,
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

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	public void excluir(NFCe nfce) {
		nfce = excluirNFCeService.excluir(nfce);
		FacesUtil.addInfoMessage("NFC-e " + nfce.getNumero() + " excluída com sucesso.");
	}

	public void pesquisar() {
		filtro.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());
		filtrados = this.nfes.pesquisa(filtro);
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

	public StatusNFe[] getStatuses() {
		return StatusNFe.values();
	}

	public LazyDataModel<NFCe> getModel() {
		return model;
	}
}