package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.StatusCliente;
import com.amsoft.erp.model.enun.TipoCliente;
import com.amsoft.erp.repository.filter.ClienteFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}

	public List<Cliente> todas() {
		return manager.createQuery("from Cliente", Cliente.class).getResultList();
	}

	public Cliente guardar(Cliente cliente) {
		return manager.merge(cliente);
	}

	public void remover(Cliente cliente) {
		cliente = porId(cliente.getId());
		manager.remove(cliente);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cliente> porNome(String nome) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		if (StringUtils.isNotBlank(nome))
			criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.CLIENTE));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cliente> transportadorPorNome(String nome) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		if (StringUtils.isNotBlank(nome))
			criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.TRANSPORTADOR));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public List<Cliente> filtrados() {
		Session session = manager.unwrap(Session.class);
		return session.createQuery("from Cliente order by nome", Cliente.class).getResultList();
	}

	@SuppressWarnings("deprecation")
	public Cliente clientePorDoc(String doc) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.CLIENTE));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));
		criteria.add(Restrictions.eq("docReceitaFederal", doc));

		return (Cliente) criteria.uniqueResult();
	}

	@SuppressWarnings("deprecation")
	public Cliente porCNPJ(String cnpj) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		if (StringUtils.isNotBlank(cnpj))
			criteria.add(Restrictions.eq("docReceitaFederal", cnpj));

		return (Cliente) criteria.uniqueResult();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cliente> pesquisaCliente(ClienteFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.CLIENTE));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));

		if (StringUtils.isNotBlank(filtro.getNome())) {
			Criterion rest1 = Restrictions.or(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			Criterion rest2 = Restrictions
					.or(Restrictions.ilike("docReceitaFederal", filtro.getNome(), MatchMode.START));
			criteria.add(Restrictions.or(rest1, rest2));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Cliente> pesquisaTransportador(ClienteFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.TRANSPORTADOR));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));

		if (StringUtils.isNotBlank(filtro.getNome())) {
			Criterion rest1 = Restrictions.or(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			Criterion rest2 = Restrictions
					.or(Restrictions.ilike("docReceitaFederal", filtro.getNome(), MatchMode.START));
			criteria.add(Restrictions.or(rest1, rest2));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@SuppressWarnings("deprecation")
	public String getNumeroClientes() {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.CLIENTE));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));
		criteria.setProjection(Projections.countDistinct("id"));

		return criteria.setProjection(Projections.rowCount()).uniqueResult().toString();
	}

	@SuppressWarnings("deprecation")
	public String getNumeroTransportadores() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		criteria.add(Restrictions.eq("tipoCliente", TipoCliente.TRANSPORTADOR));
		criteria.add(Restrictions.eq("status", StatusCliente.CADASTRO));
		criteria.createAlias("empresas", "empresas");
		criteria.add(Restrictions.eq("empresas.id", this.usuarioLogado.getUsuario().getEmpresa().getId()));
		criteria.setProjection(Projections.countDistinct("id"));

		return (String) criteria.setProjection(Projections.rowCount()).uniqueResult().toString();

	}

}