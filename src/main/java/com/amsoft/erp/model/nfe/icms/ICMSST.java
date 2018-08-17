package com.amsoft.erp.model.nfe.icms;


import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.ipi.IPICalculos;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;
import com.amsoft.erp.util.AmsoftUtils;

public class ICMSST {

	public static BigDecimal getBaseIcmsSt(ItemProduto itemProduto) {

		try {

			BigDecimal bcicmsst = BigDecimal.ZERO;

			BigDecimal produto = itemProduto.getValorTotalProdutosBaseIcms();

			if (IPIValidacoes.somarIpiBcIcmsSt(itemProduto)) {
				produto = produto.add(IPICalculos.getValorIpi(itemProduto));
			}

			BigDecimal pMva = itemProduto.getMva().divide(new BigDecimal(100));

			if (AmsoftUtils.isMaiorQZero(pMva)) {
				bcicmsst = produto.multiply(pMva).setScale(2, RoundingMode.HALF_UP);
			}

			return bcicmsst.add(produto).setScale(2, RoundingMode.HALF_UP);

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getValorIcmsSt(ItemProduto itemProduto) {

		try {

			if (ICMSValidacoes.isIcmsSt(itemProduto)) {

				BigDecimal bcst = getBaseIcmsSt(itemProduto);
				BigDecimal icms = ICMS.getValorIcms(itemProduto);
				BigDecimal icmsst = BigDecimal.ZERO;
				BigDecimal aliquota = itemProduto.getAliquotaIcmsSt().divide(new BigDecimal(100));

				icmsst = bcst.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);

				icmsst = icmsst.subtract(icms);

				return icmsst.setScale(2);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
