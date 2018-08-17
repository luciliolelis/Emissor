package com.amsoft.erp.util;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.fcp.FCPCalculos;
import com.amsoft.erp.model.nfe.fcp.FCPValidacoes;
import com.amsoft.erp.model.nfe.icms.ICMSDifal;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSST;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMSUFDest;

public class ICMSUtils {

	public static ICMS00 getICMS00(ItemProduto itemProduto) {

		ICMS00 icms = new ICMS00();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());

		icms.setCST(itemProduto.getCstIcms());

		icms.setModBC("3");

		icms.setPICMS("0");
		icms.setVBC("0.00");
		icms.setVICMS("0.00");

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {

			icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
			icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
			icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());

			// icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
			// icms.setVFCP(itemProduto.getValorFcp().toPlainString());
		}

		return icms;
	}

	public static ICMS10 getICMS10(ItemProduto itemProduto) {

		ICMS10 icms = new ICMS10();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST("0.00");

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {
			// icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
			// icms.setVFCP(itemProduto.getValorFcp().toPlainString());
			icms.setVBCFCP(itemProduto.getValorIcms().setScale(2).toPlainString());
		}

		icms.setPFCPST(null);
		icms.setVFCPST(null);
		icms.setVBCFCPST(null);

		return icms;
	}

	public static ICMS20 getICMS20(ItemProduto itemProduto) {

		ICMS20 icms = new ICMS20();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setMotDesICMS(null);
		icms.setVICMSDeson(null);

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {
//			icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
//			icms.setVFCP(itemProduto.getValorFcp().toPlainString());
			icms.setVBCFCP(itemProduto.getValorIcms().setScale(2).toPlainString());
		}

		return icms;
	}

	public static ICMS30 getICMS30(ItemProduto itemProduto) {

		ICMS30 icms = new ICMS30();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST("0.00");
		icms.setMotDesICMS(null);
		icms.setVICMSDeson(null);

		icms.setPFCPST(null);
		icms.setVFCPST(null);
		icms.setVBCFCPST(null);

		return icms;
	}

	public static ICMS40 getICMS40(ItemProduto itemProduto) {

		ICMS40 icms = new ICMS40();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setMotDesICMS(null);
		icms.setVICMSDeson(null);

		return icms;
	}

	public static ICMS51 getICMS51(ItemProduto itemProduto) {

		ICMS51 icms = new ICMS51();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		icms.setVICMSOp(itemProduto.getValorIcms().setScale(2).toPlainString());

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {
//			icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
//			icms.setVFCP(itemProduto.getValorFcp().toPlainString());
			icms.setVBCFCP(itemProduto.getValorIcms().setScale(2).toPlainString());
		}

		icms.setPDif(null);
		icms.setVICMSDif(null);

		return icms;
	}

	public static ICMS60 getICMS60(ItemProduto itemProduto) {

		ICMS60 icms = new ICMS60();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setPFCPSTRet(null);
		icms.setPST(null);
		icms.setVBCFCPSTRet(null);
		icms.setVBCSTRet(null);
		icms.setVICMSSTRet(null);

		return icms;
	}

	public static ICMS70 getICMS70(ItemProduto itemProduto) {

		ICMS70 icms = new ICMS70();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST("0.00");

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {
//			icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
//			icms.setVFCP(itemProduto.getValorFcp().toPlainString());
			icms.setVBCFCP(itemProduto.getValorIcms().setScale(2).toPlainString());
		}

		icms.setPFCPST(null);
		icms.setVFCPST(null);
		icms.setVBCFCPST(null);
		icms.setVICMSDeson(null);
		icms.setMotDesICMS(null);

		return icms;
	}

	public static ICMS90 getICMS90(ItemProduto itemProduto) {

		ICMS90 icms = new ICMS90();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST("0.00");

		if (FCPValidacoes.isNotFCPUFDest(itemProduto)) {
//			icms.setPFCP(itemProduto.getAliquotaFcp().toPlainString());
//			icms.setVFCP(itemProduto.getValorFcp().toPlainString());
			icms.setVBCFCP(itemProduto.getValorIcms().setScale(2).toPlainString());
		}

		icms.setPFCPST(null);
		icms.setVFCPST(null);
		icms.setVBCFCPST(null);
		icms.setVICMSDeson(null);
		icms.setMotDesICMS(null);

		return icms;
	}

	public static ICMSPart getICMSPart(ItemProduto itemProduto) {

		ICMSPart icms = new ICMSPart();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST(null);
		icms.setPBCOp(null);

		return icms;
	}

	public static ICMSST getICMSST(ItemProduto itemProduto) {

		ICMSST icms = new ICMSST();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setVBCSTDest(null);
		icms.setVBCSTRet(null);
		icms.setVICMSSTDest(null);
		icms.setVICMSSTRet(null);

		return icms;
	}

	public static ICMSUFDest getICMSUFDest(ItemProduto itemProduto) {

		ICMSUFDest icms = new ICMSUFDest();

		// vBCUFDest - Valor da BC do ICMSEntrada na UF de destino
		icms.setVBCUFDest(ICMSDifal.getVBCUFDest(itemProduto).toPlainString());
		icms.setVBCFCPUFDest(ICMSDifal.getVBCUFDest(itemProduto).toPlainString());
		// pFCPUFDest - Percentual do ICMSEntrada relativo ao Fundo de Combate
		// à
		// Pobreza (FCP) na UF de destino
		//icms.setPFCPUFDest(itemProduto.getAliquotaFcp().toPlainString());

		// pICMSUFDest - Alíquota interna da UF de destino
		//icms.setPICMSUFDest(itemProduto.getAliquotaInterIcms().toPlainString());

		// pICMSInter - Alíquota interestadual das UF envolvidas
		icms.setPICMSInter(itemProduto.getAliquotaIcms().toPlainString());

		// pICMSInterPart - Percentual provisório de partilha do ICMSEntrada
		// Interestadual
		icms.setPICMSInterPart(new BigDecimal(80).setScale(2).toPlainString());

		// vFCPUFDest - Valor do ICMSEntrada relativo ao Fundo de Combate à
		// Pobreza
		// (FCP) da UF de destino
		icms.setVFCPUFDest(FCPCalculos.getVFCPUFDest(itemProduto).toPlainString());

		// icms.setVBCFCPUFDest(ICMSEntradaDifal.getVICMSUFDest(item).toPlainString());

		// vICMSUFDest - Valor do ICMSEntrada Interestadual para a UF de destino
		icms.setVICMSUFDest(ICMSDifal.getVICMSUFDest(itemProduto).toPlainString());

		// vICMSUFRemet - Valor do ICMSEntrada Interestadual para a UF do
		// remetente
		icms.setVICMSUFRemet(ICMSDifal.getICMSUFRemet(itemProduto).toPlainString());

		return icms;
	}

	private static boolean isICMS00(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("00");
	}

	private static boolean isICMS10(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("10");
	}

	private static boolean isICMS20(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("20");
	}

	private static boolean isICMS30(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("30");
	}

	private static boolean isICMS40(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("40") || itemProduto.getCstIcms().equals("41")
				|| itemProduto.getCstIcms().equals("50");
	}

	private static boolean isICMS51(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("51");
	}

	private static boolean isICMS60(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("60");
	}

	private static boolean isICMS70(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("70");
	}

	private static boolean isICMS90(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("90");
	}

	private static boolean isICMSPart(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("xx");
	}

	private static boolean isICMSST(ItemProduto itemProduto) {
		return itemProduto.getCstIcms().equals("xx");
	}

	public static ICMS popularICMS(ItemProduto item) {
		ICMS icms = new ICMS();

		if (isICMS00(item)) {
			icms.setICMS00(getICMS00(item));
		} else if (isICMS10(item)) {
			icms.setICMS10(getICMS10(item));
		} else if (isICMS20(item)) {
			icms.setICMS20(getICMS20(item));
		} else if (isICMS30(item)) {
			icms.setICMS30(getICMS30(item));
		} else if (isICMS40(item)) {
			icms.setICMS40(getICMS40(item));
		} else if (isICMS51(item)) {
			icms.setICMS51(getICMS51(item));
		} else if (isICMS60(item)) {
			icms.setICMS60(getICMS60(item));
		} else if (isICMS70(item)) {
			icms.setICMS70(getICMS70(item));
		} else if (isICMS90(item)) {
			icms.setICMS90(getICMS90(item));
		} else if (isICMSST(item)) {
			icms.setICMSST(getICMSST(item));
		} else if (isICMSPart(item)) {
			icms.setICMSPart(getICMSPart(item));
		}

		return icms;
	}

}
