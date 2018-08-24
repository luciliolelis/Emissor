package com.amsoft.erp.util.icms;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.amsoft.erp.util.TributosUtils;
import com.chronos.calc.CalcTributacao;
import com.chronos.calc.dto.ITributavel;
import com.chronos.calc.resultados.IResultadoCalculoCofins;
import com.chronos.calc.resultados.IResultadoCalculoFcp;
import com.chronos.calc.resultados.IResultadoCalculoFcpSt;
import com.chronos.calc.resultados.IResultadoCalculoIpi;
import com.chronos.calc.resultados.IResultadoCalculoPis;

public class CalculosUtils {

	public static IResultadoCalculoPis calcularPis(ItemProduto item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularPis();
	}

	public static IResultadoCalculoFcp calcularFcp(ItemProduto item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularFcp();
	}

	
	public static IResultadoCalculoFcp calcularFcp(ItemProdutoNFCe item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularFcp();
	}
	
	public static IResultadoCalculoFcpSt calcularFcpSt(ItemProduto item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularFcpSt();
	}

	public static IResultadoCalculoFcpSt calcularFcpSt(ItemProdutoNFCe item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularFcpSt();
	}

	public static IResultadoCalculoCofins calcularCofins(ItemProduto item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularCofins();
	}

	public static IResultadoCalculoPis calcularPis(ItemProdutoNFCe item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularPis();
	}

	public static IResultadoCalculoCofins calcularCofins(ItemProdutoNFCe item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularCofins();
	}

	public static IResultadoCalculoIpi calcularIPI(ItemProduto item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularIpi();
	}

	public static IResultadoCalculoIpi calcularIPI(ItemProdutoNFCe item) {

		ITributavel tributos = TributosUtils.getTtibutos(item);

		CalcTributacao calcular = new CalcTributacao(tributos);

		return calcular.calcularIpi();
	}

}
