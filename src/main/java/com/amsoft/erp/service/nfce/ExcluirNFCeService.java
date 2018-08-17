package com.amsoft.erp.service.nfce;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jpa.Transactional;

public class ExcluirNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFCes nfCes;

	@Transactional
	public NFCe excluir(NFCe nfce) {
		
		if (!nfce.isCadastro()) {
			throw new NegocioException("NFC-e não pode ser excluída no status " + nfce.getStatus().getDescricao() + ".");
		}

		
		
		nfce = this.nfCes.porId(nfce.getId());

		try {
			nfce.setStatus(StatusNFe.EXCUIDA);
			nfce = this.nfCes.guardar(nfce);
		} catch (Exception e) {
			throw new NegocioException("Falha ao excluir NFC-e.");
		}

		return nfce;
	}
}
