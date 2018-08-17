package com.amsoft.erp.controller.nfce;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.emissao.EmissaoNFCeService;
import com.amsoft.erp.service.nfce.CadastroNFCeService;

@Named
@RequestScoped
public class AutorizaNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfe;

	@Inject
	private Event<NFCeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFCeService nfeServise;

	@Inject
	private EmissaoNFCeService emissaoNFeService;

	public void emitir() throws FileNotFoundException {

		this.nfe.removerItemVazio();
		this.nfe.removerItemVazioFormaPagamento();

		try {
			this.nfe = this.emissaoNFeService.emitir(this.nfe);
			this.nfeAlteradoEvent.fire(new NFCeAlteradaEvent(this.nfe));
			this.nfe = nfeServise.salvar(this.nfe);
		} finally {
			this.nfe.adicionarItemVazio();
			this.nfe.adicionarItemVazioFormaPagamento();
		}

	}

}
