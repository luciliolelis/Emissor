package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Veiculo;

public class Veiculos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Veiculo porId(Long id) {
		return manager.find(Veiculo.class, id);
	}

	public List<Veiculo> todos() {
		return manager.createQuery("from Veiculo", Veiculo.class)
				.getResultList();
	}

	public Veiculo guardar(Veiculo veiculo) {
		return manager.merge(veiculo);
	}

	public void remover(Veiculo veiculo) {
		veiculo = porId(veiculo.getId());
		manager.remove(veiculo);
	}
}