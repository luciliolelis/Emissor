package com.amsoft.erp.service.nfe;

import java.io.Serializable;

import javax.inject.Inject;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jpa.Transactional;

public class ExcluirNfeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Transactional
	public Nfe excluir(Nfe nfe) {
		nfe = this.nfes.porId(nfe.getId());

		if (!nfe.isOrcamento() || nfe.isInutilizada()) {
			throw new NegocioException("NF-e não pode ser excluída no status " + nfe.getStatus().getDescricao() + ".");
		}
		

		try {
			nfe.setStatus(StatusNFe.EXCUIDA);
			nfe = this.nfes.guardar(nfe);
		} catch (Exception e) {
			throw new NegocioException("Falha ao excluir NFe.");
		}

		return nfe;
	}
}
