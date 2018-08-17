package com.amsoft.erp.service.cancelamento;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfe.CadastroNFeService;

public class CancelamentoNFeService implements Serializable {

	private static final long serialVersionUID = 1L;


	
	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private XMLCancelamentoNFeService xmlService;

	public Nfe cancelar(Nfe nfe) throws NegocioException {
		nfe = this.nfeServise.salvar(nfe);
		return this.xmlService.enviar(nfe);
	}
}
