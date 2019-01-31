package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Grupo;

public class Grupos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Inject @Any
	private Event<Grupo> event;

	public Grupo porId(Long id) {
		return manager.find(Grupo.class, id);
	}

	public List<Grupo> todos() {
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}

	public Grupo guardar(Grupo grupo) {
		Grupo grup = manager.merge(grupo);
		this.event.fire(grup);
		return grup;
	}

	public void remover(Grupo grupo) {
		grupo = porId(grupo.getId());
		manager.remove(grupo);
	}
}