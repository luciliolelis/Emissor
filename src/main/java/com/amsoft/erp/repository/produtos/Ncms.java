package com.amsoft.erp.repository.produtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.ncmcest.Cest;
import com.amsoft.erp.model.ncmcest.Ncm;

public class Ncms implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Ncm porId(String ncm) {
		return manager.find(Ncm.class, ncm);
	}

	

	public Ncm porCodigo(String ncm) {

		List<Ncm> list = this.manager.createQuery("from ncm where ncm = :ncm", Ncm.class)
				.setParameter("ncm", ncm).getResultList();

		if (list.size() > 0) {
			return list.get(0);
		}

		return null;
	}
	
	

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cest> porCest(String ncm) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cest.class);

		if (StringUtils.isNotBlank(ncm))
			criteria.add(Restrictions.ilike("ncm", ncm, MatchMode.ANYWHERE));


		return criteria.addOrder(Order.asc("ncm")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Ncm> porDescricao(String descricao) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Ncm.class);

		if (StringUtils.isNotBlank(descricao))
			criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));
		return criteria.addOrder(Order.asc("ncm")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Ncm> todos() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Ncm.class);
		return criteria.addOrder(Order.asc("ncm")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Ncm> filtrados(String descricao) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Ncm.class);

		if (StringUtils.isNotBlank(descricao)) {
			criteria.add(Restrictions.ilike("ncm", descricao));
		}

		return criteria.addOrder(Order.asc("id")).list();
	}



	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Ncm> pesquisa() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Ncm.class);

		List<Ncm> list = criteria.addOrder(Order.asc("ncm")).list();

		List<Ncm> ret = new ArrayList<>();

		for (Ncm ncm : list) {
			if (ncm.getNcm() != null && (ncm.getNcm().length() == 8)) {
				ret.add(ncm);
			}
		}

		return ret;
	}
}
