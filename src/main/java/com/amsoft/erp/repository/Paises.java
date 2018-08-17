package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Pais;

public class Paises implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pais porId(Long id) {
		return manager.find(Pais.class, id);
	}

	public List<Pais> todos() {
		return manager.createQuery("from Pais", Pais.class)
				.getResultList();
	}

	public List<Pais> porNome(String nome) {
		
		List<Pais> list = this.manager
				.createQuery(
						"from Pais " + "where upper(nome) like :nome",
						Pais.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	
		return list;
	}
	
	
}
