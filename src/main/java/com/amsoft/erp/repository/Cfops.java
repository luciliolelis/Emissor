package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.nfe.Cfop;
import com.amsoft.erp.model.nfe.FinalidadeOperacao;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.TipoDocumento;
import com.amsoft.erp.service.NegocioException;

public class Cfops implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Cfop porId(Long id) {
		return manager.find(Cfop.class, id);
	}

	public List<Cfop> porDescricao(String descricao) {

		String codigo = descricao;

		return this.manager
				.createQuery("from Cfop where upper(descricao) like :descricao or codigo like :codigo", Cfop.class)
				.setParameter("descricao", "%" + descricao.toUpperCase() + "%")
				.setParameter("codigo", "%" + codigo.toUpperCase() + "%").getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cfop> porTipo(String descricao, Nfe nfe) {

		System.out.println("Inicia pesquisa de CFOP");

		Session session = manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Cfop.class);

		if (this.isNotaEntrada(nfe)) {

			if (this.isClienteExportacao(nfe)) {

			} else if (this.isClienteEstrangeiro(nfe)) {

			} else if (this.isOperacaoInterna(nfe)) {
				criteria.add(Restrictions.le("codigo", 1999));
			} else if (this.isOperacaoInterEstadual(nfe)) {
				criteria.add(Restrictions.ge("codigo", 1999));
				criteria.add(Restrictions.le("codigo", 2999));
			}

		} else if (this.isNotaSaida(nfe)) {

			if (this.isClienteExportacao(nfe)) {
				criteria.add(Restrictions.ge("codigo", 6999));
				criteria.add(Restrictions.le("codigo", 7999));
			} else if (this.isClienteEstrangeiro(nfe) || this.isOperacaoInterna(nfe)) {
				criteria.add(Restrictions.ge("codigo", 4999));
				criteria.add(Restrictions.le("codigo", 5999));
			} else if (this.isOperacaoInterEstadual(nfe)) {
				criteria.add(Restrictions.ge("codigo", 5999));
				criteria.add(Restrictions.le("codigo", 6999));
			}
		}

//		if (this.isDevolucao(nfe)) {
//			descricao = "devolução";
//		}

		Integer codigo = null;

		try {
			codigo = new Integer(descricao);
			criteria.add(Restrictions.eq("codigo", codigo));
		} catch (Exception e) {
			if (descricao != "") {
				criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));
			}
		}

		System.out.println("Retorna lista de CFOP");
		return criteria.addOrder(Order.asc("codigo")).list();
	}

	public List<Cfop> todosConcumidorFinal() {
		return manager.createQuery(
				"from Cfop where codigo = '5101' or  codigo = '5102' or codigo = '5103' or codigo = '5104' or codigo = '5115' or codigo = '5405' or codigo = '5656' or codigo = '5667' or codigo = '5933'",
				Cfop.class).getResultList();

	}

	private boolean isOperacaoInterEstadual(Nfe nfe) {
		return this.isCliente(nfe) && !nfe.getEmpresa().getUf().equals(nfe.getEnderecoEntrega().getUf());
	}

	@SuppressWarnings("unused")
	private boolean isDevolucao(Nfe nfe) {
		return nfe.getFinalidadeOperacao() == FinalidadeOperacao.DEVOLUCAO;
	}

	private boolean isOperacaoInterna(Nfe nfe) {
		return this.isCliente(nfe) && nfe.getEmpresa().getUf().equals(nfe.getEnderecoEntrega().getUf());
	}

	private boolean isClienteEstrangeiro(Nfe nfe) {
		return this.isCliente(nfe) && nfe.getCliente().getEstrangeiro().equals(true);
	}

	private boolean isClienteExportacao(Nfe nfe) {
		return this.isCliente(nfe) && nfe.getCliente().getExterior().equals(true);
	}

	boolean isCliente(Nfe nfe) {
		return nfe.getCliente() != null;
	}

	private boolean isNotaEntrada(Nfe nfe) {
		return nfe.getTipoDocumento().equals(TipoDocumento.ENTRADA);
	}

	private boolean isNotaSaida(Nfe nfe) {
		return nfe.getTipoDocumento().equals(TipoDocumento.SAIDA);
	}

	public List<Cfop> todos() {
		return manager.createQuery("from Cfop", Cfop.class).getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cfop> filtrados(String descricao) {
		Session session = manager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Cfop.class);

		if (StringUtils.isNotBlank(descricao)) {
			criteria.add(Restrictions.ilike("codigo", descricao));
		}

		return criteria.addOrder(Order.asc("id")).list();
	}

	public Cfop porCodigo(Integer codigo) {
		Cfop ret = null;

		try {

			ret = this.manager.createQuery("from Cfop where codigo = :codigo", Cfop.class)
					.setParameter("codigo", codigo).getSingleResult();

		} catch (NoResultException e) {
			System.out.println(e.getMessage() + " - " + e.getCause());
			throw new NegocioException("nenhum cfop encontrado com o código informado");
		}

		return ret;
	}

}
