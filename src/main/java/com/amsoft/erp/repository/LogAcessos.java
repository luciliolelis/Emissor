package com.amsoft.erp.repository;

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

import com.amsoft.erp.model.LogAcesso;

public class LogAcessos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public LogAcesso guardar(LogAcesso log) {
		return manager.merge(log);
	}


	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<LogAcesso> filtrados(String email) {
		Session session = manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(LogAcesso.class);

		if (StringUtils.isNotBlank(email)) {
			criteria.add(Restrictions.ilike("email", email, MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.desc("data_hora")).list();
	}

}