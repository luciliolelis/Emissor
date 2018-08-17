package com.amsoft.erp.model.nfe.icms;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.ipi.IPICalculos;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;
import com.amsoft.erp.util.AmsoftUtils;

public class ICMS {

	public static BigDecimal getBaseIcms(ItemProduto itemProduto) {

		try {

			if (!ICMSValidacoes.isIcms(itemProduto)) {
				return BigDecimal.ZERO;
			}

			BigDecimal base = itemProduto.getValorTotalProdutosBaseIcms();

			if (IPIValidacoes.somarIpiBcIcms(itemProduto)) {
				base = base.add(IPICalculos.getValorIpi(itemProduto));
			}

			if (ICMSValidacoes.isReducaoBaseCalculoIcms(itemProduto)) {
				BigDecimal reducao = itemProduto.getReducaoBaseCalculoIcms().divide(new BigDecimal(100));
				reducao = base.multiply(reducao).setScale(2, RoundingMode.HALF_UP);
				return base.subtract(reducao);
			}

			return base.setScale(2, RoundingMode.HALF_UP);

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getValorIcms(ItemProduto itemProduto) {

		try {

			if (ICMSValidacoes.isIcms(itemProduto)) {
				BigDecimal aliquota = itemProduto.getAliquotaIcms().divide(new BigDecimal(100));
				BigDecimal icms = getBaseIcms(itemProduto).multiply(aliquota);
				return icms.setScale(2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
