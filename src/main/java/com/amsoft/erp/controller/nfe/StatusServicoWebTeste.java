package com.amsoft.erp.controller.nfe;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.exception.NfeException;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;

@Named
@RequestScoped
public class StatusServicoWebTeste implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CertificadoInit init;

	public void status(String tipoNota) throws Exception {

		try {

			init.iniciaConfiguracoes();

			TRetConsStatServ retorno = Nfe.statusServico(tipoNota);
			FacesUtil.addInfoMessage(tipoNota + " " + retorno.getVersao() + ": " + retorno.getXMotivo());

		} catch (CertificadoException | NfeException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}


	
 
}
