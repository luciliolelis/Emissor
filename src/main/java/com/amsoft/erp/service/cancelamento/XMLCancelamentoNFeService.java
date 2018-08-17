package com.amsoft.erp.service.cancelamento;

import java.io.Serializable;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.IDUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;

import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;

public class XMLCancelamentoNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	private CertificadoInit init;

	public Nfe enviar(Nfe nfe) {

		nfe = this.nfes.porId(nfe.getId());

		try {

			init.iniciaConfiguracoes();

			String chave = nfe.getChave().replaceAll("NFe", "");
			String protocolo = nfe.getProtocolo();
			String cnpj = AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj());
			String motivo = "Cancelamento de nota fiscal";

			String id = "ID" + ConstantesUtil.EVENTO.CANCELAR + chave + "01";

			TEnvEvento enviEvento = new TEnvEvento();
			enviEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
			enviEvento.setIdLote("1");

			TEvento eventoCancela = new TEvento();
			eventoCancela.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);

			TEvento.InfEvento infoEvento = new TEvento.InfEvento();
			infoEvento.setId(id);
			infoEvento.setChNFe(chave);
			infoEvento.setCOrgao(String.valueOf(nfe.getEmpresa().getIbgeEstado()));
			infoEvento.setTpAmb(IDUtils.getAmbiente(nfe.getEmpresa()));
			infoEvento.setCNPJ(cnpj);

			infoEvento.setDhEvento(XmlUtil.dataNfe());
			infoEvento.setTpEvento(ConstantesUtil.EVENTO.CANCELAR);
			infoEvento.setNSeqEvento("1");
			infoEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);

			TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
			detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
			detEvento.setDescEvento("Cancelamento");
			detEvento.setNProt(protocolo);
			detEvento.setXJust(motivo);
			infoEvento.setDetEvento(detEvento);
			eventoCancela.setInfEvento(infoEvento);
			enviEvento.getEvento().add(eventoCancela);

			TRetEnvEvento retorno = br.com.samuelweb.nfe.Nfe.cancelarNfe(enviEvento, false, ConstantesUtil.NFE);

			if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
				nfe.setStatus(StatusNFe.FALHA);
				throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
			}

			if (!StatusEnum.EVENTO_VINCULADO.getCodigo()
					.equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {

				nfe.setStatus(StatusNFe.FALHA);
				throw new NfeException("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat()
						+ " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
			}

			FacesUtil.addInfoMessage("NF-e Cancelada com sucesso");
			FacesUtil.addInfoMessage("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat());
			FacesUtil.addInfoMessage("Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
			FacesUtil.addInfoMessage("Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento());

			// Cria TProcEventoNFe
			TProcEvento procEvento = new TProcEvento();
			procEvento.setVersao("1.00");
			procEvento.setEvento(enviEvento.getEvento().get(0));
			procEvento.setRetEvento(retorno.getRetEvento().get(0));

			nfe.setStatus(StatusNFe.CANCELADA);

			nfe.setEnviNFe(AmsoftUtils.formataXML(XmlUtil.objectToXml(procEvento)));

			AmsoftUtils.info(XmlUtil.objectToXml(procEvento));

		} catch (CertificadoException | NfeException | JAXBException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
		return nfe;
	}

}
