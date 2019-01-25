package com.amsoft.erp.service.cancelamento;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfce.CadastroNFCeService;

public class CancelamentoNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroNFCeService nfeServise;

	@Inject
	private XMLCancelamentoNFCeService xmlService;

	public NFCe cancelar(NFCe nfe) throws NegocioException {
		nfe = this.nfeServise.salvar(nfe);
		return this.xmlService.enviar(nfe);
	}
}
