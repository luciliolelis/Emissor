package com.amsoft.erp.repository;

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

import com.amsoft.erp.model.cep.CepCidade;

public class Cidades implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CepCidade porId(Long id) {
		return manager.find(CepCidade.class, id);
	}

	public CepCidade porCidade(String valor) {
		CepCidade cidade = null;

		cidade = this.manager.createQuery("from CepCidade where lower(cidade) = :cidade", CepCidade.class)
				.setParameter("cidade", valor.toLowerCase()).getSingleResult();

		return cidade;
	}

	public List<CepCidade> todos() {
		return manager.createQuery("from CepCidade", CepCidade.class).getResultList();
	}

	public List<String> porCidadeNome(String cidade) {

		List<String> ret = new ArrayList<String>();

		List<CepCidade> list = this.manager
				.createQuery("from CepCidade " + "where upper(cidade) like :cidade", CepCidade.class)
				.setParameter("cidade", cidade.toUpperCase() + "%").getResultList();

		for (CepCidade item : list) {
			ret.add(item.getCidade());
		}

		return ret;
	}

	@SuppressWarnings("deprecation")
	public String ibgePorCidade(String nome, String uf) {

		try {
			Session session = manager.unwrap(Session.class);
			Criteria criteria = session.createCriteria(CepCidade.class);
			
			
			nome = nome.replace("SAO", "SÃO");
			nome = nome.replace("CAO", "ÇÃO");

			if (StringUtils.isNotBlank(nome)) {
				criteria.add(Restrictions.ilike("cidade", nome.toUpperCase(), MatchMode.START));
				criteria.add(Restrictions.ilike("uf.uf", uf.toUpperCase(), MatchMode.ANYWHERE));
			}
			
			CepCidade cidade = ((CepCidade) criteria.uniqueResult());

			return cidade.getCod_ibge();
		} catch (Exception e) {
			System.out.println("ibgePorCidade " + e.toString());
		}

		return "";
	}

	public List<CepCidade> porCidadeNomeObj(String cidade) {
		return this.manager.createQuery("from CepCidade where upper(cidade) like :cidade", CepCidade.class)
				.setParameter("cidade", cidade.toUpperCase() + "%").getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<String> cidadesByEstado(String estado) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(CepCidade.class);

		List<String> ret = new ArrayList<String>();

		List<CepCidade> lista = new ArrayList<>();

		if (estado != null)
			criteria.add(Restrictions.ilike("uf.uf", estado, MatchMode.ANYWHERE));

		lista = criteria.addOrder(Order.asc("uf")).list();

		for (CepCidade item : lista) {
			ret.add(item.getCidade());
		}

		return ret;

	}
	
	
	

}
