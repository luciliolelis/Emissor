package com.amsoft.erp.model.nfe.piscofins;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class PISCOFINSValidacoes {

	public static boolean isPis(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null && itemProduto.getAliquotaPis() != null
				&& AmsoftUtils.isNotZero(itemProduto.getAliquotaPis());
	}

	public static boolean isCofins(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null && itemProduto.getAliquotaCofins() != null
				&& AmsoftUtils.isNotZero(itemProduto.getAliquotaCofins());
	}
}
