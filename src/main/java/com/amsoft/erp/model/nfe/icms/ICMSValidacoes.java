package com.amsoft.erp.model.nfe.icms;


import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.util.AmsoftUtils;

public class ICMSValidacoes {

	public static boolean isNotIsento(ItemProduto itemProduto) {
		return !isIsento(itemProduto);
	}

	public static boolean isIsento(ItemProduto itemProduto) {

		if (AmsoftUtils.isSimplesNacional(itemProduto)) {

		} else {
			if (itemProduto.getCstIcms().equals("40")) {
				return true;
			} else if (itemProduto.getCstIcms().equals("41")) {
				return true;
			} else if (itemProduto.getCstIcms().equals("50")) {
				return true;
			} else if (itemProduto.getCstIcms().equals("60")) {
				return true;
			}
		}

		return false;
	}

	public static boolean isIcms(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null && itemProduto.getAliquotaIcms() != null
				&& AmsoftUtils.isNotZero(itemProduto.getAliquotaIcms());
	}

	public static boolean isIcmsSt(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null && itemProduto.getAliquotaIcmsSt() != null
				&& AmsoftUtils.isNotZero(itemProduto.getAliquotaIcmsSt());
	}

	public static boolean isReducaoBaseCalculoIcms(ItemProduto itemProduto) {

		try {
			if (AmsoftUtils.isSimplesNacional(itemProduto)) {
				return isCsosnItem(itemProduto) && isReducaoIcms(itemProduto)
						&& itemProduto.getCsosn().equals("201")
						|| itemProduto.getCsosn().equals("202")
						|| itemProduto.getCsosn().equals("900");
			} else {
				return isCstIcmsItem(itemProduto) && isReducaoIcms(itemProduto)
						&& itemProduto.getCstIcms().equals("10")
						|| itemProduto.getCstIcms().equals("20")
						|| itemProduto.getCstIcms().equals("51")
						|| itemProduto.getCstIcms().equals("70")
						|| itemProduto.getCstIcms().equals("90");
			}
		} catch (Exception e) {
			AmsoftUtils.error("isReducaoBaseCalculoIcms");
		}

		return false;
	}

	private static boolean isCsosnItem(ItemProduto itemProduto) {
		try {
			return itemProduto.getCsosn() != null;
		} catch (Exception e) {
			AmsoftUtils.error("isCsosnItem");
		}
		return false;
	}

	private static boolean isCstIcmsItem(ItemProduto itemProduto) {
		return itemProduto.getCstIcms() != null;
	}

	private static boolean isReducaoIcms(ItemProduto itemProduto) {

		try {
			return itemProduto.getProduto() != null && itemProduto.getReducaoBaseCalculoIcms() != null
					&& AmsoftUtils.isNotZero(itemProduto.getReducaoBaseCalculoIcms());
		} catch (Exception e) {
			AmsoftUtils.error("temReducaoIcms");
		}

		return false;
	}

	public static boolean isReducaoBaseCalculoIcmsSt(ItemProduto itemProduto) {
		if (AmsoftUtils.isSimplesNacional(itemProduto)) {
			return isCsosnItem(itemProduto) && isReducaoIcmsSt(itemProduto)
					&& itemProduto.getCsosn().equals("201") || itemProduto.getCsosn().equals("202")
					|| itemProduto.getCsosn().equals("900");
		} else {
			return isCstIcmsItem(itemProduto) && isReducaoIcmsSt(itemProduto)
					&& itemProduto.getCstIcms().equals("10") || itemProduto.getCstIcms().equals("70")
					|| itemProduto.getCstIcms().equals("90");
		}
	}

	private static boolean isReducaoIcmsSt(ItemProduto itemProduto) {
		return itemProduto.getProduto() != null && itemProduto.getReducaoBaseCalculoIcmsSt() != null
				&& AmsoftUtils.isNotZero(itemProduto.getReducaoBaseCalculoIcmsSt());
	}

	public static boolean isDiferimentoIcms(ItemProduto itemProduto) {
		return isCstIcmsItem(itemProduto) && itemProduto.getCstIcms().equals("51");
	}

	public static boolean isEnderecoEntrega(Nfe nfe) {
		return nfe.getEnderecoEntrega() != null;
	}

	public static boolean isInterestadual(Nfe nfe) {

		try {
			return isEmitente(nfe) && isEnderecoEntrega(nfe)
					&& !nfe.getEmpresa().getUf().equals(nfe.getEnderecoEntrega().getUf());
		} catch (Exception e) {
			AmsoftUtils.error("isInterestadual");
		}

		return false;
	}

	public static boolean isEmitente(Nfe nfe) {
		return nfe.getEmpresa() != null;
	}

	public static int isDiferencalAliquotaIcms(ItemProduto itemProduto) {

		try {
			return itemProduto.getAliquotaIcms().compareTo(itemProduto.getAliquotaIcmsSt());
		} catch (Exception e) {
			AmsoftUtils.error("isDiferencalAliquotaIcms");
		}

		return 0;
	}
}
