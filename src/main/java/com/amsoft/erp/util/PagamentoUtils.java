package com.amsoft.erp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.amsoft.erp.controller.xml.ValidadorXml;
import com.amsoft.erp.model.nfce.FormaPagamentoNFCe;
import com.amsoft.erp.model.nfce.ItemDuplicataNFCe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.FormaPagamento;
import com.amsoft.erp.model.nfe.ItemDuplicata;
import com.amsoft.erp.model.nfe.Nfe;

import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr.Dup;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr.Fat;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Pag;

public class PagamentoUtils {

	public static Pag getPagamento(Nfe nfe) {
		Pag pag = new Pag();

		if (ValidadorXml.isDevolucao(nfe)) {
			Pag.DetPag detPag = new Pag.DetPag();
			detPag.setTPag("90");
			detPag.setVPag("0.00");
			pag.getDetPag().add(detPag);

			return pag;
		}

		Pag.DetPag detPag = new Pag.DetPag();
		detPag.setTPag(getTipoPagamento(nfe.getFormaPagamento()));
		detPag.setVPag(nfe.getValorTotal().setScale(2).toPlainString());
		pag.getDetPag().add(detPag);

		return pag;
	}

	public static Pag getPagamento(NFCe nfe) {
		Pag pag = new Pag();

		if (IDUtils.getFinalidadeEmissao(nfe).equals("4")) {
			Pag.DetPag detPag = new Pag.DetPag();
			detPag.setTPag("90");
			detPag.setVPag(nfe.getValorTotal().setScale(2).toPlainString());
			pag.getDetPag().add(detPag);

			return pag;
		}

		for (ItemDuplicataNFCe item : nfe.getItensDuplicatas()) {

			if (item.getFormaPagamento() != null) {
				Pag.DetPag detPag = new Pag.DetPag();
				detPag.setTPag(getTipoPagamento(item.getFormaPagamento()));
				detPag.setVPag(item.getValor().setScale(2).toPlainString());
				pag.getDetPag().add(detPag);

			}

		}

		return pag;
	}

	public static Cobr getCobranca(Nfe nfe) {
		Cobr cob = new Cobr();

		cob.setFat(getFatura(nfe));

		for (ItemDuplicata item : nfe.getItensDuplicatas()) {
			Dup dup = new Dup();
			dup.setNDup(item.getNumeroDuplicata());
			dup.setDVenc(formataData(item.getDataVencimento()));
			dup.setVDup(item.getValorParcela().setScale(2).toPlainString());
			cob.getDup().add(dup);
		}

		return cob;
	}

	public static Cobr getCobranca(NFCe nfe) {
		Cobr cob = new Cobr();

		cob.setFat(getFatura(nfe));

		// for (ItemDuplicataNFCe item : nfe.getItensDuplicatas()) {
		// Dup dup = new Dup();
		// dup.setNDup(item.getNumeroDuplicata());
		// dup.setDVenc(formataData(item.getDataVencimento()));
		// dup.setVDup(item.getValorParcela().setScale(2).toPlainString());
		// cob.getDup().add(dup);
		// }

		return cob;
	}

	public static Fat getFatura(Nfe nfe) {
		Fat fat = new Fat();
		fat.setNFat(nfe.getNumero().toString());
		fat.setVOrig(nfe.getValorTotal().add(nfe.getValorDesconto()).setScale(2).toPlainString());

		fat.setVDesc("0.00");
		if (AmsoftUtils.isNotBigDecimalZeroOrNull(nfe.getValorDesconto())) {
			fat.setVDesc(nfe.getValorDesconto().setScale(2).toPlainString());
		}

		fat.setVLiq(nfe.getValorTotal().setScale(2).toPlainString());
		return fat;
	}

	public static Fat getFatura(NFCe nfe) {
		Fat fat = new Fat();
		fat.setNFat(nfe.getNumero().toString());
		fat.setVOrig(nfe.getValorTotal().add(nfe.getValorDesconto()).setScale(2).toPlainString());

		if (AmsoftUtils.isNotBigDecimalZeroOrNull(nfe.getValorDesconto())) {
			fat.setVDesc(nfe.getValorDesconto().setScale(2).toPlainString());
		}

		fat.setVLiq(nfe.getValorTotal().setScale(2).toPlainString());
		return fat;
	}

	public static boolean isAPrazo(Nfe nfe) {
		return getTipoPagamento(nfe.getFormaPagamento()).equals("14");
	}

	public static boolean isAPrazo(NFCe nfe) {
		return getTipoPagamento(nfe.getFormaPagamento()).equals("14");
	}

	private static String getTipoPagamento(FormaPagamento pagamento) {

		if (pagamento.equals(FormaPagamento.DINHEIRO)) {
			return "01";
		} else if (pagamento.equals(FormaPagamento.CHEQUE)) {
			return "02";
		} else if (pagamento.equals(FormaPagamento.CARTAO_CREDITO)) {
			return "03";
		} else if (pagamento.equals(FormaPagamento.CARTAO_DEBITO)) {
			return "04";
		} else if (pagamento.equals(FormaPagamento.CREDITO_LOJA)) {
			return "05";
		} else if (pagamento.equals(FormaPagamento.VALE_ALIMENTACAO)) {
			return "10";
		} else if (pagamento.equals(FormaPagamento.VALE_REFEICAO)) {
			return "11";
		} else if (pagamento.equals(FormaPagamento.VALE_PRESENTE)) {
			return "12";
		} else if (pagamento.equals(FormaPagamento.VALE_COMBUSTIVEL)) {
			return "13";
		} else if (pagamento.equals(FormaPagamento.DUPLICATA_MERCANTIL)) {
			return "14";
		} else {
			return "99";
		}
	}

	private static String getTipoPagamento(FormaPagamentoNFCe pagamento) {

		if (pagamento.equals(FormaPagamentoNFCe.DINHEIRO)) {
			return "01";
		} else if (pagamento.equals(FormaPagamentoNFCe.CHEQUE)) {
			return "02";
		} else if (pagamento.equals(FormaPagamentoNFCe.CARTAO_CREDITO)) {
			return "03";
		} else if (pagamento.equals(FormaPagamentoNFCe.CARTAO_DEBITO)) {
			return "04";
		} else if (pagamento.equals(FormaPagamentoNFCe.CREDITO_LOJA)) {
			return "05";
		} else if (pagamento.equals(FormaPagamentoNFCe.VALE_ALIMENTACAO)) {
			return "10";
		} else if (pagamento.equals(FormaPagamentoNFCe.VALE_REFEICAO)) {
			return "11";
		} else if (pagamento.equals(FormaPagamentoNFCe.VALE_PRESENTE)) {
			return "12";
		} else if (pagamento.equals(FormaPagamentoNFCe.VALE_COMBUSTIVEL)) {
			return "13";
		} else {
			return "99";
		}
	}

	static String formataData(Date data) {
		String ret = "";
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			ret = dt.format(data);
		} catch (Exception e) {
			ret = "";
		}

		return ret;
	}

}
