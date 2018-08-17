package com.amsoft.erp.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.amsoft.erp.model.Cliente;
import com.amsoft.erp.model.Empresa;
import com.amsoft.erp.model.StatusNFe;
import com.amsoft.erp.model.StatusNotas;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.model.nfce.NFCe;
import com.amsoft.erp.model.nfe.ItemDuplicata;
import com.amsoft.erp.model.nfe.Nfe;
import com.amsoft.erp.model.nfe.TipoDocumento;
import com.amsoft.erp.model.vo.DataValor;
import com.amsoft.erp.repository.filter.NfeFilter;
import com.amsoft.erp.security.UsuarioLogado;
import com.amsoft.erp.security.UsuarioSistema;

public class Nfes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	UsuarioSistema usuarioLogado;

	@Inject
	private EntityManager manager;

	public Nfe porId(Long id) {
		return manager.find(Nfe.class, id);
	}

	public List<Nfe> todos() {
		return manager.createQuery("from Nfe", Nfe.class).getResultList();
	}

	public Nfe guardar(Nfe nfe) {
		return manager.merge(nfe);
	}

	public void remover(Nfe nfe) {
		nfe = porId(nfe.getId());
		manager.remove(nfe);
		manager.flush();
	}

	public List<ItemDuplicata> perquisaDuplicatas(Nfe nfe) {

		return this.manager.createQuery("from ItemDuplicata where nfe_id = :nfe_id", ItemDuplicata.class)
				.setParameter("nfe_id", nfe.getId()).getResultList();

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Nfe> pesquisa(NfeFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Nfe.class);
		criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));

		return criteria.addOrder(Order.desc("numero")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Object> pesquisaRelatorio(Empresa empresaLogada, Date ini, Date fin, StatusNotas[] statuses) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Nfe.class);

		criteria.add(Restrictions.eq("empresa", empresaLogada));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));
		criteria.add(Restrictions.ne("status", StatusNFe.CADASTRO));
		criteria.add(Restrictions.ne("status", StatusNFe.FALHA));
		criteria.add(Restrictions.ne("status", StatusNFe.EMPROCESSAMENTO));
		criteria.add(Restrictions.between("emissao", ini, fin));

		Criterion rest1 = null;
		Criterion rest2;

		if (statuses != null && statuses.length > 0) {

			if (Arrays.asList(statuses).contains(StatusNotas.AUTORIZADA)) {
				rest1 = Restrictions.eq("status", StatusNFe.AUTORIZADACORRECAO);
			}

			rest2 = Restrictions.in("status", Arrays.asList(statuses));

			if (rest1 != null) {
				criteria.add(Restrictions.or(rest1, rest2));
			} else {
				criteria.add(rest2);
			}

		}

		return criteria.addOrder(Order.asc("numero")).list();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor, Empresa empresaLogada) {
		Session session = manager.unwrap(Session.class);

		numeroDeDias -= 1;

		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

		Criteria criteria = session.createCriteria(NFCe.class);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(emissao) as data", "date(emissao)", new String[] { "data" },
						new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.sum("valorTotal").as("valor"))).add(Restrictions.ge("emissao", dataInicial.getTime()));

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("usuario", criadoPor));
		}

		criteria.add(Restrictions.eq("empresa", empresaLogada));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));

		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class))
				.list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}

		return resultado;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Map<Date, BigDecimal> valoresTotaisPorDataNfe(Integer numeroDeDias, Usuario criadoPor,
			Empresa empresaLogada) {
		Session session = manager.unwrap(Session.class);

		numeroDeDias -= 1;

		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

		Criteria criteria = session.createCriteria(Nfe.class);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(emissao) as data", "date(emissao)", new String[] { "data" },
						new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.sum("valorTotal").as("valor"))).add(Restrictions.ge("emissao", dataInicial.getTime()));

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("usuario", criadoPor));
		}

		criteria.add(Restrictions.eq("empresa", empresaLogada));
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));

		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class))
				.list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}

		return resultado;
	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}

	public List<Nfe> porChave(String chave, Empresa empresaLogada) {

		List<Nfe> list = this.manager
				.createQuery(
						"from Nfe where upper(chave) like :chave and empresa = :empresa and (status = :status or status = :statusCorrecao)",
						Nfe.class)
				.setParameter("chave", chave.toUpperCase() + "%").setParameter("empresa", empresaLogada)
				.setParameter("status", StatusNFe.AUTORIZADA)
				.setParameter("statusCorrecao", StatusNFe.AUTORIZADACORRECAO).getResultList();

		return list;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Nfe> porNumeroSerie(Nfe nfe) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Nfe.class);
		criteria.add(Restrictions.ne("status", StatusNFe.EXCUIDA));
		Criterion rest1 = Restrictions.and(Restrictions.eq("numero", nfe.getNumero()),
				Restrictions.eq("serie", nfe.getSerie()));
		Criterion rest2 = Restrictions.ne("id", nfe.getId());

		criteria.add(Restrictions.and(rest1, rest2));
		criteria.add(Restrictions.eq("empresa", nfe.getEmpresa()));

		return criteria.list();
	}

	public String getTotalSaida(Empresa empresaLogada) {

		Query q = manager
				.createQuery(
						"SELECT SUM(x.valorTotal) FROM Nfe x where empresa = :empresa and tipoDocumento = :tipoDocumento and status <> :status")
				.setParameter("empresa", empresaLogada).setParameter("tipoDocumento", TipoDocumento.SAIDA)
				.setParameter("status", StatusNFe.EXCUIDA);
		BigDecimal result = (BigDecimal) q.getSingleResult();

		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		if (result == null)
			result = BigDecimal.ZERO;

		return nf.format(result);
	}

	public String getTotalEntrada(Empresa empresaLogada) {

		Query q = manager
				.createQuery(
						"SELECT SUM(x.valorTotal) FROM Nfe x where empresa = :empresa and tipoDocumento = :tipoDocumento and status <> :status")
				.setParameter("empresa", empresaLogada).setParameter("tipoDocumento", TipoDocumento.ENTRADA)
				.setParameter("status", StatusNFe.EXCUIDA);

		BigDecimal result = (BigDecimal) q.getSingleResult();

		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

		if (result == null)
			result = BigDecimal.ZERO;

		return nf.format(result);
	}

	@SuppressWarnings("deprecation")
	public String getNumeroNotasEmitidas(Empresa empresaLogada) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Nfe.class);
		criteria.add(Restrictions.eq("empresa", empresaLogada));
		criteria.add(Restrictions.eq("status", StatusNFe.AUTORIZADA));
		criteria.add(Restrictions.eq("status", StatusNFe.AUTORIZADACORRECAO));

		criteria.setProjection(Projections.countDistinct("id"));

		return (String) criteria.setProjection(Projections.rowCount()).uniqueResult().toString();

	}

	public int quantidadeFiltrados(NfeFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);

		Root<Nfe> NfeRoot = criteriaQuery.from(Nfe.class);
		Join<Nfe, Cliente> clienteJoin = NfeRoot.join("cliente", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, NfeRoot, clienteJoin);

		criteriaQuery.select(builder.count(NfeRoot));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Long> query = manager.createQuery(criteriaQuery);

		return query.getSingleResult().intValue();
	}

	private List<Predicate> criarPredicatesParaFiltro(NfeFilter filtro, Root<Nfe> NfeRoot, From<?, ?> clienteJoin) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(NfeRoot.get("empresa").in(Arrays.asList(usuarioLogado.getUsuario().getEmpresa())));
		filtro.setPropriedadeOrdenacao("numero");
		filtro.setAscendente(false);

		if (filtro.getNumeroDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(NfeRoot.get("numero"), filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(NfeRoot.get("numero"), filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(NfeRoot.get("emissao"), filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(NfeRoot.get("emissao"), filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {
			predicates.add(builder.like(clienteJoin.get("nome"), "%" + filtro.getNomeCliente() + "%"));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(NfeRoot.get("status").in(Arrays.asList(filtro.getStatuses())));
		} else {
			predicates.add(builder.notEqual(NfeRoot.get("status"), StatusNFe.EXCUIDA));
		}

		return predicates;
	}

	public List<Nfe> filtrados(NfeFilter filtro) {
		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Nfe> criteriaQuery = builder.createQuery(Nfe.class);

		Root<Nfe> NfeRoot = criteriaQuery.from(Nfe.class);
		From<?, ?> clienteJoin = (From<?, ?>) NfeRoot.fetch("cliente", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, NfeRoot, clienteJoin);

		criteriaQuery.select(NfeRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = NfeRoot;

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

		TypedQuery<Nfe> query = manager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}
}