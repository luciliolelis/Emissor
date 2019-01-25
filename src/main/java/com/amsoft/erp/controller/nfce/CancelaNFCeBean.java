package com.amsoft.erp.controller.nfce;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.cancelamento.CancelamentoNFCeService;
import com.amsoft.erp.service.nfce.CadastroNFCeService;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@RequestScoped
public class CancelaNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfe;

	@Inject
	private Event<NFCeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFCeService nfeServise;

	@Inject
	private CancelamentoNFCeService cancelamentoNFeService;

	public void emitir() throws FileNotFoundException {

		try {
			this.nfe = this.cancelamentoNFeService.cancelar(this.nfe);
			this.nfeAlteradoEvent.fire(new NFCeAlteradaEvent(this.nfe));
			this.nfe = nfeServise.salvar(this.nfe);
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}

	}

}
