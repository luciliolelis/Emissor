package com.amsoft.erp.util.icms.normal;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.chronos.calc.TributacaoException;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;

public class NormalUtils {

	public static ICMS popularICMS(ItemProduto item) throws TributacaoException {
		ICMS icms = new ICMS();

		if (CstUtils.isICMS00(item)) {
			icms.setICMS00(TagNFe.getICMS00(item));
		} else if (CstUtils.isICMS10(item)) {
			icms.setICMS10(TagNFe.getICMS10(item));
		} else if (CstUtils.isICMS20(item)) {
			icms.setICMS20(TagNFe.getICMS20(item));
		} else if (CstUtils.isICMS30(item)) {
			icms.setICMS30(TagNFe.getICMS30(item));
		} else if (CstUtils.isICMS40(item)) {
			icms.setICMS40(TagNFe.getICMS40(item));
		} else if (CstUtils.isICMS51(item)) {
			icms.setICMS51(TagNFe.getICMS51(item));
		} else if (CstUtils.isICMS60(item)) {
			icms.setICMS60(TagNFe.getICMS60(item));
		} else if (CstUtils.isICMS70(item)) {
			icms.setICMS70(TagNFe.getICMS70(item));
		} else if (CstUtils.isICMS90(item)) {
			icms.setICMS90(TagNFe.getICMS90(item));
		} else if (CstUtils.isICMSST(item)) {
			icms.setICMSST(TagNFe.getICMSST(item));
		} else if (CstUtils.isICMSPart(item)) {
			icms.setICMSPart(TagNFe.getICMSPart(item));
		}

		return icms;
	}

	

}
