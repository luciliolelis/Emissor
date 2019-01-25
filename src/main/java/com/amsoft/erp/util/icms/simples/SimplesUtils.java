package com.amsoft.erp.util.icms.simples;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;

public class SimplesUtils {

	public static ICMS popularIcmsSN(ItemProdutoNFCe item) {
		ICMS icms = new ICMS();

		if (CsosnUtils.isICMSSN101(item)) {
			icms.setICMSSN101(TagNFCe.getICMSST101(item));
		} else if (CsosnUtils.isICMSSN102(item)) {
			icms.setICMSSN102(TagNFCe.getICMSST102(item));
		} else if (CsosnUtils.isICMSSN201(item)) {
			icms.setICMSSN201(TagNFCe.getICMSST201(item));
		} else if (CsosnUtils.isICMSSN202(item)) {
			icms.setICMSSN202(TagNFCe.getICMSST202(item));
		} else if (CsosnUtils.isICMSSN500(item)) {
			icms.setICMSSN500(TagNFCe.getICMSST500(item));
		} else if (CsosnUtils.isICMSSN900(item)) {
			icms.setICMSSN900(TagNFCe.getICMSST900(item));
		}

		return icms;
	}

	public static ICMS popularIcmsSN(ItemProduto item) {

		ICMS icms = new ICMS();

		if (CsosnUtils.isICMSSN101(item)) {
			icms.setICMSSN101(TagNFe.getICMSST101(item));
		} else if (CsosnUtils.isICMSSN102(item)) {
			icms.setICMSSN102(TagNFe.getICMSST102(item));
		} else if (CsosnUtils.isICMSSN201(item)) {
			icms.setICMSSN201(TagNFe.getICMSST201(item));
		} else if (CsosnUtils.isICMSSN202(item)) {
			icms.setICMSSN202(TagNFe.getICMSST202(item));
		} else if (CsosnUtils.isICMSSN500(item)) {
			icms.setICMSSN500(TagNFe.getICMSST500(item));
		} else if (CsosnUtils.isICMSSN900(item)) {
			icms.setICMSSN900(TagNFe.getICMSST900(item));
		}

		return icms;
	}

}
