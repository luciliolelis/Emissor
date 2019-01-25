package com.amsoft.erp.repository.produtos;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.produto.CSOSN;

public class CsOsons implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CSOSN porId(Long id) {
		return manager.find(CSOSN.class, id);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<CSOSN> porDescricao(String descricao) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CSOSN.class);

		if (StringUtils.isNotBlank(descricao))
			criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));

		return criteria.addOrder(Order.asc("codigo")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<CSOSN> todos() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CSOSN.class);
		return criteria.addOrder(Order.asc("codigo")).list();
	}

	public List<CSOSN> todosConcumidorFinal() {
		return manager.createQuery(
				"from CSOSN where codigo = '102' or  codigo = '103' or  codigo = '300' or  codigo = '400' or  codigo = '500'",
				CSOSN.class).getResultList();
	}

	public List<CSOSN> todosExterior() {
		return manager.createQuery("from CSOSN where codigo = '300' or  codigo = '400'", CSOSN.class).getResultList();
	}
	
	public CSOSN porCodigo(String codigo) {
		return this.manager.createQuery("from CSOSN where codigo = :codigo", CSOSN.class)
				.setParameter("codigo", codigo).getSingleResult();
	}
}
