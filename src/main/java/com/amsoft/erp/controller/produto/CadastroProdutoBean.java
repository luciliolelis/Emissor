package com.amsoft.erp.controller.produto;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.amsoft.erp.model.cep.CepEstado;
import com.amsoft.erp.model.enun.RegimeTributario;
import com.amsoft.erp.model.ncmcest.Cest;
import com.amsoft.erp.model.ncmcest.Ncm;
import com.amsoft.erp.model.produto.CSOSN;
import com.amsoft.erp.model.produto.CSTICMS;
import com.amsoft.erp.model.produto.CSTIPI;
import com.amsoft.erp.model.produto.CSTPISCOFINS;
import com.amsoft.erp.model.produto.ItemIcmsUf;
import com.amsoft.erp.model.produto.OrigemProduto;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.Estados;
import com.amsoft.erp.repository.Unidades;
import com.amsoft.erp.repository.produtos.CsOsons;
import com.amsoft.erp.repository.produtos.CstIcms;
import com.amsoft.erp.repository.produtos.CstIpis;
import com.amsoft.erp.repository.produtos.CstPisCofins;
import com.amsoft.erp.repository.produtos.Ncms;
import com.amsoft.erp.repository.produtos.OrigemProdutos;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.CadastroProdutoService;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrigemProdutos origens;

	@Inject
	private CstIcms cstIcms;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	@Inject
	private CsOsons csOsons;

	@Inject
	private CstPisCofins cstPisCofins;

	@Inject
	private CstIpis cstIpis;

	@Inject
	private Unidades unidades;

	@Inject
	private Estados estados;

	private CepEstado estado;

	private List<CepEstado> listaEstados;

	private CepEstado estadoLinhaEditavel;
	private List<Cest> listaCest;
	
	public List<Cest> getListaCest() {
		return listaCest;
	}

	public void setListaCest(List<Cest> listaCest) {
		this.listaCest = listaCest;
	}

	public CepEstado getEstadoLinhaEditavel() {
		return estadoLinhaEditavel;
	}

	public void setEstadoLinhaEditavel(CepEstado estadoLinhaEditavel) {
		this.estadoLinhaEditavel = estadoLinhaEditavel;
	}

	private ItemIcmsUf estadoSelecionado;

	public CepEstado getEstado() {
		return estado;
	}

	public void setEstado(CepEstado estado) {
		this.estado = estado;
	}

	public ItemIcmsUf getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(ItemIcmsUf estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public List<CepEstado> getListaEstados() {
		return listaEstados;
	}

	public void consultarEstados() {
		this.listaEstados = estados.todos();
	}

	@Inject
	private CadastroProdutoService cadastroProdutoService;

	@Produces
	@ProdutoEdicao
	private Produto produto;

	private List<Ncm> ncmFiltrados;

	@Inject
	private Ncms ncms;
	@SuppressWarnings("unused")
	private String codigoNcm;
	private String descricaoNcmPesquisa;


	private List<OrigemProduto> origemProdutos;
	private List<CSTICMS> icmss;
	private List<CSOSN> csosns;

	private List<CSTICMS> icmssConsumidorFinal;
	private List<CSOSN> csosnsConsumidorFinal;

	private List<CSTICMS> cstIcmsExterior;
	private List<CSOSN> csosnsExterior;

	private List<CSTPISCOFINS> piscofins;
	private List<CSTIPI> ipis;

	public List<CSTICMS> getCstIcmsExterior() {
		return cstIcmsExterior;
	}

	public List<CSOSN> getCsosnsExterior() {
		return csosnsExterior;
	}

	public List<CSTPISCOFINS> getPiscofins() {
		return piscofins;
	}

	public List<CSTIPI> getIpis() {
		return ipis;
	}

	public List<CSOSN> getCsosns() {
		return csosns;
	}

	public List<CSTICMS> getIcmss() {
		return icmss;
	}

	public List<CSTICMS> getIcmssConsumidorFinal() {
		return icmssConsumidorFinal;
	}

	public List<CSOSN> getCsosnsConsumidorFinal() {
		return csosnsConsumidorFinal;
	}

	public List<OrigemProduto> getOrigemProdutos() {
		return origemProdutos;
	}

	public CadastroProdutoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			origemProdutos = origens.todos();
			icmss = cstIcms.todos();
			csosns = csOsons.todos();

			icmssConsumidorFinal = cstIcms.todosConcumidorFinal();
			csosnsConsumidorFinal = csOsons.todosConcumidorFinal();

			cstIcmsExterior = cstIcms.todosExterior();
			csosnsExterior = csOsons.todosExterior();

			piscofins = cstPisCofins.todos();
			ipis = cstIpis.todos();

			if (this.produto.getId() == null) {
				if (this.isSimplesNacional()) {
					this.produto.setCstpiscofins(this.cstPisCofins.porCodigo("49"));
					this.produto.setCstipi(this.cstIpis.porCodigo("99"));
					this.produto.setCsosnExterior(this.csOsons.porCodigo("300"));
				} else {
					this.produto.setCsticmsExterior(this.cstIcms.porCodigo("41"));
				}
			}

			this.ncmPesquisar();
			this.consultarEstados();

			if (this.produto.getNcm() != null) {
				this.listaCest = ncms.porCest(produto.getNcm());
			}
			
			this.produto.adicionarItemVazio();

		}
	}

	public void removerItem(ItemIcmsUf item, int linha) {
		this.getProduto().getItensIcmsUf().remove(linha);
	}

	private boolean existeEstadoComIcms(CepEstado estado) {
		boolean existeItem = false;

		for (ItemIcmsUf item : this.getProduto().getItensIcmsUf()) {
			if (estado.equals(item.getUf())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public void carregarEstadoLinhaEditavel() {

		try {
			ItemIcmsUf item = this.produto.getItensIcmsUf().get(0);
			if (this.estadoLinhaEditavel != null) {
				if (this.existeEstadoComIcms(this.estadoLinhaEditavel)) {
					FacesUtil.addErrorMessage("Já existe a tributação ICMS para este estado");
				} else {
					item.setUf(this.estadoLinhaEditavel);
					this.produto.adicionarItemVazio();
					this.estadoLinhaEditavel = null;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getCause() + " - " + e.getMessage());
		}

	}

	public List<CepEstado> completarEstado(String nome) {
		return this.estados.porEstadoNomeObj(nome);
	}

	private void limpar() {
		produto = new Produto();
		descricaoNcmPesquisa = new String();
	}

	public void salvar() {

		this.produto.removerItemVazio();

		System.out.println(this.produto.getNome());

		this.produto.setNome(rtrim(this.produto.getNome()));

		System.out.println(this.produto.getNome());
		try {
			this.produto.setEmpresa(this.usuarioLogado.getUsuario().getEmpresa());

			this.produto = cadastroProdutoService.salvar(this.produto);
			FacesUtil.addInfoMessage("Produto salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		} finally {
			this.produto.adicionarItemVazio();
		}

		
		
	}

	public static String rtrim(String str) {
		return str.replaceAll("\\s+$", "");
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;

	}

	public boolean isEditando() {
		return this.produto.getId() != null;
	}

	public void onRowDblClckSelect(SelectEvent event) throws IOException {
		Produto obj = (Produto) event.getObject();
		this.setProduto(obj);
		FacesContext.getCurrentInstance().getExternalContext().redirect("CadastroProduto.xhtml?produto=" + obj.getId());
	}

	@NotBlank
	public String getNcm() {
		return produto.getNcm() == null ? null : produto.getNcm();
	}

	public void setNcm(String nome) {
	}

	
	@SuppressWarnings("deprecation")
	public void selecionarNcm(Ncm ncm) {
		RequestContext.getCurrentInstance().closeDialog(ncm);
	}

	public void setDescricaoNcm(String descrciao) {
	}

//	public void ncmSelecionado(SelectEvent event) {
//		this.produto.setNcm((Ncm) event.getObject());
//		this.produto.setCest(((Ncm) event.getObject()).getCest());
//	}

	public void ncmSelecionado(SelectEvent event) {
		produto.setNcm(((Ncm) event.getObject()).getNcm());

		this.listaCest = ncms.porCest(produto.getNcm());

		for (Cest item : listaCest) {

			System.out.println(item.getCest());
		}
	}
	
	public void prepararNovoCadastro() {
		System.out.println("Novo produto");
		this.produto.adicionarItemVazio();

	}

	public boolean isValidarCodigoBarra(String codigoBarras) {
		boolean apenasNumeros = true;

		for (char digito : codigoBarras.toCharArray()) {
			if (!Character.isDigit(digito)) {
				apenasNumeros = false;
				break;
			}
		}
		if (apenasNumeros) {
			if ((codigoBarras.length() == 8) || (codigoBarras.length() == 12) || (codigoBarras.length() == 13)
					|| (codigoBarras.length() == 14)) {
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	boolean isSimplesNacional() {
		return this.usuarioLogado.getUsuario().getEmpresa().getRegimeTributario() == RegimeTributario.SIMPLES;
	}

	public boolean isHabilitaAliquotaPisCofins() {

		List<String> icms = new ArrayList<String>();
		icms.add("01");
		icms.add("01");
		icms.add("02");
		icms.add("03");
		icms.add("50");
		icms.add("51");
		icms.add("52");
		icms.add("53");
		icms.add("54");
		icms.add("55");
		icms.add("56");
		icms.add("60");
		icms.add("61");
		icms.add("62");
		icms.add("63");
		icms.add("64");
		icms.add("65");
		icms.add("66");

		if (this.produto != null) {
			if (this.produto.getCstpiscofins() != null) {
				if (this.produto.getCstpiscofins().getCodigo() != null) {
					if (icms.contains(this.produto.getCstpiscofins().getCodigo())) {
						return true;
					}
				}
			}
		}

		this.produto.setAliquotaCofins(BigDecimal.ZERO);
		this.produto.setAliquotaPis(BigDecimal.ZERO);
		return false;

	}

	private boolean isProdutoItemValido(ItemIcmsUf item) {
		boolean ret = false;

		if (isSimplesNacional())
			ret = ((item != null) && (item.getCsosn() != null) && (item.getCsosn().getCodigo() != null));
		else
			ret = ((item != null) && (item.getCstIcms() != null) && (item.getCstIcms().getCodigo() != null));

		return ret;

	}

	private boolean isProdutoValido() {
		boolean ret = false;

		if (isSimplesNacional())
			ret = ((this.getProduto() != null) && (this.getProduto().getCsosn() != null)
					&& (this.getProduto().getCsosn().getCodigo() != null));
		else
			ret = ((this.getProduto() != null) && (this.getProduto().getCsticms() != null)
					&& (this.getProduto().getCsticms().getCodigo() != null));

		return ret;

	}

	public boolean habilitaAliquotaICMS(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("00");
				icms.add("10");
				icms.add("20");
				icms.add("30");
				icms.add("51");
				icms.add("70");
				icms.add("90");
				ret = icms.contains(item.getCstIcms().getCodigo());
			}
		}

		if (ret == false) {
			item.setAliquotaIcms(BigDecimal.ZERO);
		}

		return ret;
	}

	public boolean isHabilitaAliquotaICMS() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("00");
				icms.add("10");
				icms.add("20");
				icms.add("30");
				icms.add("51");
				icms.add("70");
				icms.add("90");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setAliquotaIcms(BigDecimal.ZERO);
		}

		return ret;
	}

	public boolean obrigaAliquotaICMS(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {

				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");

				ret = csosn.contains(item.getCsosn().getCodigo());

			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("20");
				icms.add("30");
				icms.add("51");
				icms.add("70");

				ret = icms.contains(item.getCstIcms().getCodigo());

			}
		}
		return ret;
	}

	public boolean isObrigaAliquotaICMS() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("20");
				icms.add("30");
				icms.add("51");
				icms.add("70");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		return ret;
	}

	public boolean habilitaAliquotaICMSST(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				icms.add("90");
				ret = icms.contains(item.getCstIcms().getCodigo());
			}
		}

		if (ret == false) {
			item.setAliquotaIcmsSt(BigDecimal.ZERO);
		}

		return ret;
	}

	public boolean isHabilitaAliquotaICMSST() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				icms.add("90");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setAliquotaIcmsSt(BigDecimal.ZERO);
		}

		return ret;
	}

	public boolean obrigaAliquotaICMSST(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {

				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				ret = icms.contains(item.getCstIcms().getCodigo());
			}

		}
		return ret;
	}

	public boolean isObrigaAliquotaICMSST() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}
		return ret;
	}

	public boolean habilitaMVA(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {

				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");

				ret = csosn.contains(item.getCsosn().getCodigo());

			} else {

				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				icms.add("90");

				ret = icms.contains(item.getCstIcms().getCodigo());

			}
		}

		if (ret == false) {
			item.setMva(BigDecimal.ZERO);
		}
		return ret;
	}

	public boolean isHabilitaMVA() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				icms.add("90");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setMva(BigDecimal.ZERO);
		}

		return ret;
	}

	public boolean obrigaMVA(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				ret = icms.contains(item.getCstIcms().getCodigo());
			}
		}

		return ret;
	}

	public boolean isObrigaMVA() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("10");
				icms.add("30");
				icms.add("70");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		return ret;
	}

	public boolean habilitaReducao(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("20");
				icms.add("51");
				icms.add("70");
				icms.add("90");
				icms.add("10");
				icms.add("30");
				ret = icms.contains(item.getCstIcms().getCodigo());
			}
		}

		if (ret == false) {
			item.setReducaoIcms(BigDecimal.ZERO);
		}

		return ret;

	}

	public boolean isHabilitaReducao() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("20");
				icms.add("51");
				icms.add("70");
				icms.add("90");
				icms.add("10");
				icms.add("30");
				ret = icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setReducaoBaseCalculoIcms(BigDecimal.ZERO);
		}

		return ret;

	}

	public boolean obrigaReducao(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {

			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("20");
				icms.add("51");
				icms.add("70");

				ret = icms.contains(item.getCstIcms().getCodigo());

				return ret;
			}
		}

		return ret;

	}

	public boolean isObrigaReducao() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {

			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("20");
				icms.add("51");
				icms.add("70");

				ret = icms.contains(this.produto.getCsticms().getCodigo());

				return ret;
			}
		}

		return ret;

	}

	public boolean habilitaReducaoSt(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");

				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("40");
				icms.add("41");
				icms.add("50");
				icms.add("60");
				icms.add("20");
				icms.add("00");
				icms.add("70");
				icms.add("51");

				ret = !icms.contains(item.getCstIcms().getCodigo());
			}
		}

		if (ret == false) {
			item.setReducaoIcmsSt(BigDecimal.ZERO);
		}

		return ret;

	}

	public boolean isHabilitaReducaoSt() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");

				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("40");
				icms.add("41");
				icms.add("50");
				icms.add("60");
				icms.add("20");
				icms.add("00");
				icms.add("70");
				icms.add("51");

				ret = !icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setReducaoBaseCalculoIcmsSt(BigDecimal.ZERO);
		}

		return ret;

	}

	public boolean habilitaValorPauta(ItemIcmsUf item, int linha) {

		boolean ret = false;

		if (this.isProdutoItemValido(item)) {
			if (this.isSimplesNacional()) {

				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(item.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("40");
				icms.add("41");
				icms.add("50");
				icms.add("60");
				icms.add("20");
				icms.add("00");
				icms.add("70");
				icms.add("51");

				ret = !icms.contains(item.getCstIcms().getCodigo());
			}
		}

		if (ret == false) {
			item.setValorPauta(BigDecimal.ZERO);
		}
		return ret;
	}

	public boolean isHabilitaValorPauta() {

		boolean ret = false;

		if (this.isProdutoValido()) {
			if (this.isSimplesNacional()) {
				List<String> csosn = new ArrayList<String>();
				csosn.add("201");
				csosn.add("202");
				csosn.add("203");
				csosn.add("900");
				ret = csosn.contains(this.produto.getCsosn().getCodigo());
			} else {
				List<String> icms = new ArrayList<String>();
				icms.add("40");
				icms.add("41");
				icms.add("50");
				icms.add("60");
				icms.add("20");
				icms.add("00");
				icms.add("70");
				icms.add("51");
				ret = !icms.contains(this.produto.getCsticms().getCodigo());
			}
		}

		if (ret == false) {
			this.produto.setPauta(BigDecimal.ZERO);
		}
		return ret;
	}

	@NotBlank
	public String getCodigoNcm() {
		return (this.produto.getNcm() == null) ? null : this.produto.getNcm().toString();
	}

	public void setCodigoNcm(String codigoNcm) {
		this.codigoNcm = codigoNcm;
	}

	public String getDescricaoNcmPesquisa() {
		return descricaoNcmPesquisa;
	}

	public void setDescricaoNcmPesquisa(String descricaoNcmPesquisa) {
		this.descricaoNcmPesquisa = descricaoNcmPesquisa;
	}

	public List<Ncm> getNcmFiltrados() {
		return ncmFiltrados;
	}

	public void setNcmFiltrados(List<Ncm> ncmFiltrados) {
		this.ncmFiltrados = ncmFiltrados;
	}

	public void ncmPesquisar() {
		ncmFiltrados = ncms.pesquisa();
	}



	public void novaPesquisa() {
		ncmPesquisar();
	}

	
	public List<String> completarUnidade(String descricao) {
		return this.unidades.porNome(descricao);
	}

	private boolean isOrigemProduto() {
		return this.produto != null && this.produto.getOrigemProduto() != null;
	}

	private boolean isNotOrigemProduto() {
		return !isOrigemProduto();
	}

	public void geraCodioBarras() {

		String pais = "1058";

		String empresa = usuarioLogado.getUsuario().getEmpresa().getId().toString();

		Integer x = 5 - empresa.length();
		String add = "";

		for (int i = 0; i < x; i++) {
			add = add + "0";
		}

		empresa = add + empresa;

		String prod = this.produto.getId().toString();

		Integer y = 4 - prod.length();

		add = "";

		for (int i = 0; i < y; i++) {
			add = add + "0";
		}
		prod = add + prod;

		String ean = pais + empresa + prod;

		Boolean valida = false;
		Integer contator = 0;

		while (valida == false) {

			if (contator > 0) {
				ean = ean.substring(0, ean.length() - 1);
			}

			ean = ean + contator.toString();
			contator++;
			CodigoBarraEAN codigoBarra = new CodigoBarraEAN(ean);
			valida = codigoBarra.validar(codigoBarra);
			System.out.println("Codigo de barra: " + (valida ? "Válido" : "Invalido"));
			System.out.println("Numero do codigo de barras: " + codigoBarra.getCodigoBarra());

			if (contator == 10) {
				valida = true;
			}
		}

		// System.out.println("Codigo de barra: " +
		// codigoBarra.validar(codigoBarra));

	}

	public String getAliquotas() {

		try {

			if (this.isNotOrigemProduto()) {
				return "";
			}

			String codigo = this.produto.getOrigemProduto().getCodigo();
			String ret = "";

			switch (codigo) {
			case "0":
				ret = "7% ou 12%";
				break;
			case "1":
				ret = "4%";
				break;
			case "2":
				ret = "4%";
				break;
			case "3":
				ret = "4%";
				break;
			case "4":
				ret = "7% ou 12%";
				break;
			case "5":
				ret = "7% ou 12%";
				break;
			case "6":
				ret = "7% ou 12%";
				break;
			case "7":
				ret = "7% ou 12%";
				break;
			case "8":
				ret = "4%";
				break;
			default:
				break;
			}

			return ret;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}