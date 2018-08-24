package com.amsoft.erp.util.icms.simples;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;

public class CsosnUtils {

	/**
	 * REGIME: Simples Nacional Verifica qual é a CSOSN selecionada no item de
	 * produdo pra escrita do XML
	 */

	/*
	 * NF-e
	 */


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

	/*
	 * NFC-e
	 */

	
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

}
