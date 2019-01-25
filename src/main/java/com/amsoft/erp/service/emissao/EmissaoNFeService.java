package com.amsoft.erp.service.emissao;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.service.nfe.CadastroNFeService;
import com.chronos.calc.TributacaoException;

public class EmissaoNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private XMLEnvioNFeService xmlService;

	@Inject
	private CadastroNFeService nfeServise;

	public Nfe emitir(Nfe nfe) throws FileNotFoundException, TributacaoException {
		nfe = this.nfeServise.salvar(nfe);
		return this.xmlService.enviar(nfe);
	}
}
