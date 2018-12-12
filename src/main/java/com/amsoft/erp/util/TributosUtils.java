package com.amsoft.erp.util;

import java.math.BigDecimal;
import java.util.List;

import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.emitente.FundoCombatePobreza;
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
		tributos.setValorProduto(item.getValorUnitario());
		tributos.setQuantidadeProduto(item.getQuantidade());

		// ICMS
		tributos.setPercentualIcms(item.getAliquotaIcms());
		tributos.setPercentualIcmsSt(item.getAliquotaIcmsSt());
		tributos.setPercentualMva(item.getMva());
		tributos.setPercentualReducaoSt(item.getReducaoBaseCalculoIcmsSt());
		tributos.setPercentualReducao(item.getReducaoBaseCalculoIcms());
		tributos.setPercentualCredito(item.getNfe().getEmpresa().getAliquotaCreditoIcms());

		// DESPESAS
		tributos.setDesconto(item.getValorDesconto());
		tributos.setFrete(item.getValorFrete());
		tributos.setSeguro(item.getValorSeguro());
		tributos.setOutrasDespesas(item.getValorDespesa());

		// FCP
		FundoCombatePobreza fcp = getFcp(item.getNfe().getEmpresa());

		if (fcp != null) {
			tributos.setPercentualFcp(fcp.getAliquotaFcp());
			tributos.setPercentualFcpSt(fcp.getAliquotaFcpSt());
		} else {
			tributos.setPercentualFcp(BigDecimal.ZERO);
			tributos.setPercentualFcpSt(BigDecimal.ZERO);
		}

		tributos.setPercentualIpi(item.getAliquotaIpi());
		tributos.setPercentualPis(item.getAliquotaPis());
		tributos.setPercentualCofins(item.getAliquotaCofins());

		return tributos;
	}

	private static FundoCombatePobreza getFcp(Empresa empresa) {
		String uf = empresa.getUf();
		List<FundoCombatePobreza> lista = empresa.getFundoCombatePobrezaItens();

		for (FundoCombatePobreza item : lista) {
			if (uf.equals(item.getUf().getUf())) {
				AmsoftUtils.info(item.getUf().getUf());
				return item;
			}
		}

		return new FundoCombatePobreza();
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
		tributos.setPercentualIcmsSt(item.getAliquotaIcmsSt());
		tributos.setPercentualMva(item.getMva());
		tributos.setPercentualReducaoSt(item.getReducaoBaseCalculoIcmsSt());
		tributos.setPercentualReducao(item.getReducaoBaseCalculoIcms());
		tributos.setPercentualCredito(item.getNfce().getEmpresa().getAliquotaCreditoIcms());

		// DESPESAS
		tributos.setDesconto(item.getValorDesconto());
		tributos.setFrete(item.getValorFrete());
		tributos.setSeguro(item.getValorSeguro());
		tributos.setOutrasDespesas(item.getValorDespesa());

		// FCP
		FundoCombatePobreza fcp = getFcp(item.getNfce().getEmpresa());

		if (fcp != null) {
			tributos.setPercentualFcp(fcp.getAliquotaFcp());
			tributos.setPercentualFcpSt(fcp.getAliquotaFcpSt());
		} else {
			tributos.setPercentualFcp(BigDecimal.ZERO);
			tributos.setPercentualFcpSt(BigDecimal.ZERO);
		}

		tributos.setPercentualIpi(item.getAliquotaIpi());
		tributos.setPercentualPis(item.getAliquotaPis());
		tributos.setPercentualCofins(item.getAliquotaCofins());

		return tributos;
	}
}
