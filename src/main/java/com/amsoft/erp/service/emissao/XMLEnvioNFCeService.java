package com.amsoft.erp.service.emissao;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.util.AmsoftUtils;
import com.amsoft.erp.util.CertificadoInit;
import com.amsoft.erp.util.ChaveAcesso;
import com.amsoft.erp.util.DestinatarioUtils;
import com.amsoft.erp.util.EmitenteUtils;
import com.amsoft.erp.util.IDUtils;
import com.amsoft.erp.util.PagamentoUtils;
import com.amsoft.erp.util.ProdutosUtils;
import com.amsoft.erp.util.icms.ICMSTotalUtils;
import com.amsoft.erp.util.jsf.FacesUtil;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.Nfe;
import br.com.samuelweb.nfe.dom.ConfiguracoesNfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.NFCeUtil;
import br.com.samuelweb.nfe.util.WebServiceUtil;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Dest;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Emit;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Ide;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.InfAdic;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Pag;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Total;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;

public class XMLEnvioNFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFCes nfes;

	@Inject
	private AmsoftUtils amsoftUtils;

	@Inject
	private CertificadoInit init;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	String idToken;
	String csc;
	
	
	private String bookName;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public NFCe enviar(NFCe nfe) throws FileNotFoundException {

		nfe = this.nfes.porId(nfe.getId());

		try {

			ConfiguracoesNfe config = init.iniciaConfiguracoes();

			TNFe tnfe = new TNFe();
			InfNFe infNFe = new InfNFe();
			infNFe.setId(ChaveAcesso.gerarChave(nfe, ConstantesUtil.NFCE));
			infNFe.setVersao("4.00");

			// Informações NFCe
			Ide ide = new Ide();
			ide.setCUF(nfe.getUsuario().getEmpresa().getIbgeEstado());
			ide.setCNF(ChaveAcesso.getCodigoChave(infNFe.getId()));
			ide.setNatOp(nfe.getCfop() == null ? "" : nfe.getCfop().getCodigo().toString());
			ide.setMod("65");
			ide.setSerie("1");
			ide.setNNF(nfe.getNumero() == null ? "1" : nfe.getNumero().toString());
			ide.setDhEmi(IDUtils.retornaDataNFCe(nfe.getEmissao()));
			ide.setTpNF("1");
			ide.setIdDest(IDUtils.getDestino(nfe));
			ide.setCMunFG(nfe.getEmpresa().getIbgeCidade());
			ide.setTpImp("4");
			ide.setTpEmis(IDUtils.getTipoEmissao(nfe));
			ide.setCDV(ChaveAcesso.getDVChave(infNFe.getId()));
			ide.setTpAmb(IDUtils.getAmbiente(nfe.getEmpresa()));
			ide.setFinNFe("1");
			ide.setIndFinal("1");
			ide.setIndPres("1");
			ide.setProcEmi("0");
			ide.setVerProc("002");
			infNFe.setIde(ide);

			// Emitente
			Emit emit = new Emit();
			emit.setCNPJ(AmsoftUtils.removerMascara(nfe.getUsuario().getEmpresa().getCnpj()));
			emit.setXNome(nfe.getUsuario().getEmpresa().getRazao_social());

			if (amsoftUtils.isHomologacao()) {
				emit.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
			}

			emit.setEnderEmit(EmitenteUtils.getEnderecoEmitente(nfe.getEmpresa()));
			emit.setIE(AmsoftUtils.removerMascara(nfe.getEmpresa().getInscricao_estadual()));
			emit.setCRT(EmitenteUtils.getCodigoRegimeTributario(nfe.getEmpresa()));
			infNFe.setEmit(emit);

			// Destinatario
			Dest dest = new Dest();

			if (AmsoftUtils.isNotCPF(nfe.getCliente())) {

			} else {
				// Destinatario

				if (AmsoftUtils.isPessoaFisica(nfe.getCliente())) {
					dest.setCPF(AmsoftUtils.removerMascara(nfe.getCliente().getDocReceitaFederal()));
				} else {
					dest.setCNPJ(AmsoftUtils.removerMascara(nfe.getCliente().getDocReceitaFederal()));
				}

				dest.setXNome(nfe.getCliente().getNome());

				if (amsoftUtils.isHomologacao()) {
					dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
				}

				dest.setEnderDest(DestinatarioUtils.getEnderecoDestinatario(nfe));
				dest.setIndIEDest(IDUtils.getIndicadorIEDestinatario(nfe));
				infNFe.setDest(dest);
			}

			infNFe.getDet().addAll(ProdutosUtils.getProdutos(nfe.getItensProdutos(), nfe));

			// TOTAIS
			Total total = new Total();
			total.setICMSTot(ICMSTotalUtils.popularICMSTotal(nfe));
			infNFe.setTotal(total);

			Transp transp = new Transp();
			transp.setModFrete("9");
			infNFe.setTransp(transp);

			InfAdic infAdic = new InfAdic();
			// infAdic.setInfCpl("XXX");
			infNFe.setInfAdic(infAdic);

			Pag pag = PagamentoUtils.getPagamento(nfe);
			//infNFe.setPag(pag);

			if (PagamentoUtils.isAPrazo(nfe)) {
				Cobr cob = PagamentoUtils.getCobranca(nfe);
				infNFe.setCobr(cob);
			}

			infNFe.setPag(pag);
			tnfe.setInfNFe(infNFe);

			// Monta EnviNfe
			TEnviNFe enviNFe = new TEnviNFe();
			enviNFe.setVersao("4.00");
			enviNFe.setIdLote("1");
			enviNFe.setIndSinc("1");
			enviNFe.getNFe().add(tnfe);

			// Monta e Assina o XML
			enviNFe = Nfe.montaNfe(enviNFe, true);

			String xmlEnvio = XmlUtil.objectToXml(enviNFe);

			nfe.setEnviNFe(AmsoftUtils.formataXML(xmlEnvio));

			AmsoftUtils.info(xmlEnvio);

			// QRCODE
			csc = this.getCsc(nfe);
			idToken = nfe.getUsuario().getEmpresa().getToken();

			String qrCode = NFCeUtil.getCodeQRCode(infNFe.getId().substring(3), "100", ide.getTpAmb(),
					dest.getCNPJ() == null ? dest.getCPF() : dest.getCNPJ(), ide.getDhEmi(),
					total.getICMSTot().getVNF(), total.getICMSTot().getVICMS(),
					Base64.getEncoder().encodeToString(
							enviNFe.getNFe().get(0).getSignature().getSignedInfo().getReference().getDigestValue()),
					idToken, csc, WebServiceUtil.getUrl(config ,ConstantesUtil.NFCE, ConstantesUtil.SERVICOS.URL_QRCODE));


			TNFe.InfNFeSupl infNFeSupl = new TNFe.InfNFeSupl();
			infNFeSupl.setQrCode(qrCode);
			infNFeSupl.setUrlChave(WebServiceUtil.getUrl(config ,ConstantesUtil.NFCE, ConstantesUtil.SERVICOS.URL_CONSULTANFCE));
			enviNFe.getNFe().get(0).setInfNFeSupl(infNFeSupl);

			nfe.setXml(AmsoftUtils.formataXML(XmlUtil.objectToXml(enviNFe)));

			// Envia a Nfe para a Sefaz
			TRetEnviNFe retorno = Nfe.enviarNfe(enviNFe, ConstantesUtil.NFCE);

			if (!retorno.getCStat().equals(StatusEnum.LOTE_PROCESSADO.getCodigo())) {
				throw new NfeException("Status: " + retorno.getCStat() + " - Motivo: " + retorno.getXMotivo());
			}

			if (!retorno.getProtNFe().getInfProt().getCStat().equals(StatusEnum.AUTORIZADO.getCodigo())) {
				throw new NfeException("Status: " + retorno.getProtNFe().getInfProt().getCStat() + " - Motivo: "
						+ retorno.getProtNFe().getInfProt().getXMotivo());
			}

			this.infoMensagensAutorizacao(retorno);

			AmsoftUtils
					.info("Xml Final :" + AmsoftUtils.formataXML(XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe())));
			
			
			nfe.setXml(XmlUtil.criaNfeProc(enviNFe, retorno.getProtNFe()));
			
			AmsoftUtils.info(AmsoftUtils.formataXML(nfe.getXml()));
			

			nfe.setModelo("65");
			nfe.setChave(infNFe.getId());
			nfe.setProtocolo(retorno.getProtNFe().getInfProt().getNProt());
			nfe.setEnviNFe(AmsoftUtils.formataXML(xmlEnvio));
			nfe.setStatus(StatusNFe.AUTORIZADA);
			
			this.setBookName(nfe.getId().toString());
			showRatingDialog();

		} catch (NfeException | CertificadoException | JAXBException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

		return nfe;
	}

	private void infoMensagensAutorizacao(TRetEnviNFe retorno) {
		FacesUtil.addInfoMessage("Evento: Envio NF-e");
		FacesUtil.addInfoMessage("Status: " + retorno.getProtNFe().getInfProt().getCStat());
		FacesUtil.addInfoMessage("Motivo: " + retorno.getProtNFe().getInfProt().getXMotivo());
		FacesUtil.addInfoMessage("Data: " + retorno.getProtNFe().getInfProt().getDhRecbto());
		FacesUtil.addInfoMessage("Protocolo: " + retorno.getProtNFe().getInfProt().getNProt());
	}

	private String getCsc(NFCe nfCe) {

		if (amsoftUtils.isHomologacao()) {
			return nfCe.getUsuario().getEmpresa().getCscHomologacao();
		} else {
			return nfCe.getUsuario().getEmpresa().getCsc();
		}

	}
	
	
	@SuppressWarnings("deprecation")
	public void showRatingDialog() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentWidth", 480);
		options.put("contentHeight", 180);
		options.put("includeViewParams", true);

		Map<String, List<String>> params = new HashMap<String, List<String>>();
		List<String> values = new ArrayList<String>();

		values.add(bookName);
		params.put("bookName", values);

		RequestContext.getCurrentInstance().openDialog("/nfce/bookRating", options, params);
	}

	public void onDialogReturn(SelectEvent event) {
		Object rating = event.getObject();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "You rated the book with " + rating, null);

		FacesContext.getCurrentInstance().addMessage(null, message);
	}


}
