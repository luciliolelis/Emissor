package com.amsoft.erp.util;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.fcp.FCPCalculos;
import com.amsoft.erp.model.nfe.fcp.FCPValidacoes;
import com.amsoft.erp.model.nfe.icms.ICMSDifal;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;

public class ICMSTotalUtils {

	public static ICMSTot popularICMSTotal(Nfe nfe) {

		ICMSTot icmstot = new ICMSTot();

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {
			icmstot.setVICMSDeson("0.00");
			icmstot.setVBC("0.00");
			icmstot.setVICMS("0.00");
		} else {
			icmstot.setVICMSDeson("0.00");
			icmstot.setVBC(nfe.getValorBaseIcms().toString());
			icmstot.setVICMS(nfe.getValorIcms().toString());
		}

		if (nfe.getValorFCP() != null) {
			icmstot.setVFCP(nfe.getValorFCP().toPlainString());
		}

		if (isDifal(nfe)) {
			icmstot.setVBC("0.00");
			icmstot.setVICMS("0.00");
			icmstot.setVFCP("0.00");
			icmstot.setVFCPUFDest(FCPCalculos.getTotalVFCPUFDest(nfe).toPlainString());
			icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
			icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		}

		icmstot.setVFCPST("0.00");
		icmstot.setVFCPSTRet("0.00");

		icmstot.setVBCST(nfe.getValorBaseIcmsSt().toString());
		icmstot.setVST(nfe.getValorIcmsSt().toString());

		icmstot.setVProd(nfe.getValorTotalSemDesconto().setScale(2).toPlainString());
		icmstot.setVFrete(nfe.getValorFrete().toString());
		icmstot.setVSeg(nfe.getValorSeguro().setScale(2).toString());
		icmstot.setVDesc(nfe.getValorDesconto().toString());
		icmstot.setVII("0");

		icmstot.setVIPI(nfe.getValorIpi().toPlainString());

		icmstot.setVIPIDevol("0");
		icmstot.setVPIS(nfe.getValorTotalPIS().toString());
		icmstot.setVCOFINS(nfe.getValorTotalCOFINS().toString());
		icmstot.setVOutro(nfe.getValorDespesas().setScale(2).toString());
		icmstot.setVNF(nfe.getValorTotalNota().setScale(2).toPlainString());
		icmstot.setVTotTrib(nfe.getValorTransparencia().setScale(2).toPlainString());

		return icmstot;
	}

	public static ICMSTot popularICMSTotal(NFCe nfe) {

		ICMSTot icmstot = new ICMSTot();

		icmstot.setVICMSDeson("0.00");
		icmstot.setVBC(nfe.getValorBaseIcms().toString());
		icmstot.setVICMS(nfe.getValorIcms().toString());

		icmstot.setVFCP("0.00");

		//
		// if (isDifal(nfe)) {
		// icmstot.setVBC("0.00");
		// icmstot.setVICMS("0.00");
		// icmstot.setVFCP("0.00");
		// icmstot.setVFCPUFDest(FCPCalculos.getTotalVFCPUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		// }

		icmstot.setVFCPST("0.00");
		icmstot.setVFCPSTRet("0.00");

		icmstot.setVBCST("0.00");
		icmstot.setVST("0.00");

		icmstot.setVProd(nfe.getValorTotalProdutoSemDesconto().toEngineeringString());
		icmstot.setVFrete(nfe.getValorFrete().toString());
		icmstot.setVSeg(nfe.getValorSeguro().setScale(2).toString());
		icmstot.setVDesc(nfe.getValorDesconto().toString());
		icmstot.setVII("0");

		icmstot.setVIPI("0.00");

		icmstot.setVIPIDevol("0");
		icmstot.setVPIS("0.00");
		icmstot.setVCOFINS("0.00");
		icmstot.setVOutro(nfe.getValorDespesas().setScale(2).toString());
		icmstot.setVNF(nfe.getValorTotalNota().setScale(2).toPlainString());
		icmstot.setVTotTrib(nfe.getValorTransparencia().setScale(2).toPlainString());

		return icmstot;
	}

	private static boolean isDifal(Nfe nfe) {

		for (ItemProduto itemProduto : nfe.getItensProdutos()) {
			if (FCPValidacoes.isFCPUFDest(itemProduto)) {
				return true;
			}
		}
		return false;
	}

}
