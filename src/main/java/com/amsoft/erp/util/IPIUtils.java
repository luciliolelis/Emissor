package com.amsoft.erp.util;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;
import com.amsoft.erp.util.icms.CalculosUtils;
import com.chronos.calc.resultados.IResultadoCalculoIpi;

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
		ipiTrib.setPIPI(item.getAliquotaIpi().toString());
		
		IResultadoCalculoIpi resultado = CalculosUtils.calcularIPI(item);
		ipiTrib.setVBC(resultado.getBaseCalculo().toPlainString());
		ipiTrib.setVIPI(resultado.getValor().setScale(2).toString());
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
		ipiTrib.setPIPI(item.getAliquotaIpi().toString());
		
		IResultadoCalculoIpi resultado = CalculosUtils.calcularIPI(item);
		ipiTrib.setVBC(resultado.getBaseCalculo().toPlainString());
		ipiTrib.setVIPI(resultado.getValor().setScale(2).toString());
		ipi.setIPITrib(ipiTrib);

		return ipi;
	}

}
