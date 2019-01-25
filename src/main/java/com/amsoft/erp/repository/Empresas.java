package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.StatusEmpresa;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.emitente.FundoCombatePobreza;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	public Empresa porId(Integer id) {
		return manager.find(Empresa.class, id);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Empresa> todas() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Empresa.class);
		criteria.add(Restrictions.eq("status", StatusEmpresa.CADASTRO));
		return criteria.addOrder(Order.asc("razao_social")).list();
	}

	public Empresa guardar(Empresa empresa) {
		return manager.merge(empresa);
	}

	public void remover(Empresa empresa) {
		empresa = porId(empresa.getId());
		manager.remove(empresa);
	}

	public List<FundoCombatePobreza> perquisaFcp(Empresa empresa) {

		return this.manager
				.createQuery("from FundoCombatePobreza where empresa_id = :empresa_id", FundoCombatePobreza.class)
				.setParameter("empresa_id", empresa.getId()).getResultList();

	}

}