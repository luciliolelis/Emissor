package com.amsoft.erp.model.nfe.mva;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class MVACalculo {

	public static BigDecimal getMVA(ItemProduto itemProduto) {

		try {
			
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
