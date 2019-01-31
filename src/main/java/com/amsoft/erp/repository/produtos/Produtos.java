package com.amsoft.erp.repository.produtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.StatusProduto;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.produto.Produto;
import com.amsoft.erp.repository.filter.ProdutoFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;
import com.amsoft.erp.service.NegocioException;
import com.amsoft.erp.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	@Inject
	private EntityManager manager;
	
	@Inject @Any
	private Event<Produto> event;

	public Produto guardar(Produto produto) {
		Produto prod = manager.merge(produto);
		this.event.fire(prod);		
		return prod;
	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			manager.remove(produto);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	public Produto porSku(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Produto porCodigo(String sku, Empresa empresa) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku and empresa =:empresa", Produto.class)
					.setParameter("sku", sku.toUpperCase()).setParameter("empresa", empresa).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome, Empresa empresa) {

		return this.manager
				.createQuery("from Produto where upper(nome) like :nome and empresa = :empresa", Produto.class)
				.setParameter("nome", "%" + nome.toUpperCase() + "%").setParameter("empresa", empresa).getResultList();
	}

	public List<Produto> pesquisa(ProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		List<Predicate> predicates = new ArrayList<>();

		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);

		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(produtoRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		predicates.add(builder.equal(produtoRoot.get("status"), StatusProduto.CADASTRO));
		predicates.add(builder.equal(produtoRoot.get("empresa"), filtro.getEmpresa()));

		criteriaQuery.select(produtoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome")));

		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Produto> pesquisaPorEmpresa(ProdutoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
		criteria.add(Restrictions.eq("status", StatusProduto.CADASTRO));

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@SuppressWarnings("deprecation")
	public String getNumeroProdutos(Empresa empresa) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);
		criteria.add(Restrictions.eq("status", StatusProduto.CADASTRO));
		criteria.add(Restrictions.eq("empresa", empresa));
		criteria.setProjection(Projections.countDistinct("id"));

		return criteria.setProjection(Projections.rowCount()).uniqueResult().toString();
	}

	public int quantidadeFiltrados(ProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);

		Root<Produto> ProdutoRoot = criteriaQuery.from(Produto.class);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, ProdutoRoot);

		criteriaQuery.select(builder.count(ProdutoRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Long> query = manager.createQuery(criteriaQuery);

		return query.getSingleResult().intValue();
	}

	private List<Predicate> criarPredicatesParaFiltro(ProdutoFilter filtro, Root<Produto> produtoRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(
					builder.like(builder.lower(produtoRoot.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
		}

		predicates.add(builder.equal(produtoRoot.get("status"), StatusProduto.CADASTRO));
		predicates.add(produtoRoot.get("empresa").in(Arrays.asList(usuarioLogado.getUsuario().getEmpresa())));
		return predicates;
	}

	public List<Produto> filtrados(ProdutoFilter filtro) {
		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);

		Root<Produto> ProdutoRoot = criteriaQuery.from(Produto.class);

		filtro.setPropriedadeOrdenacao("nome");
		filtro.setAscendente(true);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, ProdutoRoot);

		criteriaQuery.select(ProdutoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = ProdutoRoot;

			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao
						.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}

			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}

		TypedQuery<Produto> query = manager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}

	

	
	
}
