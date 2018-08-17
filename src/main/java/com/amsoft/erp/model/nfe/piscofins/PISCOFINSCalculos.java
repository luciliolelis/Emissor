package com.amsoft.erp.model.nfe.piscofins;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class PISCOFINSCalculos {

	public static BigDecimal getValorPis(ItemProduto itemProduto) {

		try {

			if (PISCOFINSValidacoes.isPis(itemProduto)) {
				return itemProduto.getValorTotal().multiply(itemProduto.getAliquotaPis()).divide(new BigDecimal(100))

						.setScale(2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getValorCofins(ItemProduto itemProduto) {

		try {

			if (PISCOFINSValidacoes.isCofins(itemProduto)) {
				return itemProduto.getValorTotal().multiply(itemProduto.getAliquotaCofins()).divide(new BigDecimal(100))
						.setScale(2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getPis(ItemProduto itemProduto) {

		try {
			if (PISCOFINSValidacoes.isPis(itemProduto)) {

				return itemProduto.getValorTotal().multiply(itemProduto.getAliquotaPis()).divide(new BigDecimal(100))
						.setScale(2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getCofins(ItemProduto itemProduto) {

		try {
			if (PISCOFINSValidacoes.isCofins(itemProduto)) {
				return itemProduto.getValorTotal().multiply(itemProduto.getAliquotaCofins()).divide(new BigDecimal(100))
						.setScale(2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
