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

import com.amsoft.erp.model.produto.CSTICMS;

public class CstIcms implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CSTICMS porId(Long id) {
		return manager.find(CSTICMS.class, id);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<CSTICMS> porDescricao(String descricao) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CSTICMS.class);

		if (StringUtils.isNotBlank(descricao))
			criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));

		return criteria.addOrder(Order.asc("codigo")).list();

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<CSTICMS> todos() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CSTICMS.class);
		return criteria.addOrder(Order.asc("codigo")).list();
	}

	public List<CSTICMS> todosConcumidorFinal() {
		return manager.createQuery(
				"from CSTICMS where codigo = '00' or  codigo = '20' or  codigo = '40' or  codigo = '41' or  codigo = '60'",
				CSTICMS.class).getResultList();
	}

	public List<CSTICMS> todosExterior() {
		return manager.createQuery("from CSTICMS where codigo = '40' or  codigo = '41'", CSTICMS.class).getResultList();
	}
	public CSTICMS porCodigo(String codigo) {
		return this.manager.createQuery("from CSTICMS where codigo = :codigo", CSTICMS.class)
				.setParameter("codigo", codigo).getSingleResult();
	}
}
