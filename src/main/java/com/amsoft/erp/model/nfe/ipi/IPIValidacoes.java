package com.amsoft.erp.model.nfe.ipi;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class IPIValidacoes {

	public static boolean somarIpiBcIcms(ItemProduto itemProduto) {
		return itemProduto.getSomarIpiBcIcms() != null && itemProduto.getSomarIpiBcIcms() == true;
	}

	public static boolean somarIpiBcIcmsSt(ItemProduto itemProduto) {
		return itemProduto.getSomarIpiBcIcmsSt() != null && itemProduto.getSomarIpiBcIcmsSt() == true;
	}

	public static boolean isIpi(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null
				&& AmsoftUtils.isRegimeNormal(itemProduto.getNfe().getEmpresa().getRegimeTributario());
	}

	public static boolean isNT(ItemProduto itemProduto) {
		return itemProduto.getCstIpi().equals("01") || itemProduto.getCstIpi().equals("02")
				|| itemProduto.getCstIpi().equals("03") || itemProduto.getCstIpi().equals("04")
				|| itemProduto.getCstIpi().equals("51") || itemProduto.getCstIpi().equals("52")
				|| itemProduto.getCstIpi().equals("53") || itemProduto.getCstIpi().equals("54")
				|| itemProduto.getCstIpi().equals("55");
	}

	public static boolean isTRIB(ItemProduto itemProduto) {
		return itemProduto.getCstIpi().equals("00") || itemProduto.getCstIpi().equals("49")
				|| itemProduto.getCstIpi().equals("50") || itemProduto.getCstIpi().equals("99");
	}
	
	
	public static boolean isIpi(ItemProdutoNFCe itemProduto) {
		return itemProduto.getProduto() != null
				&& AmsoftUtils.isRegimeNormal(itemProduto.getNfce().getEmpresa().getRegimeTributario());
	}
	
	public static boolean isNT(ItemProdutoNFCe itemProduto) {
		return itemProduto.getCstIpi().equals("01") || itemProduto.getCstIpi().equals("02")
				|| itemProduto.getCstIpi().equals("03") || itemProduto.getCstIpi().equals("04")
				|| itemProduto.getCstIpi().equals("51") || itemProduto.getCstIpi().equals("52")
//				|| itemProduto.getCstIpi().equals("53") || itemProduto.getCstIpi().equals("54")
				|| itemProduto.getCstIpi().equals("55");
	}

	public static boolean isTRIB(ItemProdutoNFCe itemProduto) {
		return itemProduto.getCstIpi().equals("00") || itemProduto.getCstIpi().equals("49")
				|| itemProduto.getCstIpi().equals("50") || itemProduto.getCstIpi().equals("99");
	}
}
