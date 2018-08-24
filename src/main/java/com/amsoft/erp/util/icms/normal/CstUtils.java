package com.amsoft.erp.util.icms.normal;

import com.amsoft.erp.model.nfe.ItemProduto;

public class CstUtils {

	public static boolean isICMS00(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("00");
	}

	public static boolean isICMS10(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("10");
	}

	public static boolean isICMS20(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("20");
	}

	public static boolean isICMS30(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("30");
	}

	public static boolean isICMS40(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("40") || itemProduto.getCstIcms().equals("41")
				|| itemProduto.getCstIcms().equals("50");
	}

	public static boolean isICMS51(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("51");
	}

	public static boolean isICMS60(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("60");
	}

	public static boolean isICMS70(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("70");
	}

	public static boolean isICMS90(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("90");
	}

	public static boolean isICMSPart(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("xx");
	}

	public static boolean isICMSST(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("xx");
	}

}
