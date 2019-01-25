package com.amsoft.erp.controller.nfe;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.cancelamento.CancelamentoNFeService;
import com.amsoft.erp.service.nfe.CadastroNFeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class CancelaNFeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFeEdicao
	private Nfe nfe;

	@Inject
	private Event<NFeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private CancelamentoNFeService cancelamentoNFeService;

	public void emitir() throws FileNotFoundException {

		try {
			this.nfe = this.cancelamentoNFeService.cancelar(this.nfe);
			this.nfeAlteradoEvent.fire(new NFeAlteradaEvent(this.nfe));
			this.nfe = nfeServise.salvar(this.nfe);
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}

	}

}
