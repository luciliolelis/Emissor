package com.amsoft.erp.util;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;

public class ICMSSNUtils {

	public static ICMSSN101 getICMSST101(ItemProduto item) {

		ICMSSN101 icmsSN101 = new ICMSSN101();
		icmsSN101.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN101.setCSOSN(item.getCsosn());
		icmsSN101.setPCredSN("0");
		icmsSN101.setVCredICMSSN("0");
		return icmsSN101;
	}

	public static ICMSSN102 getICMSST102(ItemProduto item) {

		ICMSSN102 icmsSN102 = new ICMSSN102();
		icmsSN102.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN102.setCSOSN(item.getCsosn());
		return icmsSN102;
	}

	public static ICMSSN201 getICMSST201(ItemProduto item) {
		ICMSSN201 icmsSN201 = new ICMSSN201();
		icmsSN201.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN201.setCSOSN(item.getCsosn());
		icmsSN201.setModBCST(getModBCST(item));
		icmsSN201.setPMVAST(item.getMva().toString());
		icmsSN201.setPRedBCST(item.getReducaoBaseCalculoIcmsSt().toString());
		icmsSN201.setVBCST(item.getBaseIcmsSt().toString());
		icmsSN201.setPICMSST(item.getAliquotaIcmsSt().toString());
		icmsSN201.setVICMSST(item.getValorIcmsSt().toString());
		// icmsSN201.setPFCPST("0.00");
		// icmsSN201.setVBCFCPST("0");
		// icmsSN201.setVFCPST("0");
		icmsSN201.setPCredSN("0");
		icmsSN201.setVCredICMSSN("0");

		return icmsSN201;
	}

	public static ICMSSN202 getICMSST202(ItemProduto item) {
		ICMSSN202 icmsSN202 = new ICMSSN202();
		icmsSN202.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN202.setCSOSN(item.getCsosn());
		icmsSN202.setModBCST(getModBCST(item));
		icmsSN202.setPMVAST(item.getMva().toString());
		icmsSN202.setPRedBCST(item.getReducaoBaseCalculoIcmsSt().toString());
		icmsSN202.setVBCST(item.getBaseIcmsSt().toString());
		icmsSN202.setPICMSST(item.getAliquotaIcmsSt().toString());
		icmsSN202.setVICMSST(item.getValorIcmsSt().toString());

		return icmsSN202;
	}

	public static ICMSSN500 getICMSST500(ItemProduto item) {

		ICMSSN500 icmsSN500 = new ICMSSN500();
		icmsSN500.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN500.setCSOSN(item.getCsosn());

		return icmsSN500;
	}

	public static ICMSSN900 getICMSST900(ItemProduto item) {

		ICMSSN900 icmsSN900 = new ICMSSN900();
		icmsSN900.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN900.setCSOSN(item.getCsosn());

		return icmsSN900;
	}

	public static String getModBCST(ItemProduto item) {
		// if (isNotBigDecimalZeroOuNullo(item.getPauta())) {
		return "4";
		// } else {
		// return "5";
		// }
	}

	public static boolean isICMSSN101(ItemProduto item) {
		return item.getCsosn().equals("101");
	}

	public static boolean isICMSSN102(ItemProduto item) {
		return item.getCsosn().equals("102") || item.getCsosn().equals("103") || item.getCsosn().equals("300")
				|| item.getCsosn().equals("400");
	}

	public static boolean isICMSSN201(ItemProduto item) {
		return item.getCsosn().equals("201");
	}

	public static boolean isICMSSN202(ItemProduto item) {
		return item.getCsosn().equals("202") || item.getCsosn().equals("203");
	}

	public static boolean isICMSSN500(ItemProduto item) {
		return item.getCsosn().equals("500");
	}

	public static boolean isICMSSN900(ItemProduto item) {
		return item.getCsosn().equals("900");
	}

	public static ICMS popularIcmsSN(ItemProduto item) {
		ICMS icms = new ICMS();

		if (ICMSSNUtils.isICMSSN101(item)) {
			icms.setICMSSN101(ICMSSNUtils.getICMSST101(item));
		} else if (ICMSSNUtils.isICMSSN102(item)) {
			icms.setICMSSN102(ICMSSNUtils.getICMSST102(item));
		} else if (ICMSSNUtils.isICMSSN201(item)) {
			icms.setICMSSN201(ICMSSNUtils.getICMSST201(item));
		} else if (ICMSSNUtils.isICMSSN202(item)) {
			icms.setICMSSN202(ICMSSNUtils.getICMSST202(item));
		} else if (ICMSSNUtils.isICMSSN500(item)) {
			icms.setICMSSN500(ICMSSNUtils.getICMSST500(item));
		} else if (ICMSSNUtils.isICMSSN900(item)) {
			icms.setICMSSN900(ICMSSNUtils.getICMSST900(item));
		}

		return icms;
	}

	public static boolean isICMSSN101(ItemProdutoNFCe item) {
		return item.getCsosn().equals("101");
	}

	public static boolean isICMSSN102(ItemProdutoNFCe item) {
		return item.getCsosn().equals("102") || item.getCsosn().equals("103") || item.getCsosn().equals("300")
				|| item.getCsosn().equals("400");
	}

	public static boolean isICMSSN201(ItemProdutoNFCe item) {
		return item.getCsosn().equals("201");
	}

	public static boolean isICMSSN202(ItemProdutoNFCe item) {
		return item.getCsosn().equals("202") || item.getCsosn().equals("203");
	}

	public static boolean isICMSSN500(ItemProdutoNFCe item) {
		return item.getCsosn().equals("500");
	}

	public static boolean isICMSSN900(ItemProdutoNFCe item) {
		return item.getCsosn().equals("900");
	}

	public static ICMS popularIcmsSN(ItemProdutoNFCe item) {
		ICMS icms = new ICMS();

		if (ICMSSNUtils.isICMSSN101(item)) {
			icms.setICMSSN101(ICMSSNUtils.getICMSST101(item));
		} else if (ICMSSNUtils.isICMSSN102(item)) {
			icms.setICMSSN102(ICMSSNUtils.getICMSST102(item));
		} else if (ICMSSNUtils.isICMSSN201(item)) {
			icms.setICMSSN201(ICMSSNUtils.getICMSST201(item));
		} else if (ICMSSNUtils.isICMSSN202(item)) {
			icms.setICMSSN202(ICMSSNUtils.getICMSST202(item));
		} else if (ICMSSNUtils.isICMSSN500(item)) {
			icms.setICMSSN500(ICMSSNUtils.getICMSST500(item));
		} else if (ICMSSNUtils.isICMSSN900(item)) {
			icms.setICMSSN900(ICMSSNUtils.getICMSST900(item));
		}

		return icms;
	}

	public static ICMSSN101 getICMSST101(ItemProdutoNFCe item) {

		ICMSSN101 icmsSN101 = new ICMSSN101();
		icmsSN101.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN101.setCSOSN(item.getCsosn());
		icmsSN101.setPCredSN("0");
		icmsSN101.setVCredICMSSN("0");
		return icmsSN101;
	}

	public static ICMSSN102 getICMSST102(ItemProdutoNFCe item) {

		ICMSSN102 icmsSN102 = new ICMSSN102();
		icmsSN102.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN102.setCSOSN(item.getCsosn());
		return icmsSN102;
	}

	public static ICMSSN201 getICMSST201(ItemProdutoNFCe item) {
		ICMSSN201 icmsSN201 = new ICMSSN201();
		icmsSN201.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN201.setCSOSN(item.getCsosn());
		icmsSN201.setModBCST("0,00");
		icmsSN201.setPMVAST(item.getMva().toString());
		icmsSN201.setPRedBCST("0,00");
		icmsSN201.setVBCST("0,00");
		icmsSN201.setPICMSST("0,00");
		icmsSN201.setVICMSST("0,00");
		icmsSN201.setPFCPST("0.00");
		icmsSN201.setVBCFCPST("0");
		icmsSN201.setVFCPST("0");
		icmsSN201.setPCredSN("0");
		icmsSN201.setVCredICMSSN("0");

		return icmsSN201;
	}

	public static ICMSSN202 getICMSST202(ItemProdutoNFCe item) {
		ICMSSN202 icmsSN202 = new ICMSSN202();
		icmsSN202.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN202.setCSOSN(item.getCsosn());
		icmsSN202.setModBCST("0,00");
		icmsSN202.setPMVAST(item.getMva().toString());
		icmsSN202.setPRedBCST("0,00");
		icmsSN202.setVBCST("0,00");
		icmsSN202.setPICMSST("0,00");
		icmsSN202.setVICMSST("0,00");

		return icmsSN202;
	}

	public static ICMSSN500 getICMSST500(ItemProdutoNFCe item) {

		ICMSSN500 icmsSN500 = new ICMSSN500();
		icmsSN500.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN500.setCSOSN(item.getCsosn());

		return icmsSN500;
	}

	public static ICMSSN900 getICMSST900(ItemProdutoNFCe item) {

		ICMSSN900 icmsSN900 = new ICMSSN900();
		icmsSN900.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN900.setCSOSN(item.getCsosn());

		return icmsSN900;
	}
}
