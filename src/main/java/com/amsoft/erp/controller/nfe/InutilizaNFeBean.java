package com.amsoft.erp.controller.nfe;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.inutilizacao.InutilizacaoNFeService;
import com.amsoft.erp.service.nfe.CadastroNFeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class InutilizaNFeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	@NFeEdicao
	private Nfe nfe;

	@Inject
	private Event<NFeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private InutilizacaoNFeService inutilizacaoNFeService;

	public void inutilizar() throws JAXBException {
		this.nfe.removerItemVazio();
		try {

			this.nfe = this.inutilizacaoNFeService.inutilizar(nfe);
			this.nfeAlteradoEvent.fire(new NFeAlteradaEvent(nfe));
			this.nfe = nfeServise.salvar(nfe);

		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			this.nfe.adicionarItemVazio();
		}

	}

}
