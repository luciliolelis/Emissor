package com.amsoft.erp.util;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.fcp.FCPValidacoes;
import com.amsoft.erp.model.nfe.ipi.IPIValidacoes;
import com.amsoft.erp.util.icms.normal.NormalUtils;
import com.amsoft.erp.util.icms.normal.TagNFe;
import com.amsoft.erp.util.icms.simples.SimplesUtils;
import com.chronos.calc.TributacaoException;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.ObjectFactory;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TIpi;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMSUFDest;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Prod;

public class ProdutosUtils {

	private static Imposto getImpostos(ItemProduto item, Nfe nfe) throws TributacaoException {
		Imposto imposto = new Imposto();

		imposto.getContent().add(new ObjectFactory()
				.createTNFeInfNFeDetImpostoVTotTrib(item.getValorTransparencia().setScale(2).toPlainString()));

		ICMS icms = new ICMS();

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {
			icms = SimplesUtils.popularIcmsSN(item);
		} else {
			icms = NormalUtils.popularICMS(item);
		}

		PIS pis = PISCOFINSUtils.popularPIS(item);

		COFINS cofins = PISCOFINSUtils.popularCOFINS(item);

		TIpi ipi = IPIUtils.popularIPI(item);

		JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
		imposto.getContent().add(icmsElement);

		// if (IPIValidacoes.isIpi(item)) {
		JAXBElement<TIpi> ipiElement = new JAXBElement<TIpi>(new QName("IPI"), TIpi.class, ipi);
		imposto.getContent().add(ipiElement);
		// }

		JAXBElement<PIS> pisElement = new JAXBElement<PIS>(new QName("PIS"), PIS.class, pis);
		imposto.getContent().add(pisElement);

		JAXBElement<COFINS> cofinsElement = new JAXBElement<COFINS>(new QName("COFINS"), COFINS.class, cofins);
		imposto.getContent().add(cofinsElement);

		if (FCPValidacoes.isNotFCPUFDest(item)) {

		} else {
			ICMSUFDest icmsufdest = TagNFe.getICMSUFDest(item);
			JAXBElement<ICMSUFDest> icmsufdestElement = new JAXBElement<ICMSUFDest>(new QName("ICMSUFDest"),
					ICMSUFDest.class, icmsufdest);
			imposto.getContent().add(icmsufdestElement);
		}

		return imposto;
	}

	private static Imposto getImpostos(ItemProdutoNFCe item, NFCe nfe) {
		Imposto imposto = new Imposto();

		imposto.getContent().add(new ObjectFactory()
				.createTNFeInfNFeDetImpostoVTotTrib(item.getValorTransparencia().setScale(2).toPlainString()));

		ICMS icms = new ICMS();

		if (AmsoftUtils.isSimplesNacional(nfe.getEmpresa().getRegimeTributario())) {
			icms = SimplesUtils.popularIcmsSN(item);
		} else {
			// icms = SimplesUtils.popularICMS(item);
		}

		PIS pis = PISCOFINSUtils.popularPIS(item);

		COFINS cofins = PISCOFINSUtils.popularCOFINS(item);

		TIpi ipi = IPIUtils.popularIPI(item);

		JAXBElement<ICMS> icmsElement = new JAXBElement<ICMS>(new QName("ICMS"), ICMS.class, icms);
		imposto.getContent().add(icmsElement);

		if (IPIValidacoes.isIpi(item)) {
			JAXBElement<TIpi> ipiElement = new JAXBElement<TIpi>(new QName("IPI"), TIpi.class, ipi);
			imposto.getContent().add(ipiElement);
		}

		JAXBElement<PIS> pisElement = new JAXBElement<PIS>(new QName("PIS"), PIS.class, pis);
		imposto.getContent().add(pisElement);

		JAXBElement<COFINS> cofinsElement = new JAXBElement<COFINS>(new QName("COFINS"), COFINS.class, cofins);
		imposto.getContent().add(cofinsElement);

		// if (FCPValidacoes.isNotFCPUFDest(item)) {
		//
		// } else {
		// ICMSUFDest icmsufdest = SimplesUtils.getICMSUFDest(item);
		// JAXBElement<ICMSUFDest> icmsufdestElement = new
		// JAXBElement<ICMSUFDest>(new QName("ICMSUFDest"),
		// ICMSUFDest.class, icmsufdest);
		// imposto.getContent().add(icmsufdestElement);
		// }

		return imposto;
	}

	private static Prod getProduto(ItemProduto itemProduto, Nfe nfe) {
		Prod produto = new Prod();
		produto.setCProd(itemProduto.getProduto().getSku());

		produto.setXProd(AmsoftUtils.removeCaracteresEspeciais(itemProduto.getProduto().getNome()));

		if (nfe.getEmpresa().getWsAmbiente().equals(2)) {
			produto.setXProd("NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
		}

		if (itemProduto.getProduto().getCodigoBarras().equals("")) {
			produto.setCEAN("SEM GTIN");
		} else {
			produto.setCEAN(itemProduto.getProduto().getCodigoBarras());
		}

		if (itemProduto.getProduto().getNcm() != "") {
			produto.setNCM(itemProduto.getProduto().getNcm());
		}

		if (isCest(itemProduto)) {
			produto.setCEST(AmsoftUtils.removerMascara(itemProduto.getProduto().getCest()));
		}

		produto.setCFOP(itemProduto.getCfop().toString());
		produto.setUCom(itemProduto.getProduto().getUnidadeMedida());
		produto.setUTrib(itemProduto.getProduto().getUnidadeMedida());
		produto.setQCom(itemProduto.getQuantidade().toString());
		produto.setVUnCom(itemProduto.getValorUnitario().toString());
		produto.setVProd(itemProduto.getValorUnitario().multiply(itemProduto.getQuantidade())
				.setScale(2, RoundingMode.HALF_EVEN).toString());
		produto.setQTrib(itemProduto.getQuantidade().toString());
		produto.setVUnTrib(itemProduto.getValorUnitario().toString());

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorFrete())) {
			produto.setVFrete(itemProduto.getValorFrete().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorSeguro())) {
			produto.setVSeg(itemProduto.getValorSeguro().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorDesconto())) {
			produto.setVDesc(itemProduto.getValorDesconto().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorDespesa())) {
			produto.setVOutro(itemProduto.getValorDespesa().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		produto.setIndTot("1");

		if (itemProduto.getProduto().getCodigoBarras().equals("")) {
			produto.setCEANTrib("SEM GTIN");
		} else {
			produto.setCEANTrib(itemProduto.getProduto().getCodigoBarras());
		}

		return produto;
	}

	private static Prod getProduto(ItemProdutoNFCe itemProduto, NFCe nfe) {
		Prod produto = new Prod();
		produto.setCProd(itemProduto.getProduto().getSku());

		produto.setXProd(AmsoftUtils.removeCaracteresEspeciais(itemProduto.getProduto().getNome()));

		if (nfe.getEmpresa().getWsAmbiente().equals(2)) {
			produto.setXProd("NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
		}

		if (itemProduto.getProduto().getCodigoBarras().equals("")) {
			produto.setCEAN("SEM GTIN");
		} else {
			produto.setCEAN(itemProduto.getProduto().getCodigoBarras());
		}

		if (itemProduto.getProduto().getNcm() != "") {
			produto.setNCM(itemProduto.getProduto().getNcm());
		}

		if (isCest(itemProduto)) {
			produto.setCEST(AmsoftUtils.removerMascara(itemProduto.getProduto().getCest()));
		}

		produto.setCFOP(itemProduto.getCfop().toString());
		produto.setUCom(itemProduto.getProduto().getUnidadeMedida());
		produto.setUTrib(itemProduto.getProduto().getUnidadeMedida());
		produto.setQCom(itemProduto.getQuantidade().toString());
		produto.setVUnCom(itemProduto.getValorUnitario().toPlainString());
		produto.setVProd(itemProduto.getValorUnitario().multiply(itemProduto.getQuantidade())
				.setScale(2, RoundingMode.HALF_EVEN).toString());
		produto.setQTrib(itemProduto.getQuantidade().toString());
		produto.setVUnTrib(itemProduto.getValorUnitario().toPlainString());

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorFrete())) {
			produto.setVFrete(itemProduto.getValorFrete().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorSeguro())) {
			produto.setVSeg(itemProduto.getValorSeguro().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorDesconto())) {
			produto.setVDesc(itemProduto.getValorDesconto().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		if (AmsoftUtils.isMaiorQZero(itemProduto.getValorDespesa())) {
			produto.setVOutro(itemProduto.getValorDespesa().setScale(2, RoundingMode.HALF_EVEN).toString());
		}

		produto.setIndTot("1");

		if (itemProduto.getProduto().getCodigoBarras().equals("")) {
			produto.setCEANTrib("SEM GTIN");
		} else {
			produto.setCEANTrib(itemProduto.getProduto().getCodigoBarras());
		}

		return produto;
	}

	public static ArrayList<Det> getProdutos(List<ItemProduto> produtos, Nfe nfe) throws TributacaoException {

		ArrayList<Det> ret = new ArrayList<Det>();

		Integer index = 1;

		for (ItemProduto item : produtos) {
			if (isProdutoValido(item)) {
				Det det = new Det();
				det.setNItem(index.toString());
				index++;

				det.setProd(getProduto(item, nfe));
				det.setImposto(getImpostos(item, nfe));
				ret.add(det);
			}
		}

		return ret;
	}

	public static ArrayList<Det> getProdutos(List<ItemProdutoNFCe> produtos, NFCe nfe) {

		ArrayList<Det> ret = new ArrayList<Det>();

		Integer index = 1;

		for (ItemProdutoNFCe item : produtos) {
			if (isProdutoValido(item)) {
				Det det = new Det();
				det.setNItem(index.toString());
				index++;

				det.setProd(getProduto(item, nfe));
				det.setImposto(getImpostos(item, nfe));
				ret.add(det);
			}
		}

		return ret;
	}

	private static boolean isCest(ItemProduto item) {
		return !isNotCest(item);
	}

	private static boolean isCest(ItemProdutoNFCe item) {
		return !isNotCest(item);
	}

	private static boolean isNotCest(ItemProdutoNFCe item) {
		return item.getProduto().getCest().equals("") || item.getProduto().getCest().equals(null);
	}

	private static boolean isNotCest(ItemProduto item) {
		return item.getProduto().getCest().equals("") || item.getProduto().getCest().equals(null);
	}

	public static boolean isProdutoValido(ItemProduto item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	public static boolean isProdutoValido(ItemProdutoNFCe item) {
		return item.getProduto() != null && item.getProduto().getId() != null;
	}

	public boolean isOrigemNacional(String origem) {
		return origem.equals("0") || origem.equals("3") || origem.equals("4") || origem.equals("5");
	}

}
