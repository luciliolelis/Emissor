package com.amsoft.erp.service.inutilizacao;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TProcInutNFe;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe;

public class XMLInutilizacaoNFeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	private CertificadoInit init;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	public Nfe enviar(Nfe nfe) throws JAXBException {

		nfe = this.nfes.porId(nfe.getId());

		try {

			init.iniciaConfiguracoes();

			String id = getID(usuarioLogado.getUsuario().getEmpresa(), nfe.getNumero().toString(),
					nfe.getNumero().toString());

			String motivo = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

			TRetInutNFe retorno = br.com.samuelweb.nfe.Nfe.inutilizacao(id, motivo, ConstantesUtil.NFE, true);
			TRetInutNFe.InfInut infRetorno = retorno.getInfInut();

			// Criação do ProcInutNfe
			TProcInutNFe procInutNFe = new TProcInutNFe();
			procInutNFe.setInutNFe(br.com.samuelweb.nfe.Nfe.criaObjetoInutilizacao(id, motivo, ConstantesUtil.NFE));
			procInutNFe.setRetInutNFe(retorno);
			procInutNFe.setVersao(ConstantesUtil.VERSAO.INUTILIZACAO);

			if (retorno.getInfInut().getXMotivo().equals("Um numero da faixa ja foi utilizado")) {
				nfe.setStatus(StatusNFe.FALHA);
				throw new NfeException(
						"Status:" + retorno.getInfInut().getCStat() + " - Motivo:" + retorno.getInfInut().getXMotivo());
			}

			System.out.println(XmlUtil.objectToXml(procInutNFe));

			String xmlEnvio = XmlUtil.objectToXml(procInutNFe);

			AmsoftUtils.info(AmsoftUtils.formataXML(xmlEnvio));

			nfe.setEnviNFe(AmsoftUtils.formataXML(xmlEnvio));

			FacesUtil.addInfoMessage("NF-e Inutilizada com sucesso");
			FacesUtil.addInfoMessage("Status:" + infRetorno.getCStat());
			FacesUtil.addInfoMessage("Motivo:" + infRetorno.getXMotivo());
			FacesUtil.addInfoMessage("Data:" + infRetorno.getDhRecbto());
			nfe.setStatus(StatusNFe.INUTILIZADA);

		} catch (CertificadoException | NfeException e) {
			AmsoftUtils.error(e.getMessage());
			FacesUtil.addErrorMessage("Evento: Inutilização NF-e");
			FacesUtil.addErrorMessage(e.getMessage());
		}

		return nfe;
	}

	String getID(Empresa empresa, String numIni, String numFin) {

		String codigoDoEstado = empresa.getIbgeEstado();
		String ano = AmsoftUtils.getAnoDuasPosicoes(new Date());
		String cnpj = AmsoftUtils.removerMascara(empresa.getCnpj());
		String modelo = "55";
		String serie = "001";
		String ini = AmsoftUtils.lpadTo(numIni, 9, '0');
		String fim = AmsoftUtils.lpadTo(numFin, 9, '0');

		String id = "ID" + codigoDoEstado + ano + cnpj + modelo + serie + ini + fim;

		System.out.println(id.length());
		return id;
	}

}
