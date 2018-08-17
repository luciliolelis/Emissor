package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Ibpt;

public class Ibpts implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Ibpt porId(Long id) {
		return manager.find(Ibpt.class, id);
	}

	public List<Ibpt> todos() {
		return manager.createQuery("from Ibpt", Ibpt.class)
				.getResultList();
	}

	public Ibpt porNcm(String codigo) {
		
		List<Ibpt> list = this.manager
				.createQuery(
						"from Ibpt " + "where codigo like :codigo",
						Ibpt.class)
				.setParameter("codigo", codigo)
				.getResultList();
	
		if (list.size() > 0) {
			return list.get(0);
		}

		return null;
	}
}
