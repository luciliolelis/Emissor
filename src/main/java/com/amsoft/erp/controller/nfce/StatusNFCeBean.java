package com.amsoft.erp.controller.nfce;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;

@Named
@RequestScoped
public class StatusNFCeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@NFCeEdicao
	private NFCe nfe;

	@Inject
	private CertificadoInit init;

	public void status() throws Exception {

		try {

			init.iniciaConfiguracoes();
			String chave = this.nfe.getChave().replaceAll("NFe", "");
			TRetConsSitNFe retorno = br.com.samuelweb.nfe.Nfe.consultaXml(chave, ConstantesUtil.NFCE);

			FacesUtil.addInfoMessage("Status: " + retorno.getCStat());
			FacesUtil.addInfoMessage("Motivo: " + retorno.getXMotivo());
			FacesUtil.addInfoMessage("Data: " + retorno.getProtNFe().getInfProt().getDhRecbto());
		} catch (CertificadoException | NfeException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}

}
