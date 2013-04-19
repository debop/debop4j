package kr.debop4j.data.hibernate.repository.impl;

import kr.debop4j.core.Guard;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.SimplePagedList;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.Getter;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate 엔티티에 대한 CRUD를 수해하는 Repository 입니다.
 * Spring의 HibernateDaoSupport 및 HibernateTemplate는 더 이상 사용하지 말라.
 * 참고: http://forum.springsource.org/showthread.php?117227-Missing-Hibernate-Classes-Interfaces-in-spring-orm-3.1.0.RC1
 *
 * @author sunghyouk.bae@gmail.com
 */
@SuppressWarnings("unchecked")
public class HibernateRepository<E extends IStatefulEntity> implements IHibernateRepository<E> {

    // Slf4j
    private static final Logger log = LoggerFactory.getLogger(HibernateRepository.class);
    private static final boolean isTranceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter
    private Class<E> entityClass;
    private String entityName;
    private boolean cacheable;

    public HibernateRepository(Class<E> entityClass) {
        this(entityClass, false);
    }

    public HibernateRepository(Class<E> entityClass, boolean cacheable) {
        this.entityClass = entityClass;
        this.entityName = this.entityClass.getName();
        this.cacheable = cacheable;
    }

    protected Session getSession() {
        return UnitOfWorks.getCurrentSession();
    }

    public void flush() {
        if (isDebugEnabled)
            log.debug("flush session...");
        getSession().flush();
    }

    @Override
    public E load(Serializable id) {
        if (isTranceEnabled)
            log.trace("laod entity... class=[{}], id=[{}]", entityClass, id);
        return (E) getSession().load(entityClass, id);
    }

    @Override
    public E load(Serializable id, LockOptions lockOptions) {
        if (isTranceEnabled)
            log.trace("laod entity... class=[{}], id=[{}], lockOptions=[{}]", entityClass, id, lockOptions);
        return (E) getSession().load(entityClass, id, lockOptions);
    }

    @Override
    public E get(Serializable id) {
        if (isTranceEnabled)
            log.trace("get entity... class=[{}], id=[{}]", entityClass, id);
        return (E) getSession().get(entityClass, id);
    }

    @Override
    public E get(Serializable id, LockOptions lockOptions) {
        if (isTranceEnabled)
            log.trace("get entity... class=[{}], id=[{}], lockOptions=[{}]", entityClass, id, lockOptions);
        return (E) getSession().get(entityClass, id, lockOptions);
    }

    @Override
    public List<E> getIn(Collection ids) {
        if (ArrayTool.isEmpty(ids))
            return new ArrayList<E>();

        return find(CriteriaTool.addIn(DetachedCriteria.forClass(entityClass), "id", ids));
    }

    @Override
    public List<E> getIn(Serializable[] ids) {
        if (ArrayTool.isEmpty(ids))
            return new ArrayList<E>();

        return find(CriteriaTool.addIn(DetachedCriteria.forClass(entityClass), "id", ids));
    }

    @Override
    public List<E> getAll() {
        return getSession().createCriteria(entityClass).setCacheable(cacheable).list();
    }

    protected final List<E> find(Criteria criteria) {
        return criteria.list();
    }

    protected final List<E> find(Criteria criteria, int firstResult, int maxResults, Order... orders) {
        HibernateTool.setPaging(criteria, firstResult, maxResults);
        if (!ArrayTool.isEmpty(orders))
            HibernateTool.addOrders(criteria, orders);
        return criteria.list();
    }


    @Override
    public List<E> find(DetachedCriteria dc) {
        assert dc != null;
        return dc.getExecutableCriteria(getSession()).list();
    }

    @Override
    public List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders) {
        assert dc != null;
        return find(dc.getExecutableCriteria(getSession()), firstResult, maxResults, orders);
    }

    @Override
    public List<E> findByExample(Example example) {
        return getSession().createCriteria(entityClass).add(example).list();
    }

    @Override
    public List<E> find(Query query, HibernateParameter... parameters) {
        return find(query, -1, -1, parameters);
    }

    @Override
    public List<E> find(Query query, int firstResult, int maxResults, HibernateParameter... parameters) {
        HibernateTool.setPaging(query, firstResult, maxResults);
        HibernateTool.setParameters(query, parameters);

        return query.list();
    }

    @Override
    public List<E> findByHql(String hql, HibernateParameter... parameters) {

        return findByHql(hql, -1, -1, parameters);
    }

    @Override
    public List<E> findByHql(String hql, int firstResult, int maxResults, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(hql, "hql");
        if (isDebugEnabled)
            log.debug("쿼리문을 실행합니다. hql=[{}], pageNo=[{}], pageSize=[{}],parameters=[{}]",
                      hql, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return find(query, firstResult, maxResults, parameters);
    }

    @Override
    public List<E> findByNamedQuery(String queryName, HibernateParameter... parameters) {
        return findByNamedQuery(queryName, -1, -1, parameters);
    }

    @Override
    public List<E> findByNamedQuery(String queryName, int firstResult, int maxResults, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(queryName, "sqlString");
        if (isDebugEnabled)
            log.debug("NamedQuery를 로드하여 실행합니다. queryName=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      queryName, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return find(query, firstResult, maxResults, parameters);
    }

    @Override
    public List<E> findBySQLString(String sqlString, HibernateParameter... parameters) {
        return findBySQLString(sqlString, -1, -1, parameters);
    }

    @Override
    public List<E> findBySQLString(String sqlString, int firstResult, int maxResults, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(sqlString, "sqlString");
        if (isDebugEnabled)
            log.debug("SQLString을 수행합니다. sqlString=[{}], firstResult=[{}], maxResults=[{}], paramters=[{}]",
                      sqlString, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createSQLQuery(sqlString);
        return find(query, firstResult, maxResults, parameters);
    }

    protected final IPagedList<E> getPage(Criteria criteria, int pageNo, int pageSize, Order... orders) {

        final Criteria countCriteria = HibernateTool.copyCriteria(criteria);
        long itemCount = count(countCriteria);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = find(criteria, firstResult, pageSize, orders);

        return new SimplePagedList<E>(list, pageNo, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders) {

        DetachedCriteria countDc = HibernateTool.copyDetachedCriteria(dc);
        long itemCount = count(countDc);

        List list = find(dc, (pageNo - 1) * pageSize, pageSize, orders);
        return new SimplePagedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPage(Query query, int pageNo, int pageSize, HibernateParameter... parameters) {
        Query countQuery = getSession().createQuery(query.getQueryString());
        long itemCount = count(countQuery, parameters);

        List<E> list = find(query, (pageNo - 1) * pageSize, pageSize, parameters);
        return new SimplePagedList<E>(list, pageNo, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPageByHql(String hql, int pageNo, int pageSize, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(hql, "hql");
        if (isDebugEnabled)
            log.debug("쿼리문을 실행합니다. hql=[{}], pageNo=[{}], pageSize=[{}],parameters=[{}]",
                      hql, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return getPage(query, pageNo, pageSize, parameters);
    }

    @Override
    public IPagedList<E> getPageByNamedQuery(String queryName, int pageNo, int pageSize, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(queryName, "sqlString");
        if (isDebugEnabled)
            log.debug("NamedQuery를 로드하여 실행합니다. queryName=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      queryName, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return getPage(query, pageNo, pageSize, parameters);
    }

    @Override
    public IPagedList<E> getPageBySQLString(String sqlString, int pageNo, int pageSize, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(sqlString, "sqlString");
        if (isDebugEnabled)
            log.debug("SQLString을 수행합니다. sqlString=[{}], firstResult=[{}], maxResults=[{}], paramters=[{}]",
                      sqlString, pageNo, pageSize, StringTool.listToString(parameters));
        Query query = getSession().createSQLQuery(sqlString);
        return getPage(query, pageNo, pageSize, parameters);
    }

    @Override
    public E findOne(DetachedCriteria dc) {
        assert dc != null;
        return (E) dc.getExecutableCriteria(getSession()).uniqueResult();
    }

    @Override
    public E findOne(Criteria criteria) {
        assert criteria != null;
        return (E) criteria.uniqueResult();
    }

    @Override
    public E findOne(Query query, HibernateParameter... parameters) {
        return (E) HibernateTool.setParameters(query, parameters).uniqueResult();
    }

    @Override
    public E findOneByHql(String hql, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("쿼리문을 수행하여 유일한 값을 조회합니다. 없거나 복수개이면 예외가 발생합니다. hql=[{}], parameters=[{}]",
                      hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findOne(query, parameters);
    }

    @Override
    public E findOneByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("쿼리문을 수행하여 유일한 값을 조회합니다. 없거나 복수개이면 예외가 발생합니다. queryName=[{}], parameters=[{}]",
                      queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findOne(query, parameters);
    }

    @Override
    public E findOneBySQLString(String sqlString, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("쿼리문을 수행하여 유일한 값을 조회합니다. 없거나 복수개이면 예외가 발생합니다. sqlString=[{}], parameters=[{}]",
                      sqlString, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(sqlString);
        return findOne(query, parameters);
    }

    @Override
    public E findFirst(Criteria criteria) {
        List<E> list = find(criteria, 0, 1);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public E findFirst(DetachedCriteria dc) {
        return findFirst(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public E findFirst(Query query, HibernateParameter... parameters) {
        List<E> list = find(query, 0, 1, parameters);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public E findFirstByHql(String hql, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("HQL문 실행. hql=[{}], parameters=[{}]", hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findFirst(query, parameters);
    }

    @Override
    public E findFirstByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("NamedQuery 실행. queryName=[{}], parameters=[{}]", queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findFirst(query, parameters);
    }

    @Override
    public E findFirstBySQLString(String sqlString, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("일반 쿼리문 실행. sqlString=[{}], parameters=[{}]", sqlString, StringTool.listToString(parameters));

        Query query = getSession().createSQLQuery(sqlString);
        return findFirst(query, parameters);
    }

    @Override
    public boolean exists() {
        return exists(getSession().createCriteria(entityClass));
    }

    @Override
    public boolean exists(DetachedCriteria dc) {
        return exists(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public boolean exists(Criteria criteria) {
        return (findFirst(criteria) != null);
    }

    @Override
    public boolean exists(Query query, HibernateParameter... parameters) {
        return (findFirst(query, parameters) != null);
    }

    @Override
    public boolean existsByHql(String hql, HibernateParameter... parameters) {
        return (findFirstByHql(hql, parameters) != null);
    }

    @Override
    public boolean existsByNamedQuery(String queryName, HibernateParameter... parameters) {
        return (findFirstByNamedQuery(queryName, parameters) != null);
    }

    @Override
    public boolean existsBySQLString(String sqlString, HibernateParameter... parameters) {
        return (findFirstBySQLString(sqlString, parameters) != null);
    }

    @Override
    public long count() {
        return count(getSession().createCriteria(entityClass));
    }

    @Override
    public long count(DetachedCriteria dc) {
        return count(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public long count(Criteria criteria) {
        Object count = criteria.setProjection(Projections.rowCount())
                .uniqueResult();
        return ((Number) count).longValue();
    }

    protected final long count(Query query, HibernateParameter... parameters) {
        // TODO: 테스트가 필요합니다!!!
        Object count = query.setResultTransformer(Criteria.PROJECTION)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .uniqueResult();

        return ((Number) count).longValue();
    }

    @Override
    public E merge(E entity) {
        return (E) getSession().merge(entity);
    }

    @Override
    public void persist(E entity) {
        getSession().persist(entity);
    }

    @Override
    public Serializable save(E entity) {
        return getSession().save(entity);
    }

    @Override
    public void saveOrUpdate(E entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void update(E entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(E entity) {
        getSession().delete(entity);
    }

    @Override
    public void deleteEntities(Collection<E> entities) {
        Session session = getSession();
        for (E entity : entities)
            session.delete(entityName, entity);
    }

    @Override
    public void delete(DetachedCriteria dc) {
        deleteEntities(find(dc));
    }

    @Override
    public void deleteById(Serializable id) {
        getSession().delete(load(id));
    }

    @Override
    public int deleteAllWithoutCascade() {
        return getSession()
                .createQuery("delete from " + entityName)
                .executeUpdate();
    }

    @Override
    public int executeUpdateByHql(String hql, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("Update나 Delete용 쿼리를 수행합니다. hql=[{}], parameters=[{}]",
                      hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }


    @Override
    public int executeUpdateByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (isDebugEnabled)
            log.debug("Update나 Delete용 쿼리를 수행합니다. queryName=[{}], parameters=[{}]",
                      queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }


    @Override
    public <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, DetachedCriteria dc) {
        return reportOne(projectClass, projectionList, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, Criteria criteria) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, true);
        return (TProject) projectCriteria.uniqueResult();
    }

    @Override
    public <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                                ProjectionList projectionList,
                                                DetachedCriteria dc) {
        return reportList(projectClass, projectionList, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                                ProjectionList projectionList,
                                                DetachedCriteria dc,
                                                int firstResult,
                                                int maxResults) {
        return reportList(projectClass,
                          projectionList,
                          dc.getExecutableCriteria(getSession()),
                          firstResult,
                          maxResults);
    }

    @Override
    public <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                                ProjectionList projectionList,
                                                Criteria criteria) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, false);
        return projectCriteria.list();
    }

    @Override
    public <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                                ProjectionList projectionList,
                                                Criteria criteria,
                                                int firstResult,
                                                int maxResults) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, false);
        HibernateTool.setPaging(projectCriteria, firstResult, maxResults);
        return projectCriteria.list();
    }

    @Override
    public <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                                      ProjectionList projectionList,
                                                      DetachedCriteria dc,
                                                      int pageNo,
                                                      int pageSize) {
        return reportPage(projectClass,
                          projectionList,
                          dc.getExecutableCriteria(getSession()),
                          pageNo,
                          pageSize);
    }

    @Override
    public <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                                      ProjectionList projectionList,
                                                      Criteria criteria,
                                                      int pageNo,
                                                      int pageSize) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, false);

        long itemCount = count(projectCriteria);
        int firstResult = (pageNo - 1) * pageSize;
        HibernateTool.setPaging(projectCriteria, firstResult, pageSize);

        return new SimplePagedList(projectCriteria.list(), pageNo, pageSize, itemCount);
    }

    protected final <TProject> Criteria buildProjectionCriteria(Class<TProject> projectClass,
                                                                Criteria criteria,
                                                                Projection projection,
                                                                boolean distinctResult) {

        if (distinctResult)
            criteria.setProjection(Projections.distinct(projection));
        else
            criteria.setProjection(projection);

        criteria.setResultTransformer(Transformers.aliasToBean(projectClass));
        return criteria;
    }
}
