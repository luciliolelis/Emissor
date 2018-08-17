package com.amsoft.erp.service.inutilizacao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.service.nfce.CadastroNFCeService;

public class InutilizacaoNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFCes nfces;

	@Inject
	private CadastroNFCeService cadastroNFeService;

	@Inject
	private XMLInutilizacaoNFCeService xmlService;

	public NFCe inutilizar(NFCe nfe) throws NegocioException, JAXBException {

		nfe = this.cadastroNFeService.salvar(nfe);
		nfe = this.xmlService.enviar(nfe);
		nfe = this.nfces.guardar(nfe);
		return nfe;
	}
}
