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

import com.amsoft.erp.model.produto.OrigemProduto;

public class OrigemProdutos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public OrigemProduto porId(Long id) {
		return manager.find(OrigemProduto.class, id);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<OrigemProduto> porDescricao(String descricao) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(OrigemProduto.class);

		if (StringUtils.isNotBlank(descricao))
			criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));

		return criteria.addOrder(Order.asc("codigo")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<OrigemProduto> todos() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(OrigemProduto.class);
		return criteria.addOrder(Order.asc("codigo")).list();
	}

}
