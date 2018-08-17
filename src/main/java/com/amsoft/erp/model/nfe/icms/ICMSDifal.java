package com.amsoft.erp.model.nfe.icms;


import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.util.AmsoftUtils;

public class ICMSDifal {

	public static BigDecimal getVBCUFDest(ItemProduto itemProduto) {

		try {

			if (AmsoftUtils.isSimplesNacional(itemProduto)) {
				return BigDecimal.ZERO;
			}

			BigDecimal base = BigDecimal.ZERO;
			base = ICMS.getBaseIcms(itemProduto);
			return base;

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	// Parte UF Destino = Valor do DIFAL * (%Destino / 100)
	public static BigDecimal getVICMSUFDest(ItemProduto itemProduto) {

		try {

			BigDecimal difal = getDIFAL(itemProduto);
			BigDecimal destino = new BigDecimal(80).divide(new BigDecimal(100));

			BigDecimal icms = difal.multiply(destino).setScale(2, RoundingMode.HALF_UP);

			return icms;

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getTotalVICMSUFDest(Nfe nfe) {

		try {

			BigDecimal total = BigDecimal.ZERO;

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {
					total = total.add(getVICMSUFDest(item));
				}
			}

			return total;

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	// Parte UF Origem = Valor do DIFAL * (%Origem / 100)
	public static BigDecimal getICMSUFRemet(ItemProduto itemProduto) {

		try {

			BigDecimal difal = getDIFAL(itemProduto);
			BigDecimal fcp = new BigDecimal(20).divide(new BigDecimal(100));
			BigDecimal ret = difal.multiply(fcp).setScale(2, RoundingMode.HALF_UP);;

			return ret;

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	public static BigDecimal getTotalVICMSUFRemet(Nfe nfe) {

		try {

			BigDecimal total = BigDecimal.ZERO;

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {
					total = total.add(getICMSUFRemet(item));
				}
			}

			return total;

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

	// DIFAL = Base do ICMSEntrada * ((%Alíquota do ICMSEntrada Intra – %Alíquota do ICMSEntrada
	// Inter) / 100)
	public static BigDecimal getDIFAL(ItemProduto itemProduto) {

		try {

			BigDecimal base = itemProduto.getValorTotalProdutosBaseIcms();
			BigDecimal aliquotaIntra = itemProduto.getAliquotaIcms().divide(new BigDecimal(100));
			BigDecimal aliquotaInter = itemProduto.getAliquotaIcms(); //AQUI TEM QUE ADD A ALIQUOTA INTERNA


			aliquotaInter = aliquotaInter.divide(new BigDecimal(100));

			BigDecimal dif = AmsoftUtils.dif(aliquotaInter, aliquotaIntra).abs();

			BigDecimal icms = base.multiply(dif);

			return icms.setScale(2, RoundingMode.HALF_UP);

		} catch (Exception e) {
			AmsoftUtils.error(e.getCause() + " - " + e.getMessage());
		}

		return BigDecimal.ZERO;
	}

}
