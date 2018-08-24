package com.amsoft.erp.util;

import java.math.BigDecimal;

import com.amsoft.erp.model.nfce.ItemProdutoNFCe;
import com.amsoft.erp.model.nfe.ItemProduto;
import com.chronos.calc.dto.ITributavel;

public class TributosUtils {

	/**
	 * Seta as configurações de impostos do item de produto para a NF-e
	 * 
	 * @param ItemProduto
	 * @return ITributavel
	 */
	public static ITributavel getTtibutos(ItemProduto item) {
		ITributavel tributos = new ITributavel();

		// PRODUTO
		tributos.setValorProduto(item.getValorUnitario().setScale(2));
		tributos.setQuantidadeProduto(item.getQuantidade().setScale(2));

		// ICMS
		tributos.setPercentualIcms(item.getAliquotaIcms());
		tributos.setPercentualIcmsSt(item.getAliquotaIcmsSt());
		tributos.setPercentualMva(item.getMva());
		tributos.setPercentualReducaoSt(item.getReducaoBaseCalculoIcmsSt());
		tributos.setPercentualReducao(item.getReducaoBaseCalculoIcms());

		// CREDITO ICMS - OBS. este valor é temporário apenas para efeito de
		// calculo até que a configuração no emitente esteja pronta
		tributos.setPercentualCredito(BigDecimal.valueOf(1.47));

		// DESPESAS
		tributos.setDesconto(item.getValorDesconto().setScale(2));
		tributos.setFrete(item.getValorFrete().setScale(2));
		tributos.setSeguro(item.getValorSeguro().setScale(2));
		tributos.setOutrasDespesas(item.getValorDespesa().setScale(2));

		// FCP
		tributos.setPercentualFcp(new BigDecimal(2));
		tributos.setPercentualFcpSt(new BigDecimal(2));

		tributos.setPercentualIpi(item.getAliquotaIpi());
		tributos.setPercentualPis(item.getAliquotaPis());
		tributos.setPercentualCofins(item.getAliquotaCofins());

		return tributos;
	}

	/**
	 * Seta as configurações de impostos do item de produto para a NFC-e
	 * 
	 * @param ItemProduto
	 * @return ITributavel
	 */
	public static ITributavel getTtibutos(ItemProdutoNFCe item) {
		ITributavel tributos = new ITributavel();

		// PRODUTO
		tributos.setValorProduto(item.getValorUnitario());
		tributos.setQuantidadeProduto(item.getQuantidade());

		// ICMS
		tributos.setPercentualIcms(item.getAliquotaIcms());
		tributos.setPercentualMva(item.getMva());

		// CREDITO ICMS - OBS. este valor é temporário apenas para efeito de
		// calculo até que a configuração no emitente esteja pronta
		tributos.setPercentualCredito(BigDecimal.valueOf(1.47));

		// DESPESAS
		tributos.setDesconto(item.getValorDesconto());
		tributos.setFrete(item.getValorFrete());
		tributos.setSeguro(item.getValorSeguro());
		tributos.setOutrasDespesas(item.getValorDespesa());

		// FCP
		tributos.setPercentualFcp(new BigDecimal(2));
		tributos.setPercentualFcpSt(new BigDecimal(2));

		tributos.setPercentualIpi(item.getAliquotaIpi());
		tributos.setPercentualPis(item.getAliquotaPis());
		tributos.setPercentualCofins(item.getAliquotaCofins());

		return tributos;
	}
}
