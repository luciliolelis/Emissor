package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.Marca;

public class Marcas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Marca porId(Long id) {
		return manager.find(Marca.class, id);
	}

	public List<Marca> todos() {
		return manager.createQuery("from Marca", Marca.class)
				.getResultList();
	}

	public List<String> porNome(String descricao) {
		
		List<String> ret = new ArrayList<String>();
		
		List<Marca> list = this.manager
				.createQuery(
						"from Marca " + "where upper(descricao) like :descricao",
						Marca.class)
				.setParameter("descricao", descricao.toUpperCase() + "%")
				.getResultList();
	
		for (Marca item : list) {
			ret.add(item.getDescricao());
		}

		return ret;
	}
	
	
}
