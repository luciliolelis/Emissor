package com.amsoft.erp.model.nfe.fcp;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.icms.ICMSValidacoes;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.IDUtils;

public class FCPValidacoes {

	public static boolean isFcp(ItemProduto itemProduto) {

		return itemProduto.getProduto() != null && ICMSValidacoes.isIcms(itemProduto)
				&& AmsoftUtils.isRegimeNormal(itemProduto.getNfe().getEmpresa().getRegimeTributario());
	}

	public static boolean isFcpInterna(ItemProduto itemProduto) {

		boolean interna = IDUtils.getDestino(itemProduto.getNfe()).equals("1");
		boolean naoContribuinte = IDUtils.getIndicadorIEDestinatario(itemProduto.getNfe()).equals("9");
		boolean produto = itemProduto.getProduto() != null;
		boolean icms = ICMSValidacoes.isIcms(itemProduto);
		boolean regimeNormal = AmsoftUtils.isRegimeNormal(itemProduto.getNfe().getEmpresa().getRegimeTributario());

		return interna && naoContribuinte && produto && regimeNormal && icms;
	}

	/**
	 * operação interestadual (tag:idDest=2); Consumidor Final (tag:indFinal=1);
	 * não contribuinte (tag: indIEDest=9);
	 * 
	 * @param itemProduto
	 * @return
	 */
	public static boolean isFCPUFDest(ItemProduto itemProduto) {
		boolean interestadual = IDUtils.getDestino(itemProduto.getNfe()).equals("2");
		boolean naoContribuinte = IDUtils.getIndicadorIEDestinatario(itemProduto.getNfe()).equals("9");

		boolean cst = false;

		if (AmsoftUtils.isRegimeNormal(itemProduto.getNfe().getEmpresa().getRegimeTributario())) {
			cst = isCSTFCPUFDest(itemProduto);
		}

		return interestadual && naoContribuinte && cst;
	}

	private static boolean isCSTFCPUFDest(ItemProduto itemProduto) {

		return itemProduto.getCstIcms().equals("00") || itemProduto.getCstIcms().equals("10")
				|| itemProduto.getCstIcms().equals("20") || itemProduto.getCstIcms().equals("30")
				|| itemProduto.getCstIcms().equals("51") || itemProduto.getCstIcms().equals("60")
				|| itemProduto.getCstIcms().equals("70") || itemProduto.getCstIcms().equals("90");
	}

	public static boolean isNotFCPUFDest(ItemProduto itemProduto) {
		return !isFCPUFDest(itemProduto);
	}

}
