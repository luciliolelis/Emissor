package com.amsoft.erp.util;

import javax.inject.Inject;

import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.dom.ConfiguracoesIniciaisNfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;

public class CertificadoInit {



	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public ConfiguracoesIniciaisNfe iniciaConfiguracoes() throws NfeException, CertificadoException {

		String caminho = AmsoftUtils.getRaiz() + "resources/"
				+ usuarioLogado.getUsuario().getEmpresa().getNomeCertificado();

		String senha = usuarioLogado.getUsuario().getEmpresa().getSenhaCertificado();

		Certificado certificado = CertificadoService.certificadoPfx(caminho, senha);

		if (usuarioLogado.getUsuario().getEmpresa().getWsAmbiente().equals(2)) {

			return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(
					Estados.valueOf(usuarioLogado.getUsuario().getEmpresa().getUf()),
					ConstantesUtil.AMBIENTE.HOMOLOGACAO, certificado, "C://schemas");
		} else {

			return ConfiguracoesIniciaisNfe.iniciaConfiguracoes(
					Estados.valueOf(usuarioLogado.getUsuario().getEmpresa().getUf()), ConstantesUtil.AMBIENTE.PRODUCAO,
					certificado, "C://schemas");
		}

	}

}