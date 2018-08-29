package com.amsoft.erp.util.icms.simples;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.TributosUtils;
import com.amsoft.erp.util.icms.CalculosUtils;
import com.chronos.calc.csosn.Csosn101;
import com.chronos.calc.csosn.Csosn201;
import com.chronos.calc.csosn.Csosn202;
import com.chronos.calc.csosn.Csosn500;
import com.chronos.calc.csosn.Csosn900;
import com.chronos.calc.dto.ITributavel;
import com.chronos.calc.resultados.IResultadoCalculoFcpSt;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;

public class TagNFe {

	public static ICMSSN101 getICMSST101(ItemProduto item) {
		Csosn101 csosn = new Csosn101();
		csosn.calcular(TributosUtils.getTtibutos(item));

		ICMSSN101 icmsSN101 = new ICMSSN101();
		icmsSN101.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN101.setCSOSN(item.getCsosn());
		icmsSN101.setPCredSN(csosn.getValorCredito().toString());
		icmsSN101.setVCredICMSSN(csosn.getValorCredito().toString());

		return icmsSN101;
	}

	public static ICMSSN102 getICMSST102(ItemProduto item) {

		ICMSSN102 icmsSN102 = new ICMSSN102();
		icmsSN102.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN102.setCSOSN(item.getCsosn());
		return icmsSN102;
	}

	
	public static ICMSSN201 getICMSST201(ItemProduto item) {

		// CALCULOS
		ITributavel tributos = TributosUtils.getTtibutos(item);
		Csosn201 csosn = new Csosn201();
		csosn.calcular(tributos);
		IResultadoCalculoFcpSt fcpst = CalculosUtils.calcularFcpSt(item);

		// ICMSSN201
		ICMSSN201 icmsSN201 = new ICMSSN201();
		icmsSN201.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN201.setCSOSN(item.getCsosn());
		icmsSN201.setModBCST(getModBCST(item));

		// ICMS
		icmsSN201.setPMVAST(csosn.getPercentualMva().toPlainString());
		icmsSN201.setPRedBCST(csosn.getPercentualReducaoSt().toPlainString());
		icmsSN201.setVBCST(csosn.getValorBcIcmsSt().toPlainString());
		icmsSN201.setPICMSST(csosn.getPercentualIcmsSt().toPlainString());
		icmsSN201.setVICMSST(csosn.getValorIcmsSt().toPlainString());

		// Credito ICMS
		icmsSN201.setPCredSN(csosn.getPercentualCredito().toPlainString());
		icmsSN201.setVCredICMSSN(csosn.getValorCredito().toPlainString());

	
		icmsSN201.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icmsSN201.setVBCFCPST(fcpst.getBaseCalculo().toPlainString());
		icmsSN201.setVFCPST(fcpst.getValor().toPlainString());

		return icmsSN201;
	}

	
	public static ICMSSN202 getICMSST202(ItemProduto item) {
		
		ITributavel tributos = TributosUtils.getTtibutos(item);
		Csosn202 csosn = new Csosn202();
		csosn.calcular(tributos);
		IResultadoCalculoFcpSt fcp = CalculosUtils.calcularFcpSt(item);

		// ICMSSN202
		ICMSSN202 icmsSN202 = new ICMSSN202();
		icmsSN202.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN202.setCSOSN(item.getCsosn());
		icmsSN202.setModBCST(getModBCST(item));

		// ICMS
		icmsSN202.setPMVAST(csosn.getPercentualMva().toString());
		icmsSN202.setPRedBCST(csosn.getPercentualReducaoSt().toPlainString());
		icmsSN202.setVBCST(csosn.getValorBcIcmsSt().toPlainString());
		icmsSN202.setPICMSST(csosn.getPercentualIcmsSt().toPlainString());
		icmsSN202.setVICMSST(csosn.getValorIcmsSt().toPlainString());

		// FCP ST
		icmsSN202.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icmsSN202.setVBCFCPST(fcp.getBaseCalculo().toPlainString());
		icmsSN202.setVFCPST(fcp.getValor().toPlainString());

		return icmsSN202;
	}

	public static String getModBCST(ItemProduto item) {
		return "4";
	}

	public static ICMSSN500 getICMSST500(ItemProduto item) {

		Csosn500 csosn = new Csosn500();
		csosn.calcular(TributosUtils.getTtibutos(item));

		ICMSSN500 icmsSN500 = new ICMSSN500();
		icmsSN500.setOrig(item.getProduto().getOrigemProduto().getCodigo());
		icmsSN500.setCSOSN(item.getCsosn());

		return icmsSN500;
	}

	public static ICMSSN900 getICMSST900(ItemProduto item) {
		
		// CALCULOS
		ITributavel tributos = TributosUtils.getTtibutos(item);
		Csosn900 csosn = new Csosn900();
		csosn.calcular(tributos);
		IResultadoCalculoFcpSt fcp = CalculosUtils.calcularFcpSt(item);
		
		// ICMSSN900
		ICMSSN900 icmsSN900 = new ICMSSN900();
		icmsSN900.setOrig(item.getProduto().getOrigemProduto().getCodigo()); 
		icmsSN900.setCSOSN(item.getCsosn());
		icmsSN900.setModBCST(getModBCST(item));

		// ICMS
		icmsSN900.setVBC(csosn.getValorBcIcms().toPlainString());
		icmsSN900.setPRedBC(csosn.getPercentualReducaoIcmsBc().toPlainString());
		icmsSN900.setVICMS(csosn.getValorIcms().toPlainString());
		
		// ICMS ST
		icmsSN900.setPMVAST(csosn.getPercentualMva().toPlainString());
		icmsSN900.setPRedBCST(csosn.getPercentualReducaoSt().toPlainString());
		icmsSN900.setVBCST(csosn.getValorBcIcmsSt().toPlainString());
		icmsSN900.setPICMSST(csosn.getPercentualIcmsSt().toPlainString());
		icmsSN900.setVICMSST(csosn.getValorIcmsSt().toPlainString());

		// Credito ICMS
		icmsSN900.setPCredSN(csosn.getPercentualCredito().toPlainString());
		icmsSN900.setVCredICMSSN(csosn.getValorCredito().toPlainString());

		// FCP ST - OBS. Este valor é temporário até que a conf. no emitente
		// esteja pronta.
		icmsSN900.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icmsSN900.setVBCFCPST(fcp.getBaseCalculo().toPlainString());
		icmsSN900.setVFCPST(fcp.getValor().toPlainString());

		return icmsSN900;
	}

}
