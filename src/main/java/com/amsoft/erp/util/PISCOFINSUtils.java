package com.amsoft.erp.util;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;

public class PISCOFINSUtils {

	private static boolean isAliq(ItemProduto item) {
		return item.getCstPis().equals("01") || item.getCstPis().equals("02");
	}
	
	private static boolean isAliq(ItemProdutoNFCe item) {
		return item.getCstPis().equals("01") || item.getCstPis().equals("02");
	}

	private static boolean isNT(ItemProduto itemPedido) {
		return itemPedido.getCstPis().equals("04") || itemPedido.getCstPis().equals("05")
				|| itemPedido.getCstPis().equals("06")
				|| itemPedido.getCstPis().equals("07")
				|| itemPedido.getCstPis().equals("08")
				|| itemPedido.getCstPis().equals("09");
	}
	
	private static boolean isNT(ItemProdutoNFCe itemPedido) {
		return itemPedido.getCstPis().equals("04") || itemPedido.getCstPis().equals("05")
				|| itemPedido.getCstPis().equals("06")
				|| itemPedido.getCstPis().equals("07")
				|| itemPedido.getCstPis().equals("08")
				|| itemPedido.getCstPis().equals("09");
	}

	private static boolean isOutr(ItemProduto item) {
		return item.getCstPis().equals("49") || item.getCstPis().equals("50")
				|| item.getCstPis().equals("51") || item.getCstPis().equals("52")
				|| item.getCstPis().equals("53") || item.getCstPis().equals("54")
				|| item.getCstPis().equals("55") || item.getCstPis().equals("56")
				|| item.getCstPis().equals("60") || item.getCstPis().equals("61")
				|| item.getCstPis().equals("62") || item.getCstPis().equals("63")
				|| item.getCstPis().equals("64") || item.getCstPis().equals("65")
				|| item.getCstPis().equals("66") || item.getCstPis().equals("67")
				|| item.getCstPis().equals("70") || item.getCstPis().equals("71")
				|| item.getCstPis().equals("72") || item.getCstPis().equals("73")
				|| item.getCstPis().equals("74") || item.getCstPis().equals("75")
				|| item.getCstPis().equals("98") || item.getCstPis().equals("99");
	}

	private static boolean isOutr(ItemProdutoNFCe item) {
		return item.getCstPis().equals("49") || item.getCstPis().equals("50")
				|| item.getCstPis().equals("51") || item.getCstPis().equals("52")
				|| item.getCstPis().equals("53") || item.getCstPis().equals("54")
				|| item.getCstPis().equals("55") || item.getCstPis().equals("56")
				|| item.getCstPis().equals("60") || item.getCstPis().equals("61")
				|| item.getCstPis().equals("62") || item.getCstPis().equals("63")
				|| item.getCstPis().equals("64") || item.getCstPis().equals("65")
				|| item.getCstPis().equals("66") || item.getCstPis().equals("67")
				|| item.getCstPis().equals("70") || item.getCstPis().equals("71")
				|| item.getCstPis().equals("72") || item.getCstPis().equals("73")
				|| item.getCstPis().equals("74") || item.getCstPis().equals("75")
				|| item.getCstPis().equals("98") || item.getCstPis().equals("99");
	}
	
	public static COFINS popularCOFINS(ItemProduto item) {
		COFINS cofins = new COFINS();

		if (item.getCstPis() != null) {
			if (isOutr(item)) {
				cofins = popularCofinsOutr(item);
			} else if (isNT(item)) {
				cofins = popularCOFINSNT(item);
			} else if (isAliq(item)) {
				cofins = popularCofinsAliq(item);
			}
		}

		return cofins;
	}
	
	public static COFINS popularCOFINS(ItemProdutoNFCe item) {
		COFINS cofins = new COFINS();

		if (item.getCstPis() != null) {
			if (isOutr(item)) {
				cofins = popularCofinsOutr(item);
			} else if (isNT(item)) {
				cofins = popularCOFINSNT(item);
			} else if (isAliq(item)) {
				cofins = popularCofinsAliq(item);
			}
		}

		return cofins;
	}

	private static COFINS popularCofinsAliq(ItemProduto item) {
		COFINS cofins = new COFINS();

		COFINSAliq cofinsAliq = new COFINSAliq();

		cofinsAliq.setCST(item.getCstPis());
		cofinsAliq.setVBC(BigDecimal.ZERO.toString());
		cofinsAliq.setPCOFINS(item.getAliquotaPis().toString());
		cofinsAliq.setVCOFINS(item.getCofins().toString());

		cofins.setCOFINSAliq(cofinsAliq);

		return cofins;
	}
	
	private static COFINS popularCofinsAliq(ItemProdutoNFCe item) {
		COFINS cofins = new COFINS();

		COFINSAliq cofinsAliq = new COFINSAliq();

		cofinsAliq.setCST(item.getCstPis());
		cofinsAliq.setVBC(BigDecimal.ZERO.toString());
		cofinsAliq.setPCOFINS(item.getAliquotaPis().toString());
		cofinsAliq.setVCOFINS(item.getCofins().toString());

		cofins.setCOFINSAliq(cofinsAliq);

		return cofins;
	}

	private static COFINS popularCOFINSNT(ItemProduto item) {
		COFINS cofins = new COFINS();

		COFINSNT cofinsNT = new COFINSNT();

		cofinsNT.setCST(item.getCstPis());

		cofins.setCOFINSNT(cofinsNT);

		return cofins;
	}

	private static COFINS popularCOFINSNT(ItemProdutoNFCe item) {
		COFINS cofins = new COFINS();

		COFINSNT cofinsNT = new COFINSNT();

		cofinsNT.setCST(item.getCstPis());

		cofins.setCOFINSNT(cofinsNT);

		return cofins;
	}
	
	private static COFINS popularCofinsOutr(ItemProduto item) {
		COFINS cofins = new COFINS();

		COFINSOutr cofinsOutr = new COFINSOutr();

		cofinsOutr.setCST(item.getCstPis());
		cofinsOutr.setVBC(BigDecimal.ZERO.toString());
		cofinsOutr.setPCOFINS(item.getAliquotaPis().toString());
		cofinsOutr.setVCOFINS(item.getCofins().toString());

		cofins.setCOFINSOutr(cofinsOutr);

		return cofins;
	}

	private static COFINS popularCofinsOutr(ItemProdutoNFCe item) {
		COFINS cofins = new COFINS();

		COFINSOutr cofinsOutr = new COFINSOutr();

		cofinsOutr.setCST(item.getCstPis());
		cofinsOutr.setVBC(BigDecimal.ZERO.toString());
		cofinsOutr.setPCOFINS(item.getAliquotaPis().toString());
		cofinsOutr.setVCOFINS(item.getCofins().toString());

		cofins.setCOFINSOutr(cofinsOutr);

		return cofins;
	}
	
	public static PIS popularPIS(ItemProduto item) {
		PIS pis = new PIS();

		if (item.getCstPis() != null) {
			if (isOutr(item)) {
				pis = popularPisOutr(item);
			} else if (isNT(item)) {
				pis = popularPISNT(item);
			} else if (isAliq(item)) {
				pis = popularPISAliq(item);
			}
		}

		return pis;
	}
	
	public static PIS popularPIS(ItemProdutoNFCe item) {
		PIS pis = new PIS();

		if (item.getCstPis() != null) {
			if (isOutr(item)) {
				pis = popularPisOutr(item);
			} else if (isNT(item)) {
				pis = popularPISNT(item);
			} else if (isAliq(item)) {
				pis = popularPISAliq(item);
			}
		}

		return pis;
	}

	private static PIS popularPISAliq(ItemProduto item) {
		PIS pis = new PIS();

		PISAliq pisAliq = new PISAliq();

		pisAliq.setCST(item.getCstPis());
		pisAliq.setVBC(BigDecimal.ZERO.toString());
		pisAliq.setPPIS(item.getAliquotaPis().toString());
		pisAliq.setVPIS(item.getPis().toString());

		pis.setPISAliq(pisAliq);

		return pis;
	}

	private static PIS popularPISAliq(ItemProdutoNFCe item) {
		PIS pis = new PIS();

		PISAliq pisAliq = new PISAliq();

		pisAliq.setCST(item.getCstPis());
		pisAliq.setVBC(BigDecimal.ZERO.toString());
		pisAliq.setPPIS(item.getAliquotaPis().toString());
		pisAliq.setVPIS(item.getPis().toString());

		pis.setPISAliq(pisAliq);

		return pis;
	}
	
	private static PIS popularPISNT(ItemProduto item) {
		PIS pis = new PIS();

		PISNT pisNT = new PISNT();
		pisNT.setCST(item.getCstPis());
		pis.setPISNT(pisNT);

		return pis;
	}
	private static PIS popularPISNT(ItemProdutoNFCe item) {
		PIS pis = new PIS();

		PISNT pisNT = new PISNT();
		pisNT.setCST(item.getCstPis());
		pis.setPISNT(pisNT);

		return pis;
	}
	
	private static PIS popularPisOutr(ItemProduto item) {
		PIS pis = new PIS();

		PISOutr pisOutr = new PISOutr();

		pisOutr.setCST(item.getCstPis());
		pisOutr.setVBC(BigDecimal.ZERO.toString());
		pisOutr.setPPIS(item.getAliquotaPis().toString());
		pisOutr.setVPIS(item.getPis().toString());
		pis.setPISOutr(pisOutr);

		return pis;
	}
	
	private static PIS popularPisOutr(ItemProdutoNFCe item) {
		PIS pis = new PIS();

		PISOutr pisOutr = new PISOutr();

		pisOutr.setCST(item.getCstPis());
		pisOutr.setVBC(BigDecimal.ZERO.toString());
		pisOutr.setPPIS(item.getAliquotaPis().toString());
		pisOutr.setVPIS(item.getPis().toString());
		pis.setPISOutr(pisOutr);

		return pis;
	}

}
