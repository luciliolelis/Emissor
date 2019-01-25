package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.cep.CepEstado;

public class Estados implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CepEstado porId(String id) {
		return manager.find(CepEstado.class, id);
	}

	public List<CepEstado> todos() {
		return manager.createQuery("from CepEstado", CepEstado.class).getResultList();
	}

	public List<String> porEstadoNome(String estado) {

		List<String> ret = new ArrayList<String>();

		List<CepEstado> list = this.manager
				.createQuery("from CepEstado where upper(estado) like :estado or upper(uf) like :uf", CepEstado.class)
				.setParameter("estado", estado.toUpperCase() + "%").setParameter("uf", estado.toUpperCase() + "%")
				.getResultList();

		for (CepEstado item : list) {
			ret.add(item.getUf());
		}

		return ret;
	}

	public CepEstado porUF(String uf) {
		CepEstado estado = null;

		estado = this.manager.createQuery("from CepEstado where lower(uf) = :uf", CepEstado.class)
				.setParameter("uf", uf.toLowerCase()).getSingleResult();

		return estado;
	}

	public String ibgePorUF(String uf) {
		CepEstado estado = null;

		estado = this.manager.createQuery("from CepEstado where lower(uf) = :uf", CepEstado.class)
				.setParameter("uf", uf.toLowerCase()).getSingleResult();

		return estado.getCod_ibge();
	}

	public List<CepEstado> porEstadoNomeObj(String estado) {

		List<CepEstado> list = this.manager
				.createQuery("from CepEstado where upper(estado) like :estado or upper(uf) like :uf", CepEstado.class)
				.setParameter("estado", estado.toUpperCase() + "%").setParameter("uf", estado.toUpperCase() + "%")
				.getResultList();

		return list;

	}

	public List<String> todosEstados() {
		List<CepEstado> lista = new ArrayList<>();

		lista = manager.createQuery("from CepEstado", CepEstado.class).getResultList();

		List<String> ret = new ArrayList<>();

		for (CepEstado item : lista) {
			ret.add(item.getUf());
		}

		return ret;
	}
}