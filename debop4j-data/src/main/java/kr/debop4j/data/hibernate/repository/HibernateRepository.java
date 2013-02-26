package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.Guard;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.SimplePagedList;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate 용 Data Access Object 입니다.
 * Spring의 HibernateDaoSupport 및 HibernateTemplate는 더 이상 사용하지 말라.
 * 참고: http://forum.springsource.org/showthread.php?117227-Missing-Hibernate-Classes-Interfaces-in-spring-orm-3.1.0.RC1
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 27.
 */
@Repository
@Slf4j
@SuppressWarnings("unchecked")
public class HibernateRepository<E extends IStatefulEntity> implements IHibernateRepository<E> {

    @Getter
    private Class<E> entityClass;

    String entityName;

    public HibernateRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
        this.entityName = this.entityClass.getName();
    }

    protected Session getSession() {
        return UnitOfWorks.getCurrentSession();
    }

    @Override
    public E load(Serializable id) {
        return (E) getSession().load(entityClass, id);
    }

    @Override
    public E load(Serializable id, LockOptions lockOptions) {
        return (E) getSession().load(entityClass, id, lockOptions);
    }

    @Override
    public E get(Serializable id) {
        return (E) getSession().get(entityClass, id);
    }

    @Override
    public E get(Serializable id, LockOptions lockOptions) {
        return (E) getSession().get(entityClass, id, lockOptions);
    }

    @Override
    public List<E> getIn(Collection ids) {
        if (!ArrayTool.isEmpty(ids))
            return new ArrayList<>();

        return find(CriteriaTool.addIn(DetachedCriteria.forClass(entityClass), "id", ids));
    }

    @Override
    public List<E> getIn(Object[] ids) {
        if (!ArrayTool.isEmpty(ids))
            return new ArrayList<>();

        return find(CriteriaTool.addIn(DetachedCriteria.forClass(entityClass), "id", ids));
    }

    @Override

    public List<E> getAll() {
        return getSession().createQuery("from " + entityName).list();
    }

    protected final List<E> find(Criteria criteria) {
        return criteria.list();
    }

    protected final List<E> find(Criteria criteria, int firstResult, int maxResults, Order... orders) {
        HibernateTool.setPaging(criteria, firstResult, maxResults);
        HibernateTool.addOrders(criteria, orders);
        return criteria.list();
    }


    @Override
    public List<E> find(DetachedCriteria dc) {
        return dc.getExecutableCriteria(getSession()).list();
    }

    @Override
    public List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders) {
        return find(dc.getExecutableCriteria(getSession()), firstResult, maxResults, orders);
    }

    @Override
    public List<E> findByCriteria(Criterion... criterions) {
        return findByCriteria(criterions, -1, -1);
    }

    @Override
    public List<E> findByCriteria(Criterion[] criterions, int firstResult, int maxResults, Order... orders) {
        Criteria criteria = getSession().createCriteria(entityClass);
        HibernateTool.addCriterions(criteria, criterions);

        return find(criteria, firstResult, maxResults, orders);
    }

    @Override
    public List<E> findByExample(Example example) {
        return getSession().createCriteria(entityClass).add(example).list();
    }

    @Override
    public List<E> findByQuery(Query query, HibernateParameter... parameters) {
        return findByQuery(query, -1, -1, parameters);
    }

    @Override
    public List<E> findByQuery(Query query, int firstResult, int maxResults, HibernateParameter... parameters) {
        HibernateTool.setPaging(query, firstResult, maxResults);
        HibernateTool.setParameters(query, parameters);

        return query.list();
    }

    @Override
    public List<E> findByQueryString(String queryString, HibernateParameter... parameters) {

        return findByQueryString(queryString, -1, -1, parameters);
    }

    @Override
    public List<E> findByQueryString(String queryString, int firstResult, int maxResults, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(queryString, "queryString");
        if (log.isDebugEnabled())
            log.debug("쿼리문을 실행합니다. queryString=[{}], pageNo=[{}], pageSize=[{}],parameters=[{}]",
                      queryString, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createQuery(queryString);
        return findByQuery(query, firstResult, maxResults, parameters);
    }

    @Override
    public List<E> findByNamedQuery(String queryName, HibernateParameter... parameters) {
        return findByNamedQuery(queryName, -1, -1, parameters);
    }

    @Override
    public List<E> findByNamedQuery(String queryName, int firstResult, int maxResults, HibernateParameter... parameters) {
        Guard.shouldNotBeEmpty(queryName, "queryName");
        if (log.isDebugEnabled())
            log.debug("NamedQuery를 로드하여 실행합니다. queryName=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      queryName, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findByQuery(query, firstResult, maxResults, parameters);
    }


    protected final IPagedList<E> getPage(Criteria criteria, int pageNo, int pageSize, Order... orders) {

        final Criteria countCriteria = HibernateTool.copyCriteria(criteria);
        long itemCount = count(countCriteria);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = find(criteria, firstResult, pageSize, orders);

        return new SimplePagedList<E>(list, firstResult, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders) {

        DetachedCriteria countDc = HibernateTool.copyDetachedCriteria(dc);
        long itemCount = count(countDc);

        int firstResult = (pageNo - 1) * pageSize;
        List list = find(dc, firstResult, pageSize, orders);

        return new SimplePagedList(list, firstResult, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPageByQuery(Query query, int pageNo, int pageSize, HibernateParameter... parameters) {
        Query countQuery = getSession().createQuery(query.getQueryString());
        long itemCount = count(countQuery, parameters);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = findByQuery(query, firstResult, pageSize, parameters);

        return new SimplePagedList<E>(list, firstResult, pageSize, itemCount);
    }

    @Override
    public IPagedList<E> getPageByQueryString(String queryString, int pageNo, int pageSize, HibernateParameter... parameters) {
        return getPageByQuery(getSession().createQuery(queryString),
                              pageNo,
                              pageSize,
                              parameters);
    }

    @Override
    public IPagedList<E> getPageByNamedQuery(String queryName, int pageNo, int pageSize, HibernateParameter... parameters) {
        return getPageByQuery(getSession().getNamedQuery(queryName),
                              pageNo,
                              pageSize,
                              parameters);
    }

    @Override
    public E findOne(DetachedCriteria dc) {
        return (E) dc.getExecutableCriteria(getSession()).uniqueResult();
    }

    @Override
    public E findOneByCriteria(Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion criterion : criterions)
            criteria.add(criterion);

        return (E) criteria.uniqueResult();
    }

    @Override
    public E findOneByQuery(Query query, HibernateParameter... parameters) {
        return (E) HibernateTool.setParameters(query, parameters).uniqueResult();
    }

    @Override
    public E findOneByQueryString(String queryString, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
            log.debug("쿼리문을 수행하여 유일한 값을 조회합니다. 없거나 복수개이면 예외가 발생합니다. " +
                              "queryString=[{}], parameters=[{}]",
                      queryString, StringTool.listToString(parameters));

        Query query = getSession().createQuery(queryString);
        return findOneByQuery(query, parameters);
    }

    @Override
    public E findOneByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
            log.debug("쿼리문을 수행하여 유일한 값을 조회합니다. 없거나 복수개이면 예외가 발생합니다. " +
                              "queryName=[{}], parameters=[{}]",
                      queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findOneByQuery(query, parameters);
    }


    protected final E findFirst(Criteria criteria) {
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
    public E findFirstByCriteria(Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        HibernateTool.addCriterions(criteria, criterions);
        return findFirst(criteria);
    }

    @Override
    public E findFirstByQuery(Query query, HibernateParameter... parameters) {
        List<E> list = findByQuery(query, 0, 1, parameters);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public E findFirstByQueryString(String queryString, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
            log.debug("쿼리문 실행. queryString=[{}], parameters=[{}]", queryString, StringTool.listToString(parameters));
        return findFirstByQuery(getSession().createQuery(queryString), parameters);
    }

    @Override
    public E findFirstByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
            log.debug("쿼리문 실행. queryName=[{}], parameters=[{}]", queryName, StringTool.listToString(parameters));
        return findFirstByQuery(getSession().getNamedQuery(queryName), parameters);
    }

    protected boolean exists(Criteria criteria) {
        return (findFirst(criteria) != null);
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
    public boolean existsByCriteria(Criterion... criterions) {
        return (findFirstByCriteria(criterions) != null);
    }

    @Override
    public boolean existsByQuery(Query query, HibernateParameter... parameters) {
        return (findFirstByQuery(query, parameters) != null);
    }

    @Override
    public boolean existsByQueryString(String queryString, HibernateParameter... parameters) {
        return (findFirstByQueryString(queryString, parameters) != null);
    }

    @Override
    public boolean existsByNamedQuery(String queryName, HibernateParameter... parameters) {
        return (findFirstByNamedQuery(queryName, parameters) != null);
    }


    protected long count(Criteria criteria) {
        Object count = criteria.setProjection(Projections.rowCount())
                .uniqueResult();
        return ((Number) count).longValue();
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
    public long countByCriteria(Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        HibernateTool.addCriterions(criteria, criterions);

        return count(criteria);
    }

    protected final long count(Query query, HibernateParameter... parameters) {
        // TODO: 테스트가 필요합니다!!!
        Object count = query.setResultTransformer(Criteria.PROJECTION)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .uniqueResult();

        return ((Number) count).longValue();
    }

    @Override
    public void merge(E entity) {
        getSession().merge(entityName, entity);
    }

    @Override
    public void persist(E entity) {
        getSession().persist(entityName, entity);
    }

    @Override
    public void save(E entity) {
        getSession().save(entityName, entity);
    }

    @Override
    public void saveOrUpdate(E entity) {
        getSession().saveOrUpdate(entityName, entity);
    }

    @Override
    public void update(E entity) {
        getSession().saveOrUpdate(entityName, entity);
    }

    @Override
    public void delete(E entity) {
        getSession().delete(entityName, entity);
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
        return
                getSession()
                        .createQuery("delete from " + entityName)
                        .executeUpdate();
    }

    @Override
    public int executeUpdateByQueryString(String queryString, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
            log.debug("Update나 Delete용 쿼리를 수행합니다. queryString=[{}], parameters=[{}]",
                      queryString, StringTool.listToString(parameters));

        Query query = getSession().createQuery(queryString);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }


    @Override
    public int executeUpdateByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (log.isDebugEnabled())
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
        criteria.setProjection(distinctResult
                                       ? Projections.distinct(projection)
                                       : projection);
        criteria.setResultTransformer(Transformers.aliasToBean(projectClass));

        return criteria;
    }
}
