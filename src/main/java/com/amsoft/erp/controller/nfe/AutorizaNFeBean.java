package com.amsoft.erp.controller.nfe;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.emissao.EmissaoNFeService;
import com.amsoft.erp.service.nfe.CadastroNFeService;

@Named
@RequestScoped
public class AutorizaNFeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFeEdicao
	private Nfe nfe;

	@Inject
	private Event<NFeAlteradaEvent> nfeAlteradoEvent;

	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private EmissaoNFeService emissaoNFeService;

	public void emitir() throws FileNotFoundException {

		this.nfe.removerItemVazio();

		try {
			this.nfe = this.emissaoNFeService.emitir(this.nfe);
			this.nfeAlteradoEvent.fire(new NFeAlteradaEvent(this.nfe));
			this.nfe = nfeServise.salvar(this.nfe);
		} finally {
			this.nfe.adicionarItemVazio();
		}

	}

}
