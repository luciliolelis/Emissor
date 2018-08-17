package com.amsoft.erp.service.nfe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.amsoft.erp.model.nfe.FormaPagamento;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Transactional
	public Nfe salvar(Nfe nfe) {

		if (nfe.getUsuario() == null)
			nfe.setUsuario(this.usuarioLogado.getUsuario());

		nfe.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());

		String validacoes = this.getValidacoes(nfe);

		if (!validacoes.equals("")) {
			throw new NegocioException(validacoes);
		}

		return nfes.guardar(nfe);
	}

	@Transactional
	public void excluir(Nfe nfe) {
		nfes.remover(nfe);
	}

	private String getValidacoes(Nfe nfe) {

		if (nfe.isNovo()) {
			// nfe.setStatus(StatusNFe.CADASTRO);
		}

		nfe.recalcularValorTotal();

		// if (nfe.isNaoAlteravel()) {
		// throw new NegocioException("NFe não pode ser alterada no status " +
		// nfe.getStatus().getDescricao() + ".");
		// }

		if (nfe.getValorTotalNota().equals(BigDecimal.ZERO.setScale(2))) {
			return "O valor total da nota fiscal deve ser maior que zero";
		}
		
		if (AmsoftUtils.isMenorQZero(nfe.getValorTotalNota())) {
			throw new NegocioException("O valor total da nota fiscal deve ser maior que zero");
		}

		if (nfe.getEnderecoEntrega() == null && this.isNotExportacao(nfe))
			return "Endereço do destinatário é obrigatório";

		if (this.isTransportadorObrigatorio(nfe)) {
			if (nfe.getEnderecoTransportador().getCepTransportador() == null)
				return "Endereço do tranportador é obrigatório!";

			if (nfe.getVeiculoEntrega().getPlaca() == null)
				return "Veículo do transporte é obrigatório";
		}

		if (this.isNotNumeroNfeDisponivel(nfe))
			return "Numero da Nfe já cadastrado";

		if (this.isAPrazo(nfe)) {
			if (comprarTotalDuplicatas(nfe)) {
				return "O valor total da Nfe não está compatível com a soma das duplicatas";
			}
		}

		return "";
	}

	private boolean isAPrazo(Nfe nfe) {
		return nfe.getFormaPagamento() != null && nfe.getFormaPagamento() == FormaPagamento.DUPLICATA_MERCANTIL;
	}

	private boolean comprarTotalDuplicatas(Nfe nfe) {
		return !nfe.getValorTotalNota().equals(nfe.getValorTotalParcelas());
	}

	private boolean isNotNumeroNfeDisponivel(Nfe nfe) {
		return !this.isNumeroNfeDisponivel(nfe);
	}

	boolean isTransportadorObrigatorio(Nfe nfe) {
		return nfe.getModalidadeFrete().equals("DESTINATARIO");
	}

	private boolean isNumeroNfeDisponivel(Nfe nfe) {
		List<Nfe> nfeTodos = nfes.porNumeroSerie(nfe);
		return nfeTodos.isEmpty();
	}

	public boolean isExportacao(Nfe nfe) {
		return this.isClienteValido(nfe)
				&& (nfe.getCliente().getEstrangeiro().equals(true) || nfe.getCliente().getExterior().equals(true));
	}

	public boolean isNotExportacao(Nfe nfe) {
		return !this.isExportacao(nfe);
	}

	private boolean isClienteValido(Nfe nfe) {
		return this.isNfeValida(nfe) && nfe.getCliente() != null;
	}

	private boolean isNfeValida(Nfe nfe) {
		return nfe != null;
	}

}