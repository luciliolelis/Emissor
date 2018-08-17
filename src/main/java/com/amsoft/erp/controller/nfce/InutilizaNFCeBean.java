package com.amsoft.erp.controller.nfce;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.inutilizacao.InutilizacaoNFCeService;
import com.amsoft.erp.service.nfce.CadastroNFCeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class InutilizaNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfe;

	@Inject
	private Event<NFCeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFCeService nfeServise;

	@Inject
	private InutilizacaoNFCeService inutilizacaoNFCeService;

	public void inutilizar() throws JAXBException {
		this.nfe.removerItemVazio();
		try {

			this.nfe = this.inutilizacaoNFCeService.inutilizar(nfe);
			this.nfeAlteradoEvent.fire(new NFCeAlteradaEvent(nfe));
			this.nfe = nfeServise.salvar(nfe);

		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			this.nfe.adicionarItemVazio();
		}

	}

}
