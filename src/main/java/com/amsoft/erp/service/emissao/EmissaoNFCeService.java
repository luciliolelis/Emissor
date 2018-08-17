package com.amsoft.erp.service.emissao;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.nfce.CadastroNFCeService;


public class EmissaoNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;


	@Inject
	private XMLEnvioNFCeService xmlService;
	
	@Inject
	private CadastroNFCeService nfeServise;

	public NFCe emitir(NFCe nfe) throws FileNotFoundException {
		nfe = this.nfeServise.salvar(nfe);
		return this.xmlService.enviar(nfe);
	}
}
