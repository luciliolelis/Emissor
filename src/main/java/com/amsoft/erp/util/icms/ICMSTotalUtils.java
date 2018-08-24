package com.amsoft.erp.util.icms;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.fcp.FCPCalculos;
import com.amsoft.erp.model.nfe.fcp.FCPValidacoes;
import com.amsoft.erp.model.nfe.icms.ICMSDifal;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.IPIUtils;
import com.amsoft.erp.util.PISCOFINSUtils;
import com.amsoft.erp.util.icms.normal.NormalUtils;
import com.amsoft.erp.util.icms.simples.SimplesUtils;
import com.chronos.calc.TributacaoException;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;

public class ICMSTotalUtils {

	public static ICMSTot popularICMSTotal(Nfe nfe) throws TributacaoException {

		ICMSTot icmstot = new ICMSTot();

		BigDecimal vProd = BigDecimal.ZERO;
		BigDecimal vDesc = BigDecimal.ZERO;
		BigDecimal vFrete = BigDecimal.ZERO;
		BigDecimal vSeg = BigDecimal.ZERO;
		BigDecimal vOutro = BigDecimal.ZERO;

		BigDecimal vbcst = BigDecimal.ZERO;
		BigDecimal vicmsst = BigDecimal.ZERO;
		BigDecimal vbc = BigDecimal.ZERO;
		BigDecimal vicms = BigDecimal.ZERO;
		BigDecimal vfcpst = BigDecimal.ZERO;
		BigDecimal vbcfcpst = BigDecimal.ZERO;
		BigDecimal vfcp = BigDecimal.ZERO;
		// BigDecimal vbcfcp = BigDecimal.ZERO;
		BigDecimal vpis = BigDecimal.ZERO;
		BigDecimal vipi = BigDecimal.ZERO;

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {

					ICMS icms = SimplesUtils.popularIcmsSN(item);

					if (icms.getICMSSN101() != null) {

					} else if (icms.getICMSSN102() != null) {

					} else if (icms.getICMSSN201() != null) {
						vbcst = vbcst.add(new BigDecimal(icms.getICMSSN201().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN201().getVICMSST()));
						vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN201().getVFCPST()));
						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN201().getVBCFCPST()));
					} else if (icms.getICMSSN202() != null) {
						vbcst = vbcst.add(new BigDecimal(icms.getICMSSN202().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN202().getVICMSST()));
						vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN202().getVFCPST()));
						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN202().getVBCFCPST()));
					} else if (icms.getICMSSN500() != null) {

					} else if (icms.getICMSSN900() != null) {
						vbcst = vbcst.add(new BigDecimal(icms.getICMSSN900().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN900().getVICMSST()));
						vbc = vbcst.add(new BigDecimal(icms.getICMSSN900().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMSSN900().getVICMS()));
						vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN900().getVFCPST()));
						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN900().getVBCFCPST()));
					}
				}

				if (item.getCstPis() != null) {
					if (PISCOFINSUtils.isOutr(item)) {
						vpis = vpis.add(new BigDecimal(PISCOFINSUtils.popularPisOutr(item).getPISOutr().getVPIS()));
					} else if (PISCOFINSUtils.isNT(item)) {
					} else if (PISCOFINSUtils.isAliq(item)) {
						vpis = vpis.add(new BigDecimal(PISCOFINSUtils.popularPISAliq(item).getPISAliq().getVPIS()));
					}
				}

				if (IPIValidacoes.isNT(item)) {

				} else if (IPIValidacoes.isTRIB(item)) {
					vipi = vipi.add(new BigDecimal(IPIUtils.popularIPITrib(item).getIPITrib().getVIPI()));
				}

				vProd = vProd.add(item.getValorUnitario());
				vDesc = vDesc.add(item.getValorDesconto());
				vFrete = vFrete.add(item.getValorFrete());
				vSeg = vSeg.add(item.getValorSeguro());
				vOutro = vOutro.add(item.getValorDespesa());
			}

			// icmstot.setVICMSDeson("0.00");
			// icmstot.setVBC("0.00");
			// icmstot.setVICMS("0.00");
		} else {
			AmsoftUtils.info("REGIME NORMAL");

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {
					ICMS icms = NormalUtils.popularICMS(item);

					if (icms.getICMS00() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS00().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS00().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS00().getVFCP()));
						
						

					} else if (icms.getICMS10() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS10().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS10().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS10().getVFCP()));

					} else if (icms.getICMS20() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS20().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS20().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS20().getVFCP()));

					} else if (icms.getICMS30() != null) {

					} else if (icms.getICMS40() != null) {

					} else if (icms.getICMS51() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS51().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS51().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS51().getVFCP()));

					} else if (icms.getICMS60() != null) {

					} else if (icms.getICMS70() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS70().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS70().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS70().getVFCP()));

					} else if (icms.getICMS90() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMS90().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS90().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS90().getVFCP()));

					} else if (icms.getICMSPart() != null) {

						vbc = vbcst.add(new BigDecimal(icms.getICMSPart().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMSPart().getVICMS()));

					}

					vProd = vProd.add(item.getValorUnitario());
					vDesc = vDesc.add(item.getValorDesconto());
					vFrete = vFrete.add(item.getValorFrete());
					vSeg = vSeg.add(item.getValorSeguro());
					vOutro = vOutro.add(item.getValorDespesa());
				}
			}

		}
		icmstot.setVICMSDeson("0.00");
		icmstot.setVFCPUFDest("0.00");
		icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
		icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		icmstot.setVFCP(vfcp.toPlainString());
		if (nfe.getValorFCP() != null) {
			
		}
		//
		// if (isDifal(nfe)) {
		// icmstot.setVBC("0.00");
		// icmstot.setVICMS("0.00");
		// icmstot.setVFCP("0.00");
		// icmstot.setVFCPUFDest(FCPCalculos.getTotalVFCPUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		// }

		icmstot.setVBC(vbc.toPlainString());
		icmstot.setVICMS(vicms.toPlainString());

		icmstot.setVFCPSTRet("0.00");

		icmstot.setVBCST(vbcst.toPlainString());
		icmstot.setVST(vicmsst.toEngineeringString());

		icmstot.setVFCPST(vfcpst.toPlainString());
		icmstot.setVProd(nfe.getValorTotalSemDesconto().setScale(2).toPlainString());
		icmstot.setVFrete(nfe.getValorFrete().toString());
		icmstot.setVSeg(nfe.getValorSeguro().setScale(2).toString());
		icmstot.setVDesc(nfe.getValorDesconto().toString());
		icmstot.setVII("0");

		icmstot.setVIPI(vipi.toString());

		icmstot.setVIPIDevol("0");
		icmstot.setVPIS(vpis.toString());
		icmstot.setVCOFINS(nfe.getValorTotalCOFINS().toString());
		icmstot.setVOutro(nfe.getValorDespesas().setScale(2).toString());

		//
		// (+) vProd (Somatório do valor de todos os produtos da NF-e);
		// (-) vDesc (Somatório do desconto de todos os produtos da NF-e);
		// (+) vST (Somatório do valor do ICMS com Substituição Tributária de
		// todos os produtos da NF-e);
		// (+) vFrete (Somatório do valor do Frete de todos os produtos da
		// NF-e);
		// (+) vSeg (Somatório do valor do seguro de todos os produtos da NF-e);
		// (+) vOutro (Somatório do valor de outras despesas de todos os
		// produtos da NF-e);
		// (+) vII (Somatório do valor do Imposto de Importação de todos os
		// produtos da NF-e);
		// (+) vIPI (Somatório do valor do IPI de todos os produtos da NF-e);
		// (+) vServ (Somatório do valor do serviço de todos os itens da NF-e).

		vProd = vProd.subtract(vDesc).add(vFrete).add(vSeg).add(vOutro).add(vicmsst).add(vipi).add(vfcpst);

		icmstot.setVNF(vProd.setScale(2).toPlainString());

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
