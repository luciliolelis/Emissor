package com.amsoft.erp.util;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TIpi;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TIpi.IPINT;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TIpi.IPITrib;

public class IPIUtils {

	
	public static TIpi popularIPI(ItemProduto item) {

		if (IPIValidacoes.isNT(item)) {
			return popularIPINT(item);
		} else if (IPIValidacoes.isTRIB(item)) {
			return popularIPITrib(item);
		}

		return null;
	}

	public static TIpi popularIPINT(ItemProduto item) {

		TIpi ipi = new TIpi();
		ipi.setCEnq("999");

		IPINT ipint = new IPINT();
		ipint.setCST(item.getCstIpi());
		ipi.setIPINT(ipint);

		return ipi;
	}

	public static TIpi popularIPITrib(ItemProduto item) {

		TIpi ipi = new TIpi();
		ipi.setCEnq("999");

		IPITrib ipiTrib = new IPITrib();

		ipiTrib.setCST(item.getCstIpi());
		ipiTrib.setVBC(item.getValorTotal().add(item.getValorDesconto()).setScale(2).toPlainString());
		ipiTrib.setPIPI(item.getAliquotaIpi().toString());
		ipiTrib.setVIPI(item.getValorIpi().toString());

		ipi.setIPITrib(ipiTrib);

		return ipi;
	}
	
	
	
	public static TIpi popularIPI(ItemProdutoNFCe item) {

		if (IPIValidacoes.isNT(item)) {
			return popularIPINT(item);
		} else if (IPIValidacoes.isTRIB(item)) {
			return popularIPITrib(item);
		}

		return null;
	}

	public static TIpi popularIPINT(ItemProdutoNFCe item) {

		TIpi ipi = new TIpi();
		ipi.setCEnq("999");

		IPINT ipint = new IPINT();
		ipint.setCST(item.getCstIpi());
		ipi.setIPINT(ipint);

		return ipi;
	}

	public static TIpi popularIPITrib(ItemProdutoNFCe item) {

		TIpi ipi = new TIpi();
		ipi.setCEnq("999");

		IPITrib ipiTrib = new IPITrib();

		ipiTrib.setCST(item.getCstIpi());
		ipiTrib.setVBC(item.getValorTotal().add(item.getValorDesconto()).setScale(2).toPlainString());
		ipiTrib.setPIPI(item.getAliquotaIpi().toString());
		ipiTrib.setVIPI(item.getValorIpi().toString());

		ipi.setIPITrib(ipiTrib);

		return ipi;
	}

}
