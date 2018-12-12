package com.amsoft.erp.util.icms;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
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
		BigDecimal vcofins = BigDecimal.ZERO;

		BigDecimal vicmsdeson = BigDecimal.ZERO;

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {
			AmsoftUtils.info("SIMPLES NACIONAL");

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {

					ICMS icms = SimplesUtils.popularIcmsSN(item);

					if (icms.getICMSSN101() != null) {

					} else if (icms.getICMSSN102() != null) {

					} else if (icms.getICMSSN201() != null) {
						vbcst = vbcst.add(new BigDecimal(icms.getICMSSN201().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN201().getVICMSST()));
//						vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN201().getVFCPST()));
//						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN201().getVBCFCPST()));
					} else if (icms.getICMSSN202() != null) {
						vbcst = vbcst.add(new BigDecimal(icms.getICMSSN202().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN202().getVICMSST()));
//						vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN202().getVFCPST()));
//						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN202().getVBCFCPST()));
					} else if (icms.getICMSSN500() != null) {

					} else if (icms.getICMSSN900() != null) {
						//vbcst = vbcst.add(new BigDecimal(icms.getICMSSN900().getVBCST()));
						//vicmsst = vicmsst.add(new BigDecimal(icms.getICMSSN900().getVICMSST()));
						//vbc = vbc.add(new BigDecimal(icms.getICMSSN900().getVBC()));
						//vicms = vicms.add(new BigDecimal(icms.getICMSSN900().getVICMS()));
						//vfcpst = vfcpst.add(new BigDecimal(icms.getICMSSN900().getVFCPST()));
						//vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMSSN900().getVBCFCPST()));
					}
				}

				if (item.getCstPis() != null) {
					if (PISCOFINSUtils.isOutr(item)) {
						vpis = vpis.add(new BigDecimal(PISCOFINSUtils.popularPisOutr(item).getPISOutr().getVPIS()));
						vcofins = vcofins
								.add(new BigDecimal(PISCOFINSUtils.popularCOFINS(item).getCOFINSOutr().getVCOFINS()));
					} else if (PISCOFINSUtils.isNT(item)) {
					} else if (PISCOFINSUtils.isAliq(item)) {
						vpis = vpis.add(new BigDecimal(PISCOFINSUtils.popularPISAliq(item).getPISAliq().getVPIS()));
						vcofins = vcofins
								.add(new BigDecimal(PISCOFINSUtils.popularCOFINS(item).getCOFINSAliq().getVCOFINS()));
					}
				}

				if (IPIValidacoes.isNT(item)) {

				} else if (IPIValidacoes.isTRIB(item)) {
					vipi = vipi.add(new BigDecimal(IPIUtils.popularIPITrib(item).getIPITrib().getVIPI()));
				}

				vProd = vProd.add(
						item.getValorUnitario().multiply(item.getQuantidade()).setScale(2, RoundingMode.HALF_EVEN));
				vDesc = vDesc.add(item.getValorDesconto());
				vFrete = vFrete.add(item.getValorFrete());
				vSeg = vSeg.add(item.getValorSeguro());
				vOutro = vOutro.add(item.getValorDespesa());
			}

		} else {

			AmsoftUtils.info("REGIME NORMAL");

			for (ItemProduto item : nfe.getItensProdutos()) {
				if (AmsoftUtils.isProdutoValido(item)) {

					ICMS icms = NormalUtils.popularICMS(item);

					if (icms.getICMS00() != null) {
						vbc = vbc.add(new BigDecimal(icms.getICMS00().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMS00().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS00().getVFCP()));
					} else if (icms.getICMS10() != null) {
						vbc = vbc.add(new BigDecimal(icms.getICMS10().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMS10().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS10().getVFCP()));
						vbcst = vbcst.add(new BigDecimal(icms.getICMS10().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMS10().getVICMSST()));
						vfcpst = vfcpst.add(new BigDecimal(icms.getICMS10().getVFCPST()));
						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMS10().getVBCFCPST()));
					} else if (icms.getICMS20() != null) {
						vbc = vbcst.add(new BigDecimal(icms.getICMS20().getVBC()));
						vicms = vicmsst.add(new BigDecimal(icms.getICMS20().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS20().getVFCP()));
					} else if (icms.getICMS30() != null) {

					} else if (icms.getICMS40() != null) {

					} else if (icms.getICMS51() != null) {

						vbc = vbc.add(new BigDecimal(icms.getICMS51().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMS51().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS51().getVFCP()));

					} else if (icms.getICMS60() != null) {

					} else if (icms.getICMS70() != null) {

						vbc = vbc.add(new BigDecimal(icms.getICMS70().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMS70().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS70().getVFCP()));

					} else if (icms.getICMS90() != null) {

						vbc = vbc.add(new BigDecimal(icms.getICMS90().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMS90().getVICMS()));
						vfcp = vfcp.add(new BigDecimal(icms.getICMS90().getVFCP()));
						vbcst = vbcst.add(new BigDecimal(icms.getICMS90().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMS90().getVICMSST()));
						vfcpst = vfcpst.add(new BigDecimal(icms.getICMS90().getVFCPST()));
						vbcfcpst = vbcfcpst.add(new BigDecimal(icms.getICMS90().getVBCFCPST()));
						vicmsdeson = vicmsdeson.add(new BigDecimal(icms.getICMS90().getVICMSDeson()));

					} else if (icms.getICMSPart() != null) {

						vbc = vbc.add(new BigDecimal(icms.getICMSPart().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMSPart().getVICMS()));
						vbcst = vbcst.add(new BigDecimal(icms.getICMSPart().getVBCST()));
						vicmsst = vicmsst.add(new BigDecimal(icms.getICMSPart().getVICMSST()));
					}

					if (IPIValidacoes.isNT(item)) {

					} else if (IPIValidacoes.isTRIB(item)) {
						vipi = vipi.add(new BigDecimal(IPIUtils.popularIPITrib(item).getIPITrib().getVIPI()));
					}

					vProd = vProd.add(
							item.getValorUnitario().multiply(item.getQuantidade()).setScale(2, RoundingMode.HALF_EVEN));
					vDesc = vDesc.add(item.getValorDesconto());
					vFrete = vFrete.add(item.getValorFrete());
					vSeg = vSeg.add(item.getValorSeguro());
					vOutro = vOutro.add(item.getValorDespesa());
				}
			}
		}

		icmstot.setVICMSDeson(vicmsdeson.toPlainString());
		icmstot.setVFCPUFDest("0.00");
		// icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		icmstot.setVFCP(vfcp.toPlainString());
		icmstot.setVBC(vbc.toPlainString());
		icmstot.setVICMS(vicms.toPlainString());
		icmstot.setVFCPSTRet("0.00");
		icmstot.setVBCST(vbcst.toPlainString());
		icmstot.setVST(vicmsst.toEngineeringString());
		icmstot.setVFCPST(vfcpst.toPlainString());
		icmstot.setVProd(vProd.setScale(2).toPlainString());
		icmstot.setVFrete(nfe.getValorFrete().toString());
		icmstot.setVSeg(nfe.getValorSeguro().setScale(2).toString());
		icmstot.setVDesc(nfe.getValorDesconto().toString());
		icmstot.setVII("0");
		icmstot.setVIPI(vipi.toString());
		icmstot.setVIPIDevol("0");
		icmstot.setVPIS(vpis.toString());
		icmstot.setVCOFINS(vcofins.toPlainString());
		icmstot.setVOutro(nfe.getValorDespesas().setScale(2).toString());

		/*
		 * (+) vProd (Somatório do valor de todos os produtos da NF-e); (-)
		 * vDesc (Somatório do desconto de todos os produtos da NF-e); (+) vST
		 * (Somatório do valor do ICMS com Substituição Tributária de todos os
		 * produtos da NF-e); (+) vFrete (Somatório do valor do Frete de todos
		 * os produtos da NF-e); (+) vSeg (Somatório do valor do seguro de todos
		 * os produtos da NF-e); (+) vOutro (Somatório do valor de outras
		 * despesas de todos os produtos da NF-e); (+) vII (Somatório do valor
		 * do Imposto de Importação de todos os produtos da NF-e); (+) vIPI
		 * (Somatório do valor do IPI de todos os produtos da NF-e); (+) vServ
		 * (Somatório do valor do serviço de todos os itens da NF-e).
		 */

		vProd = vProd.subtract(vDesc).add(vFrete).add(vSeg).add(vOutro).add(vicmsst).add(vipi).add(vfcpst);

		icmstot.setVNF(vProd.setScale(2).toPlainString());
		icmstot.setVTotTrib(nfe.getValorTransparencia().setScale(2).toPlainString());
		return icmstot;
	}

	public static ICMSTot popularICMSTotal(NFCe nfe) {

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
		BigDecimal vicmsdeson = BigDecimal.ZERO;

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {
			AmsoftUtils.info("SIMPLES NACIONAL");

			for (ItemProdutoNFCe item : nfe.getItensProdutos()) {
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
						vbc = vbc.add(new BigDecimal(icms.getICMSSN900().getVBC()));
						vicms = vicms.add(new BigDecimal(icms.getICMSSN900().getVICMS()));
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

				vProd = vProd.add(
						item.getValorUnitario().multiply(item.getQuantidade()).setScale(2, RoundingMode.HALF_EVEN));
				vDesc = vDesc.add(item.getValorDesconto());
				vFrete = vFrete.add(item.getValorFrete());
				vSeg = vSeg.add(item.getValorSeguro());
				vOutro = vOutro.add(item.getValorDespesa());

			}

		}

		icmstot.setVICMSDeson(vicmsdeson.toPlainString());
		icmstot.setVFCPUFDest("0.00");
		// icmstot.setVICMSUFDest(ICMSDifal.getTotalVICMSUFDest(nfe).toPlainString());
		// icmstot.setVICMSUFRemet(ICMSDifal.getTotalVICMSUFRemet(nfe).toPlainString());
		icmstot.setVFCP(vfcp.toPlainString());
		icmstot.setVBC(vbc.toPlainString());
		icmstot.setVICMS(vicms.toPlainString());
		icmstot.setVFCPSTRet("0.00");
		icmstot.setVBCST(vbcst.toPlainString());
		icmstot.setVST(vicmsst.toEngineeringString());
		icmstot.setVFCPST(vfcpst.toPlainString());
		icmstot.setVProd(vProd.setScale(2).toPlainString());
		icmstot.setVFrete(nfe.getValorFrete().toString());
		icmstot.setVSeg(nfe.getValorSeguro().setScale(2).toString());
		icmstot.setVDesc(nfe.getValorDesconto().toString());
		icmstot.setVII("0");
		icmstot.setVIPI(vipi.toString());
		icmstot.setVIPIDevol("0");
		icmstot.setVPIS(vpis.toString());
		icmstot.setVCOFINS("0");
		icmstot.setVOutro(nfe.getValorDespesas().setScale(2).toString());

		/*
		 * (+) vProd (Somatório do valor de todos os produtos da NF-e); (-)
		 * vDesc (Somatório do desconto de todos os produtos da NF-e); (+) vST
		 * (Somatório do valor do ICMS com Substituição Tributária de todos os
		 * produtos da NF-e); (+) vFrete (Somatório do valor do Frete de todos
		 * os produtos da NF-e); (+) vSeg (Somatório do valor do seguro de todos
		 * os produtos da NF-e); (+) vOutro (Somatório do valor de outras
		 * despesas de todos os produtos da NF-e); (+) vII (Somatório do valor
		 * do Imposto de Importação de todos os produtos da NF-e); (+) vIPI
		 * (Somatório do valor do IPI de todos os produtos da NF-e); (+) vServ
		 * (Somatório do valor do serviço de todos os itens da NF-e).
		 */

		vProd = vProd.subtract(vDesc).add(vFrete).add(vSeg).add(vOutro).add(vicmsst).add(vipi).add(vfcpst);

		icmstot.setVNF(vProd.setScale(2).toPlainString());
		icmstot.setVTotTrib(nfe.getValorTransparencia().setScale(2).toPlainString());
		return icmstot;
	}
}
