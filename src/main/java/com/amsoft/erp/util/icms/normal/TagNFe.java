package com.amsoft.erp.util.icms.normal;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.TributosUtils;
import com.amsoft.erp.util.icms.CalculosUtils;
import com.chronos.calc.TributacaoException;
import com.chronos.calc.cst.Cst00;
import com.chronos.calc.cst.Cst10;
import com.chronos.calc.cst.Cst20;
import com.chronos.calc.cst.Cst30;
import com.chronos.calc.cst.Cst51;
import com.chronos.calc.cst.Cst70;
import com.chronos.calc.cst.Cst90;
import com.chronos.calc.dto.ITributavel;
import com.chronos.calc.resultados.IResultadoCalculoFcp;
import com.chronos.calc.resultados.IResultadoCalculoFcpSt;

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

public class TagNFe {

	public static ICMS00 getICMS00(ItemProduto itemProduto) throws TributacaoException {

		ICMS00 icms = new ICMS00();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst00 cst = new Cst00();
		cst.calcular(tributos);

		icms.setPICMS(cst.getPercentualIcms().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());
		icms.setPFCP(new BigDecimal(2).setScale(2).toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());

		return icms;
	}

	public static ICMS10 getICMS10(ItemProduto itemProduto) throws TributacaoException {

		ICMS10 icms = new ICMS10();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		IResultadoCalculoFcpSt fcpst = CalculosUtils.calcularFcpSt(itemProduto);

		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst10 cst = new Cst10();
		cst.calcular(tributos);

		icms.setPICMS(tributos.getPercentualIcms().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());

		icms.setPICMSST(tributos.getPercentualIcmsSt().toPlainString());
		icms.setVBCST(cst.getValorBcIcmsSt().toPlainString());
		icms.setVICMSST(cst.getValorIcmsSt().toPlainString());

		icms.setPRedBCST(tributos.getPercentualReducaoSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST("0.00");

		icms.setPFCP(tributos.getPercentualFcp().toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());
		icms.setVBCFCP(fcp.getBaseCalculo().toPlainString());

		icms.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icms.setVFCPST(fcpst.getValor().toPlainString());
		icms.setVBCFCPST(fcpst.getBaseCalculo().toPlainString());

		return icms;
	}

	public static ICMS20 getICMS20(ItemProduto itemProduto) throws TributacaoException {

		ICMS20 icms = new ICMS20();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst20 cst = new Cst20();
		cst.calcular(tributos);

		icms.setPICMS(cst.getPercentualIcms().toPlainString());
		icms.setPRedBC(cst.getPercentualReducao().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());

		icms.setMotDesICMS(cst.getModalidadeDeterminacaoBcIcms().getCodigo());
		icms.setVICMSDeson(icms.getVICMS());

		icms.setPFCP(tributos.getPercentualFcp().toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());
		icms.setVBCFCP(fcp.getBaseCalculo().toPlainString());

		return icms;
	}

	public static ICMS30 getICMS30(ItemProduto itemProduto) {

		ICMS30 icms = new ICMS30();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());

		IResultadoCalculoFcpSt fcpst = CalculosUtils.calcularFcpSt(itemProduto);
		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst30 cst = new Cst30();
		cst.calcular(tributos);

		icms.setPICMSST(cst.getPercentualIcmsSt().toPlainString());
		icms.setVBCST(cst.getValorBcIcmsSt().toPlainString());
		icms.setVICMSST(cst.getValorIcmsSt().toPlainString());
		icms.setPRedBCST(cst.getPercentualReducaoSt().toPlainString());

		icms.setModBCST("4");
		icms.setPMVAST(tributos.getPercentualMva().toPlainString());

		icms.setMotDesICMS(cst.getModalidadeDeterminacaoBcIcmsSt().getCodigo());
		icms.setVICMSDeson(icms.getVICMSST());

		icms.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icms.setVFCPST(fcpst.getValor().toPlainString());
		icms.setVBCFCPST(fcpst.getBaseCalculo().toPlainString());

		return icms;
	}

	public static ICMS40 getICMS40(ItemProduto itemProduto) {

		ICMS40 icms = new ICMS40();
		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());

		// ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		// Cst40 cst = new Cst40();
		// cst.calcular(tributos);

		icms.setMotDesICMS(null);
		icms.setVICMSDeson(null);

		return icms;
	}

	public static ICMS51 getICMS51(ItemProduto itemProduto) {

		ICMS51 icms = new ICMS51();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst51 cst = new Cst51();
		cst.calcular(tributos);

		icms.setPICMS(cst.getPercentualIcms().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());
		icms.setPRedBC(cst.getPercentualReducao().toPlainString());

		icms.setVICMSOp(cst.getValorIcmsOperacao().toPlainString());

		icms.setPFCP(tributos.getPercentualFcp().toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());
		icms.setVBCFCP(fcp.getBaseCalculo().toPlainString());

		icms.setPDif(cst.getPercentualDiferimento().toPlainString());
		icms.setVICMSDif(cst.getValorIcmsDiferido().toPlainString());

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

	public static ICMS70 getICMS70(ItemProduto itemProduto) throws TributacaoException {

		ICMS70 icms = new ICMS70();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		IResultadoCalculoFcpSt fcpst = CalculosUtils.calcularFcpSt(itemProduto);

		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst70 cst = new Cst70();
		cst.calcular(tributos);

		icms.setPICMS(cst.getPercentualIcms().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());
		icms.setPRedBC(cst.getPercentualReducao().toPlainString());
		icms.setPICMSST(cst.getPercentualIcmsSt().toPlainString());
		icms.setVBCST(cst.getValorBcIcmsSt().setScale(2).toPlainString());
		icms.setVICMSST(cst.getValorIcmsSt().toPlainString());
		icms.setPRedBCST(cst.getPercentualReducaoSt().toPlainString());
		icms.setModBCST("4");
		icms.setPMVAST(cst.getPercentualMva().toPlainString());

		icms.setPFCP(tributos.getPercentualFcp().toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());
		icms.setVBCFCP(fcp.getBaseCalculo().toPlainString());

		icms.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icms.setVFCPST(fcpst.getValor().toPlainString());
		icms.setVBCFCPST(fcpst.getBaseCalculo().toPlainString());

		icms.setMotDesICMS(cst.getModalidadeDeterminacaoBcIcms().getCodigo());
		icms.setVICMSDeson(icms.getVICMSST());

		// icms.setVICMSDeson(null);
		// icms.setMotDesICMS(null);

		return icms;
	}

	public static ICMS90 getICMS90(ItemProduto itemProduto) {

		ICMS90 icms = new ICMS90();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");
		icms.setModBCST("4");

		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst90 cst = new Cst90();
		cst.calcular(tributos);

		icms.setPRedBC(tributos.getPercentualReducao().toPlainString());
		icms.setPICMS(tributos.getPercentualIcms().toPlainString());
		icms.setVBC(cst.getValorBcIcms().toPlainString());
		icms.setVICMS(cst.getValorIcms().toPlainString());

		icms.setPRedBCST(tributos.getPercentualReducaoSt().toPlainString());
		icms.setPICMSST(tributos.getPercentualIcmsSt().toPlainString());
		icms.setVBCST(cst.getValorBcIcmsSt().toPlainString());
		icms.setVICMSST(cst.getValorIcmsSt().setScale(2).toPlainString());
		icms.setPMVAST(tributos.getPercentualMva().toPlainString());

		IResultadoCalculoFcpSt fcpst = CalculosUtils.calcularFcpSt(itemProduto);
		icms.setPFCPST(tributos.getPercentualFcpSt().toPlainString());
		icms.setVFCPST(fcpst.getValor().toPlainString());
		icms.setVBCFCPST(fcpst.getBaseCalculo().toPlainString());

		IResultadoCalculoFcp fcp = CalculosUtils.calcularFcp(itemProduto);
		icms.setPFCP(tributos.getPercentualFcp().toPlainString());
		icms.setVFCP(fcp.getValor().toPlainString());
		icms.setVBCFCP(fcp.getBaseCalculo().toPlainString());

		icms.setVICMSDeson(icms.getVICMS());
		icms.setMotDesICMS("9");

		return icms;
	}

	public static ICMSPart getICMSPart(ItemProduto itemProduto) {

		ICMSPart icms = new ICMSPart();

		icms.setOrig(itemProduto.getProduto().getOrigemProduto().getCodigo());
		icms.setCST(itemProduto.getCstIcms());
		icms.setModBC("3");

		ITributavel tributos = TributosUtils.getTtibutos(itemProduto);
		Cst90 cst = new Cst90();
		cst.calcular(tributos);

		// icms.setPICMS(itemProduto.getAliquotaIcms().toPlainString());
		// icms.setVBC(itemProduto.getBaseIcms().setScale(2).toPlainString());
		// icms.setVICMS(itemProduto.getValorIcms().setScale(2).toPlainString());
		// icms.setPRedBC(itemProduto.getReducaoBaseCalculoIcms().toPlainString());
		// icms.setPICMSST(itemProduto.getAliquotaIcmsSt().toPlainString());
		// icms.setVBCST(itemProduto.getBaseIcmsSt().setScale(2).toPlainString());
		// icms.setVICMSST(itemProduto.getValorIcmsSt().setScale(2).toPlainString());
		// icms.setPRedBCST(itemProduto.getReducaoBaseCalculoIcmsSt().toPlainString());
		// icms.setModBCST("4");
		// icms.setPMVAST(null);
		// icms.setPBCOp(null);

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

		// // vBCUFDest - Valor da BC do ICMSEntrada na UF de destino
		// icms.setVBCUFDest(ICMSDifal.getVBCUFDest(itemProduto).toPlainString());
		// icms.setVBCFCPUFDest(ICMSDifal.getVBCUFDest(itemProduto).toPlainString());
		// pFCPUFDest - Percentual do ICMSEntrada relativo ao Fundo de Combate
		// à
		// Pobreza (FCP) na UF de destino
		// icms.setPFCPUFDest(itemProduto.getAliquotaFcp().toPlainString());

		// pICMSUFDest - Alíquota interna da UF de destino
		// icms.setPICMSUFDest(itemProduto.getAliquotaInterIcms().toPlainString());

		// pICMSInter - Alíquota interestadual das UF envolvidas
		icms.setPICMSInter(itemProduto.getAliquotaIcms().toPlainString());

		// pICMSInterPart - Percentual provisório de partilha do ICMSEntrada
		// Interestadual
		icms.setPICMSInterPart(new BigDecimal(80).setScale(2).toPlainString());

		// vFCPUFDest - Valor do ICMSEntrada relativo ao Fundo de Combate à
		// Pobreza
		// (FCP) da UF de destino
		// icms.setVFCPUFDest(FCPCalculos.getVFCPUFDest(itemProduto).toPlainString());

		// icms.setVBCFCPUFDest(ICMSEntradaDifal.getVICMSUFDest(item).toPlainString());

		// vICMSUFDest - Valor do ICMSEntrada Interestadual para a UF de destino
		// icms.setVICMSUFDest(ICMSDifal.getVICMSUFDest(itemProduto).toPlainString());

		// vICMSUFRemet - Valor do ICMSEntrada Interestadual para a UF do
		// remetente
		// icms.setVICMSUFRemet(ICMSDifal.getICMSUFRemet(itemProduto).toPlainString());

		return icms;
	}

}
