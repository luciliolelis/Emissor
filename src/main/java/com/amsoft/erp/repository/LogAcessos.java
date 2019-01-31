package com.amsoft.erp.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.amsoft.erp.model.LogAcesso;
import com.amsoft.erp.repository.filter.NfeFilter;

public class LogAcessos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public LogAcesso guardar(LogAcesso log) {
		return manager.merge(log);
	}

	public List<LogAcesso> filtrados(NfeFilter filtro) {
		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LogAcesso> criteriaQuery = builder.createQuery(LogAcesso.class);

		Root<LogAcesso> LogRoot = criteriaQuery.from(LogAcesso.class);

		List<Predicate> predicates = criarPredicates(filtro, LogRoot);

		criteriaQuery.select(LogRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = LogRoot;

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

		TypedQuery<LogAcesso> query = manager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}

	private List<Predicate> criarPredicates(NfeFilter filtro, Root<LogAcesso> LogRoot) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		filtro.setPropriedadeOrdenacao("data_hora");
		filtro.setAscendente(false);

		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(LogRoot.get("data_hora"), filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(LogRoot.get("data_hora"), filtro.getDataCriacaoAte()));
		}

		return predicates;
	}

	
	
}