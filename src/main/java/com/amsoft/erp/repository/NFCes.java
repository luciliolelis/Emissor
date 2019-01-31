package com.amsoft.erp.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.StatusNotas;
import com.amsoft.erp.model.emitente.Empresa;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.repository.filter.NfceFilter;
import com.amsoft.erp.repository.filter.NfeFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class NFCes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject @Any
	private Event<NFCe> event;
	
	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	@Inject
	private EntityManager manager;

	public NFCe porId(Long id) {
		return manager.find(NFCe.class, id);
	}

	public List<NFCe> todos() {
		return manager.createQuery("from nfce", NFCe.class).getResultList();
	}

	public NFCe guardar(NFCe nfce) {
		NFCe nfc = manager.merge(nfce);
		 this.event.fire(nfc);
		return nfc;
	}

	public void remover(NFCe nfce) {
		nfce = porId(nfce.getId());
		manager.remove(nfce);
		manager.flush();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<NFCe> pesquisa(NfceFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(NFCe.class);
		criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));
		return criteria.addOrder(Order.desc("numero")).list();
	}

	@SuppressWarnings("unchecked")
	public List<Object> pesquisaRelatorio(Empresa empresaLogada, Date ini, Date fin, StatusNotas[] statuses) {
		Session session = manager.unwrap(Session.class);

		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(NFCe.class);

		criteria.add(Restrictions.eq("empresa", empresaLogada));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));
		criteria.add(Restrictions.ne("status", StatusNFe.CADASTRO));
		criteria.add(Restrictions.ne("status", StatusNFe.FALHA));
		criteria.add(Restrictions.ne("status", StatusNFe.EMPROCESSAMENTO));
		criteria.add(Restrictions.between("emissao", ini, fin));

		if (statuses != null && statuses.length > 0) {
			criteria.add(Restrictions.in("status", Arrays.asList(statuses)));
		}

		criteria.add(Restrictions.isNotNull("cliente"));
		return criteria.addOrder(Order.asc("numero")).list();
	}

	public String getTotalSaida(Empresa empresaLogada) {

		Query q = manager.createQuery(
				"SELECT SUM(x.valorTotal) FROM NFCe x where empresa = :empresa and status = :status and cliente_id <> null")
				.setParameter("empresa", empresaLogada).setParameter("status", StatusNFe.AUTORIZADA);

		BigDecimal result = (BigDecimal) q.getSingleResult();

		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		if (result == null)
			result = BigDecimal.ZERO;

		return nf.format(result);
	}

	public int quantidadeFiltrados(NfceFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);

		Root<NFCe> nfceRoot = criteriaQuery.from(NFCe.class);
		Join<NFCe, Cliente> clienteJoin = nfceRoot.join("cliente", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, nfceRoot, clienteJoin);

		criteriaQuery.select(builder.count(nfceRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Long> query = manager.createQuery(criteriaQuery);

		return query.getSingleResult().intValue();
	}

	private List<Predicate> criarPredicatesParaFiltro(NfceFilter filtro, Root<NFCe> nfceRoot, From<?, ?> clienteJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(nfceRoot.get("empresa").in(Arrays.asList(usuarioLogado.getUsuario().getEmpresa())));

		filtro.setPropriedadeOrdenacao("numero");
		filtro.setAscendente(false);

		if (filtro.getNumeroDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(nfceRoot.get("numero"), filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(nfceRoot.get("numero"), filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(nfceRoot.get("emissao"), filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(nfceRoot.get("emissao"), filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			predicates.add(builder.like(clienteJoin.get("nome"), "%" + filtro.getNomeCliente() + "%"));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(nfceRoot.get("status").in(Arrays.asList(filtro.getStatuses())));
		} else {
			predicates.add(builder.notEqual(nfceRoot.get("status"), StatusNFe.EXCUIDA));
		}

		return predicates;
	}

	public List<NFCe> filtrados(NfceFilter filtro) {
		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<NFCe> criteriaQuery = builder.createQuery(NFCe.class);

		Root<NFCe> nfceRoot = criteriaQuery.from(NFCe.class);
		From<?, ?> clienteJoin = (From<?, ?>) nfceRoot.fetch("cliente", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, nfceRoot, clienteJoin);

		criteriaQuery.select(nfceRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = nfceRoot;

			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao
						.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}

			if (filtro.getPropriedadeOrdenacao().startsWith("cliente.")) {
				orderByFromEntity = clienteJoin;
			}

			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}

		TypedQuery<NFCe> query = manager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}

	

	public List<NFCe> emitidas(NfeFilter filtro) {
		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<NFCe> criteriaQuery = builder.createQuery(NFCe.class);

		Root<NFCe> NfceRoot = criteriaQuery.from(NFCe.class);
		From<?, ?> clienteJoin = (From<?, ?>) NfceRoot.fetch("cliente", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaNfcEmitidas(filtro, NfceRoot, clienteJoin);

		criteriaQuery.select(NfceRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = NfceRoot;

			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao
						.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}

			if (filtro.getPropriedadeOrdenacao().startsWith("cliente.")) {
				orderByFromEntity = clienteJoin;
			}

			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}

		TypedQuery<NFCe> query = manager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}

	private List<Predicate> criarPredicatesParaNfcEmitidas(NfeFilter filtro, Root<NFCe> NfceRoot, From<?, ?> clienteJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		filtro.setPropriedadeOrdenacao("emissao");
		filtro.setAscendente(false);

		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(NfceRoot.get("emissao"), filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(NfceRoot.get("emissao"), filtro.getDataCriacaoAte()));
		}

		predicates.add(builder.notEqual(NfceRoot.get("status"), StatusNFe.EXCUIDA));

		return predicates;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}