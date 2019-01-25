package com.amsoft.erp.controller.nfe;

import com.amsoft.erp.model.nfe.Nfe;

public class NFeAlteradaEvent {

	private Nfe nfe;
	
	public NFeAlteradaEvent(Nfe nfe) {
		this.nfe = nfe;
	}

	public Nfe getNFe() {
		return nfe;
	}
	
}
