package com.amsoft.erp.controller.home;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.repository.LogAcessos;
import com.amsoft.erp.repository.NFCes;
import com.amsoft.erp.repository.Nfes;
import com.amsoft.erp.repository.filter.NfeFilter;
import com.amsoft.erp.util.date.AmsoftUtilsDate;
import com.amsoft.erp.util.jsf.FacesUtil;

/**
 * @author Denis Gibikoski Beans Gestao Log´s de Acesso
 *
 */
@Named
@SessionScoped
public class GestaoHomeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Nfes nfes;

	@Inject
	private NFCes nfces;

	@Inject
	private LogAcessos logAcessos;

	private NfeFilter filtro = new NfeFilter();
	private BigDecimal totalNotasNfeFiltadas;
	private BigDecimal totalNotasNfceFiltadas;

	private List<LogAcesso> acessosList = new ArrayList<>();
	private List<Nfe> nfeList = new ArrayList<>();
	private List<NFCe> nfceList = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

		}
	}

	@PostConstruct
	private void onInit() {
		Date ini = AmsoftUtilsDate.getZeroHora(new Date());
		Date fin = AmsoftUtilsDate.getUltimaHora(new Date());
		this.filtro.setDataCriacaoDe(ini);
		this.filtro.setDataCriacaoAte(fin);
	}

	public void consultar() {

		Date ini = AmsoftUtilsDate.getZeroHora(this.filtro.getDataCriacaoDe());
		Date fin = AmsoftUtilsDate.getUltimaHora(this.filtro.getDataCriacaoAte());

		filtro.setDataCriacaoDe(ini);
		filtro.setDataCriacaoAte(fin);

		this.acessosList = this.logAcessos.filtrados(filtro);

		this.nfeList = this.nfes.emitidas(filtro);
		this.nfceList = this.nfces.emitidas(filtro);

		this.somarNotasFiltradas();
	}

	/**
	 * Soma os valores da NF-e utilizando Stream do JAVA8
	 */
	public void somarNotasFiltradas() {

		this.setTotalNotasNfeFiltadas(
				this.nfeList.stream().map(Nfe::getValorTotalNota).reduce(BigDecimal.ZERO, BigDecimal::add));
		this.setTotalNotasNfceFiltadas(
				this.nfceList.stream().map(NFCe::getValorTotalNota).reduce(BigDecimal.ZERO, BigDecimal::add));
	}

	public NfeFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(NfeFilter filtro) {
		this.filtro = filtro;
	}

	public List<LogAcesso> getacessosList() {
		return acessosList;
	}

	public List<NFCe> getNfceList() {
		return nfceList;
	}

	public List<Nfe> getNfeList() {
		return nfeList;
	}

	public List<LogAcesso> getAcessosList() {
		return acessosList;
	}

	public BigDecimal getTotalNotasNfeFiltadas() {
		return totalNotasNfeFiltadas;
	}

	public void setTotalNotasNfeFiltadas(BigDecimal totalNotasNfeFiltadas) {
		this.totalNotasNfeFiltadas = totalNotasNfeFiltadas;
	}

	public BigDecimal getTotalNotasNfceFiltadas() {
		return totalNotasNfceFiltadas;
	}

	public void setTotalNotasNfceFiltadas(BigDecimal totalNotasNfceFiltadas) {
		this.totalNotasNfceFiltadas = totalNotasNfceFiltadas;
	}

}