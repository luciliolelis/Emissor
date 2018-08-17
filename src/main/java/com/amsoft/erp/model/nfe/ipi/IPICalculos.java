package com.amsoft.erp.model.nfe.ipi;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class IPICalculos {

	public static BigDecimal getValorIpi(ItemProduto itemProduto) {

		try {
			if (IPIValidacoes.isIpi(itemProduto) && IPIValidacoes.isTRIB(itemProduto)) {

				BigDecimal aliquota = itemProduto.getAliquotaIpi().divide(new BigDecimal(100));
				BigDecimal produto = itemProduto.getValorTotalProdutosBaseIcms();
				
				return produto.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
