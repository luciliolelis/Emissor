package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.amsoft.erp.model.UnidadeMedida;

public class Unidades implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public UnidadeMedida porId(Long id) {
		return manager.find(UnidadeMedida.class, id);
	}

	public List<UnidadeMedida> todos() {
		return manager.createQuery("from UnidadeMedida", UnidadeMedida.class)
				.getResultList();
	}

	public List<String> porNome(String descricao) {
		
		List<String> ret = new ArrayList<String>();
		
		List<UnidadeMedida> list = this.manager
				.createQuery(
						"from UnidadeMedida " + "where upper(descricao) like :descricao or upper(grandeza) like :grandeza or upper(sigla) like :sigla",
						UnidadeMedida.class)
				.setParameter("descricao", descricao.toUpperCase() + "%")
				.setParameter("grandeza", descricao.toUpperCase() + "%")
				.setParameter("sigla", descricao.toUpperCase() + "%")
				.getResultList();
	
		for (UnidadeMedida item : list) {
			ret.add(item.getSigla());
		}

		return ret;
	}
	
	
}
