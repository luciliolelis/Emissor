package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.EmpresaGrupo;
import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.repository.filter.UsuarioFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	public Usuario porId(Long id) {
		return this.manager.find(Usuario.class, id);
	}

	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}

	public LogAcesso guardarLogAcesso(LogAcesso log) {
		return manager.merge(log);
	}

	@Transactional
	public void remover(Usuario usuario) {
		try {
			usuario = porId(usuario.getId());
			manager.remove(usuario);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Usuário não pode ser excluído.");
		}
	}

	public List<Usuario> vendedores() {
		return this.manager.createQuery("from Usuario", Usuario.class).getResultList();
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;

		try {
			usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {

			System.out.println(e.getMessage() + " - " + e.getCause());
			throw new NegocioException("nenhum usuário encontrado com o e-mail informado");
		}

		return usuario;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Usuario> filtrados(UsuarioFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public List<Usuario> todos() {
		return manager.createQuery("from Usuario", Usuario.class).getResultList();
	}

	
	@SuppressWarnings("deprecation")
	public String getNumeroUsuarios(Empresa empresa) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(EmpresaGrupo.class);

		criteria.add(Restrictions.eq("empresa", empresa));
		criteria.setProjection(Projections.countDistinct("id"));

		return criteria.setProjection(Projections.rowCount()).uniqueResult().toString();
	}


}