package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Grupo;

public class Grupos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Grupo porId(Long id) {
		return manager.find(Grupo.class, id);
	}

	public List<Grupo> todos() {
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}

	public Grupo guardar(Grupo grupo) {
		return manager.merge(grupo);
	}

	public void remover(Grupo grupo) {
		grupo = porId(grupo.getId());
		manager.remove(grupo);
	}
}