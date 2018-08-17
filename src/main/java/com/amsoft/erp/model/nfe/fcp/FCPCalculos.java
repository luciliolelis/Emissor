package com.amsoft.erp.model.nfe.fcp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.IDUtils;

public class FCPCalculos {

	public static BigDecimal getValorFcp(ItemProduto itemProduto) {

		try {
			if (FCPValidacoes.isFcp(itemProduto)) {

				BigDecimal aliquota = new BigDecimal(2).divide(new BigDecimal(100));

				BigDecimal produto;

				if (IDUtils.getIndicadorIEDestinatario(itemProduto.getNfe()).equals("2")
						|| itemProduto.getCstIcms().equals("10") || itemProduto.getCstIcms().equals("20")
						|| itemProduto.getCstIcms().equals("51")) {
					produto = itemProduto.getValorIcms();
				} else {
					produto = itemProduto.getValorTotalProdutosBaseIcms();
				}

				return produto.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getVFCPUFDest(ItemProduto itemProduto) {

		try {
			if (FCPValidacoes.isFcp(itemProduto)) {

				BigDecimal aliquota = new BigDecimal(2).divide(new BigDecimal(100));
				BigDecimal produto = itemProduto.getValorTotalProdutosBaseIcms();

				return produto.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
			}
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getTotalVFCPUFDest(Nfe nfe) {

		try {
			BigDecimal total = BigDecimal.ZERO;

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {
					total = total.add(getVFCPUFDest(item));
				}
			}

			return total;
		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}
}
