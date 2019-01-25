package com.amsoft.erp.controller.nfce;

import com.amsoft.erp.model.nfce.NFCe;

public class NFCeAlteradaEvent {

	private NFCe nfce;
	
	public NFCeAlteradaEvent(NFCe nfce) {
		this.nfce = nfce;
	}

	public NFCe getNFCe() {
		return nfce;
	}
	
}
