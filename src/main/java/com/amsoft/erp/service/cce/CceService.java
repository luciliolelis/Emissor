package com.amsoft.erp.service.cce;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfe.CadastroNFeService;

public class CceService implements Serializable {

	private static final long serialVersionUID = 1L;


	
	@Inject
	private CadastroNFeService nfeServise;

	@Inject
	private XMLCceNFeService xmlService;

	public Nfe corrigir(Nfe nfe) throws NegocioException {
		nfe = this.nfeServise.salvar(nfe);
		return this.xmlService.enviar(nfe);
	}
}
