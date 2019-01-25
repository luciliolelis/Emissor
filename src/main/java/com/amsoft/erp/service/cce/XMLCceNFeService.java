package com.amsoft.erp.service.cce;

import java.io.Serializable;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.dom.ConfiguracoesNfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento;

public class XMLCceNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	private CertificadoInit init;

	public Nfe enviar(Nfe nfe) {

		nfe = this.nfes.porId(nfe.getId());

		try {

			ConfiguracoesNfe config = init.iniciaConfiguracoes();

			String chave = nfe.getChave().replaceAll("NFe", "");
			String cnpj = AmsoftUtils.removerMascara(nfe.getEmpresa().getCnpj());
			String sequencia;
			String motivo = AmsoftUtils.removeCaracteresEspeciais(nfe.getCce());

			if (nfe.getSeqcce() == null) {
				nfe.setSeqcce(1);
			}

//			if (nfe.getSeqcce().toString().length() == 1) {
//				sequencia = "0" + nfe.getSeqcce();
//			} else {
//				sequencia = nfe.getSeqcce().toString();
//			}

			sequencia = nfe.getSeqcce().toString();
			String id = "ID" + ConstantesUtil.EVENTO.CCE + chave
					+ (sequencia.length() < 2 ? "0" + sequencia : sequencia);

			nfe.setSeqcce(nfe.getSeqcce() + 1);

			TEnvEvento envEvento = new TEnvEvento();
			envEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);
			envEvento.setIdLote("1");

			br.inf.portalfiscal.nfe.schema.envcce.TEvento evento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento();
			evento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

			br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento infEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento();
			infEvento.setId(id);
			infEvento.setCOrgao(config.getEstado().getCodigoIbge());
			infEvento.setTpAmb(config.getAmbiente());

			infEvento.setCNPJ(cnpj);
			infEvento.setChNFe(chave);

			// Altere a Data
			infEvento.setDhEvento(XmlUtil.dataNfe());
			infEvento.setTpEvento(ConstantesUtil.EVENTO.CCE);
			infEvento.setNSeqEvento(sequencia);
			infEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CCE);

			br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento detEvento = new br.inf.portalfiscal.nfe.schema.envcce.TEvento.InfEvento.DetEvento();
			detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);
			detEvento.setDescEvento("Carta de Correcao");

			// Informe a Correção
			detEvento.setXCorrecao(motivo);
			detEvento.setXCondUso(
					"A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
			infEvento.setDetEvento(detEvento);
			evento.setInfEvento(infEvento);
			envEvento.getEvento().add(evento);

			TRetEnvEvento retorno = br.com.samuelweb.nfe.Nfe.cce(envEvento, false, ConstantesUtil.NFE);

			if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
				FacesUtil.addErrorMessage("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
				throw new NfeException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
			}

			if (!StatusEnum.EVENTO_VINCULADO.getCodigo()
					.equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {

				FacesUtil.addErrorMessage("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat()
						+ " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());

				throw new NfeException("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat()
						+ " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
			}

			FacesUtil.addInfoMessage("NF-e Corrigida com sucesso");
			FacesUtil.addInfoMessage("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat());
			FacesUtil.addInfoMessage("Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
			FacesUtil.addInfoMessage("Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento());

			// Criação do ProcEventoNFe
			TProcEvento procEvento = new TProcEvento();
			procEvento.setEvento(envEvento.getEvento().get(0));
			procEvento.setRetEvento(retorno.getRetEvento().get(0));
			procEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CCE);

			String xmlProcEventoNfe = XmlUtil.objectToXml(procEvento);
			System.out.println(xmlProcEventoNfe);
			nfe.setStatus(StatusNFe.AUTORIZADACORRECAO);

		} catch (CertificadoException | NfeException | JAXBException e) {
			System.err.println(e.getMessage());
		}

		return nfe;
	}

}
