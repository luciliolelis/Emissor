package com.amsoft.erp.service.nfce;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.jpa.Transactional;

public class CadastroNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFCes nfces;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Transactional
	public NFCe salvar(NFCe nfce) {

		if (nfce.getUsuario() == null)
			nfce.setUsuario(this.usuarioLogado.getUsuario());

		nfce.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());

		if (nfce.getItensDuplicatas().isEmpty()) {
			throw new NegocioException("Informe a forma de pagamento");
		}
		
		if (nfce.getValorTotalNota().equals(BigDecimal.ZERO.setScale(2))) {
			throw new NegocioException("O valor total da nota fiscal deve ser maior que zero");
		}
		
		if (AmsoftUtils.isMenorQZero(nfce.getValorTotalNota())) {
			throw new NegocioException("O valor total da nota fiscal deve ser maior que zero");
		}
		
		return nfces.guardar(nfce);
	}

	@Transactional
	public void excluir(NFCe nfce) {
		nfces.remover(nfce);
	}

}