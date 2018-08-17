package com.amsoft.erp.model.nfe.icms;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.AmsoftUtils;

public class ICMSDiferencial {

	
	public static BigDecimal getBaseIcmsStDiferencial(ItemProduto itemProduto) {

		try {

			if (AmsoftUtils.isSimplesNacional(itemProduto)) {
				return BigDecimal.ZERO;
			}

			if (ICMSValidacoes.isIcms(itemProduto) && ICMSValidacoes.isIcmsSt(itemProduto)) {

				BigDecimal aliquota = itemProduto.getAliquotaIcmsSt().divide(new BigDecimal(100));
				BigDecimal bcst = ICMSST.getBaseIcmsSt(itemProduto);
				BigDecimal icms = ICMS.getValorIcms(itemProduto);
				BigDecimal fator = BigDecimal.ONE.subtract(aliquota);
				
				BigDecimal icmsst = bcst.subtract(icms);
				
				icmsst = icmsst.divide(fator, 2, RoundingMode.HALF_UP);

				return icmsst;
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	
	public static BigDecimal getValorIcmsStDiferencial(ItemProduto itemProduto) {

		try {

			if (AmsoftUtils.isSimplesNacional(itemProduto)) {
				return BigDecimal.ZERO;
			}

			if (ICMSValidacoes.isIcms(itemProduto) && ICMSValidacoes.isIcmsSt(itemProduto)) {

				BigDecimal aliquota = itemProduto.getAliquotaIcmsSt().divide(new BigDecimal(100));
				BigDecimal bcst = ICMSST.getBaseIcmsSt(itemProduto);
				BigDecimal icms = ICMS.getValorIcms(itemProduto);
				BigDecimal fator = BigDecimal.ONE.subtract(aliquota);
				
				BigDecimal icmsst = bcst.subtract(icms);
				
				icmsst = icmsst.divide(fator, 2, RoundingMode.HALF_UP);
				icmsst = icmsst.multiply(aliquota);
				icmsst = icmsst.subtract(icms).setScale(2, RoundingMode.HALF_UP);

				return icmsst;
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
