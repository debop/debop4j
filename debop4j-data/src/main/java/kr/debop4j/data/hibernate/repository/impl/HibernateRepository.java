/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.hibernate.repository.impl;

import com.google.common.collect.Lists;
import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.core.collection.PaginatedList;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.Getter;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate 엔티티에 대한 CRUD를 수행하는 Repository 입니다.
 * Spring의 HibernateDaoSupport 및 HibernateTemplate는 더 이상 사용하지 말라.
 * 참고: http://forum.springsource.org/showthread.php?117227-Missing-Hibernate-Classes-Interfaces-in-spring-orm-3.1.0.RC1
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 */
@Repository
@SuppressWarnings("unchecked")
public class HibernateRepository<E extends IStatefulEntity> implements IHibernateRepository<E> {

    private static final Logger log = LoggerFactory.getLogger(HibernateRepository.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Getter
    private final boolean cacheable;
    private final Class<E> entityClazz;

    /**
     * Instantiates a new HibernateRepository.
     *
     * @param entityClazz 엔티티 수형
     */
    public HibernateRepository(Class<E> entityClazz) {
        this(entityClazz, false);
    }

    /**
     * Instantiates a new HibernateRepository.
     *
     * @param entityClazz 엔티티 수형
     * @param cacheable   캐시 여부
     */
    public HibernateRepository(Class<E> entityClazz, boolean cacheable) {
        this.entityClazz = entityClazz;
        this.cacheable = cacheable;
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClazz;
    }

    @Override
    public Session getSession() {
        return UnitOfWorks.getCurrentSession();
    }

    @Override
    public final void flushSession() {
        if (isDebugEnabled)
            log.debug("Session 정보를 flush 합니다...");

        getSession().flush();
    }

    @Override
    public final void transactionalFlush() {
        UnitOfWorks.getCurrent().transactionalFlush();
    }

    @Override
    public <E> E load(Serializable id) {
        if (isTraceEnabled)
            log.trace("load entity... id=[{}]", entityClazz, id);

        return (E) getSession().load(entityClazz, id);
    }

    @Override
    public <E> E load(Serializable id, LockOptions lockOptions) {
        if (isTraceEnabled)
            log.trace("load entity... id=[{}], lockOptions=[{}]", entityClazz, id, lockOptions);

        return (E) getSession().load(entityClazz, id, lockOptions);
    }

    @Override
    public <E> E get(Serializable id) {
        if (isTraceEnabled)
            log.trace("get entity... id=[{}]", entityClazz, id);

        return (E) getSession().get(entityClazz, id);
    }

    @Override
    public <E> E get(Serializable id, LockOptions lockOptions) {
        if (isTraceEnabled)
            log.trace("get entity... id=[{}], lockOptions=[{}]", entityClazz, id, lockOptions);

        return (E) getSession().get(entityClazz, id, lockOptions);
    }

    @Override
    public <E> List<E> getIn(Collection<? extends Serializable> ids) {
        if (ArrayTool.isEmpty(ids))
            return Lists.newArrayList();

        DetachedCriteria dc = CriteriaTool.addIn(DetachedCriteria.forClass(entityClazz), "id", ids);
        return find(dc);
    }

    @Override
    public <E> List<E> getIn(Serializable[] ids) {
        if (ArrayTool.isEmpty(ids))
            return Lists.newArrayList();

        DetachedCriteria dc = CriteriaTool.addIn(DetachedCriteria.forClass(entityClazz), "id", ids);
        return find(dc);
    }

    @Override
    public ScrollableResults getScroll(DetachedCriteria dc) {
        return getScroll(dc, ScrollMode.FORWARD_ONLY);
    }

    @Override
    public ScrollableResults getScroll(DetachedCriteria dc, ScrollMode scrollMode) {
        return dc.getExecutableCriteria(getSession()).scroll(scrollMode);
    }

    @Override
    public ScrollableResults getScroll(Criteria criteria) {
        return getScroll(criteria, ScrollMode.FORWARD_ONLY);
    }

    @Override
    public ScrollableResults getScroll(Criteria criteria, ScrollMode scrollMode) {
        return criteria.scroll(scrollMode);
    }

    @Override
    public ScrollableResults getScroll(Query query, HibernateParameter... parameters) {
        return getScroll(query, ScrollMode.FORWARD_ONLY, parameters);
    }

    @Override
    public ScrollableResults getScroll(Query query, ScrollMode scrollMode, HibernateParameter... parameters) {
        return HibernateTool.setParameters(query, parameters).scroll(scrollMode);
    }

    @Override
    public final <E> List<E> findAll(Order... orders) {
        if (ArrayTool.isEmpty(orders)) {
            Query query = getSession().createQuery("from " + entityClazz.getName());
            return (List<E>) query.setCacheable(cacheable).list();
        } else {
            Criteria criteria = getSession().createCriteria(entityClazz);
            HibernateTool.addOrders(criteria, orders);
            return criteria.setCacheable(cacheable).list();
        }
    }

    @Override
    public final <E> List<E> findAll(int firstResult, int maxResults, Order... orders) {
        if (ArrayTool.isEmpty(orders)) {
            Query query = getSession().createQuery("from " + entityClazz.getName());
            HibernateTool.setPaging(query, firstResult, maxResults);

            return (List<E>) query.setCacheable(cacheable).list();
        } else {
            Criteria criteria = getSession().createCriteria(entityClazz);
            HibernateTool.setPaging(criteria, firstResult, maxResults);
            if (!ArrayTool.isEmpty(orders))
                HibernateTool.addOrders(criteria, orders);

            return criteria.setCacheable(cacheable).list();
        }
    }

    @Override
    public final <E> List<E> find(Criteria criteria, Order... orders) {
        if (!ArrayTool.isEmpty(orders))
            HibernateTool.addOrders(criteria, orders);

        return criteria.setCacheable(cacheable).list();
    }

    @Override
    public final <E> List<E> find(Criteria criteria, int firstResult, int maxResults, Order... orders) {
        HibernateTool.setPaging(criteria, firstResult, maxResults);
        if (!ArrayTool.isEmpty(orders))
            HibernateTool.addOrders(criteria, orders);
        return criteria.setCacheable(cacheable).list();
    }

    @Override
    public final <E> List<E> find(DetachedCriteria dc, Order... orders) {
        return find(dc.getExecutableCriteria(getSession()), orders);
    }

    @Override
    public final <E> List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders) {
        return find(dc.getExecutableCriteria(getSession()), firstResult, maxResults, orders);
    }

    @Override
    public final <E> List<E> find(Query query, HibernateParameter... parameters) {
        return find(query, -1, -1, parameters);
    }

    @Override
    public <E> List<E> find(Query query, int firstResult, int maxResults, HibernateParameter... parameters) {
        assert query != null;
        HibernateTool.setPaging(query, firstResult, maxResults);
        HibernateTool.setParameters(query, parameters);

        return (List<E>) query.list();
    }

    @Override
    public final <E> List<E> find(final String hql, HibernateParameter... parameters) {
        return find(hql, -1, -1, parameters);
    }

    @Override
    public <E> List<E> find(final String hql, int firstResult, int maxResults, HibernateParameter... parameters) {
        assert StringTool.isNotEmpty(hql);
        if (isTraceEnabled)
            log.trace("HQL문을 실행합니다. hql=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                      hql, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return find(query, firstResult, maxResults, parameters);
    }

    @Override
    public final <E> List<E> findByNamedQuery(final String queryName, HibernateParameter... parameters) {
        return findByNamedQuery(queryName, -1, -1, parameters);
    }

    @Override
    public <E> List<E> findByNamedQuery(final String queryName, int firstResult, int maxResults, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("NamedQuery를 실행합니다. sqlString=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                      queryName, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return find(query, firstResult, maxResults, parameters);
    }

    @Override
    public final <E> List<E> findBySQLString(final String sqlString, HibernateParameter... parameters) {
        return findBySQLString(sqlString, -1, -1, parameters);
    }

    @Override
    public <E> List<E> findBySQLString(final String sqlString, int firstResult, int maxResults, HibernateParameter... parameters) {
        assert StringTool.isNotEmpty(sqlString);
        if (isTraceEnabled)
            log.trace("일반 SQL 문 실행합니다. sqlString=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                      sqlString, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createSQLQuery(sqlString);
        return find(query, firstResult, maxResults, parameters);
    }

    @Override
    public <E> List<E> findByExample(Example example) {
        return getSession().createCriteria(entityClazz).add(example).list();
    }

    @Override
    public <E> PaginatedList<E> getPage(Criteria criteria, int pageNo, int pageSize, Order... orders) {
        Criteria countCriteria = HibernateTool.copyCriteria(criteria);
        long itemCount = count(countCriteria);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = find(criteria, firstResult, pageSize, orders);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <E> PaginatedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders) {
        DetachedCriteria countDc = HibernateTool.copyDetachedCriteria(dc);
        long itemCount = count(countDc);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = find(dc, firstResult, pageSize, orders);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <E> PaginatedList<E> getPage(Query query, int pageNo, int pageSize, HibernateParameter... parameters) {

        Query countQuery = getSession().createQuery(query.getQueryString());
        long itemCount = count(countQuery, parameters);

        int firstResult = (pageNo - 1) * pageSize;
        List<E> list = find(query, firstResult, pageSize, parameters);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <E> IPagedList<E> getPageByHql(final String hql, int pageNo, int pageSize, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("HQL문을 실행하고, 결과를 Paging처리합니다. hql=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      hql, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return getPage(query, pageNo, pageSize, parameters);
    }

    @Override
    public <E> IPagedList<E> getPageByNamedQuery(final String queryName, int pageNo, int pageSize, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("NamedQuery를 실행하고, 결과를 Paging처리합니다. sqlString=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      queryName, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return getPage(query, pageNo, pageSize, parameters);
    }

    @Override
    public <E> IPagedList<E> getPageBySQLString(final String sqlString, int pageNo, int pageSize, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("일반 SQL문을 실행하고, 결과를 Paging처리합니다. sqlString=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                      sqlString, pageNo, pageSize, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return getPage(query, pageNo, pageSize, parameters);
    }


    @Override
    public <E> E findUnique(DetachedCriteria dc) {
        return findUnique(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public <E> E findUnique(Criteria criteria) {
        return (E) criteria.setCacheable(cacheable).uniqueResult();
    }

    @Override
    public <E> E findUnique(Query query, HibernateParameter... parameters) {
        HibernateTool.setParameters(query, parameters);
        return (E) query.uniqueResult();
    }

    @Override
    public <E> E findUniqueByHql(String hql, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("hql을 수행합니다. hql=[{}], parameters=[{}]", hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findUnique(query, parameters);
    }

    @Override
    public <E> E findUniqueByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("NamedQuery를 수행합니다. queryName=[{}], parameters=[{}]", queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);

        if (isTraceEnabled)
            log.trace("NamedQuery를 수행합니다. queryName=[{}], queryString=[{}], parameters=[{}]",
                      queryName, query.getQueryString(), StringTool.listToString(parameters));

        return findUnique(query, parameters);
    }

    @Override
    public <E> E findUniqueBySQLString(String sqlString, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("일반 SQL문을 수행합니다. sqlString=[{}], parameters=[{}]", sqlString, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return findUnique(query, parameters);
    }

    @Override
    public <E> E findFirst(DetachedCriteria dc, Order... orders) {
        return findFirst(dc.getExecutableCriteria(getSession()), orders);
    }

    @Override
    public <E> E findFirst(Criteria criteria, Order... orders) {
        List<E> list = find(criteria, 0, 1, orders);
        return (list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public <E> E findFirst(Query query, HibernateParameter... parameters) {
        HibernateTool.setParameters(query, parameters);
        List<E> list = find(query, 0, 1, parameters);
        return (list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public <E> E findFirstByHql(String hql, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("hql을 수행합니다. hql=[{}], parameters=[{}]", hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findFirst(query, parameters);
    }

    @Override
    public <E> E findFirstByNamedQuery(String queryName, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("NamedQuery를 수행합니다. queryName=[{}], parameters=[{}]", queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findUnique(query, parameters);
    }

    @Override
    public <E> E findFirstBySQLString(String sqlString, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("일반 SQL문을 수행합니다. sqlString=[{}], parameters=[{}]", sqlString, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return findUnique(query, parameters);
    }

    @Override
    public boolean exists() {
        return exists(getSession().createCriteria(entityClazz));
    }

    @Override
    public boolean exists(DetachedCriteria dc) {
        return exists(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public boolean exists(Criteria criteria) {
        return findFirst(criteria) != null;
    }

    @Override
    public boolean exists(Query query, HibernateParameter... parameters) {
        return findFirst(query, parameters) != null;
    }

    @Override
    public boolean existsByHql(String hql, HibernateParameter... parameters) {
        return findFirstByHql(hql, parameters) != null;
    }

    @Override
    public boolean existsByNamedQuery(String queryName, HibernateParameter... parameters) {
        return findFirstByNamedQuery(queryName, parameters) != null;
    }

    @Override
    public boolean existsBySQLString(String sqlString, HibernateParameter... parameters) {
        return findFirstBySQLString(sqlString, parameters) != null;
    }

    @Override
    public long count() {
        return count(getSession().createCriteria(entityClazz));
    }

    @Override
    public long count(Criteria criteria) {
        Object count = criteria.setProjection(Projections.rowCount()).uniqueResult();
        if (isTraceEnabled)
            log.trace("count=" + count);
        return (count == null) ? 0 : ((Number) count).longValue();
    }

    @Override
    public long count(DetachedCriteria dc) {
        return count(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public long count(Query query, HibernateParameter... parameters) {
        assert query != null;
        Object count = HibernateTool.setParameters(query, parameters)
                                    .setResultTransformer(Criteria.PROJECTION)
                                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                                    .uniqueResult();

        if (isTraceEnabled)
            log.trace("count=" + count);

        return (count == null) ? 0 : ((Number) count).longValue();
    }

    @Override
    public Object merge(Object entity) {
        return getSession().merge(entity);
    }

    @Override
    public void persist(Object entity) {
        getSession().persist(entity);
    }

    @Override
    public Serializable save(Object entity) {
        return getSession().save(entity);
    }

    @Override
    public void saveOrUpdate(Object entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void update(Object entity) {
        getSession().update(entity);
    }

    @Override
    public void delete(Object entity) {
        getSession().delete(entity);
    }

    @Override
    public void deleteById(Serializable id) {
        if (isTraceEnabled)
            log.trace("엔티티[{}]를 삭제합니다. id=[{}]", entityClazz.getSimpleName(), id);

        getSession().delete(load(id));
    }

    @Override
    public void deleteAll() {
        if (isTraceEnabled)
            log.trace("해당 엔티티를 모두 삭제합니다. entityClazz=[{}]", entityClazz);

        deleteAll(DetachedCriteria.forClass(entityClazz));
    }

    @Override
    public void deleteAll(Collection<?> entities) {
        if (isTraceEnabled)
            log.trace("지정한 엔티티들을 모두 삭제합니다.");

        final Session session = getSession();
        for (Object entity : entities) {
            session.delete(entity);
        }
    }

    @Override
    public void deleteAll(DetachedCriteria dc) {
        deleteAll(dc.getExecutableCriteria(getSession()));
    }

    @Override
    public void deleteAll(Criteria criteria) {
        deleteAll(find(criteria));
    }

    @Override
    public int deleteAllWithoutCascade() {
        if (isTraceEnabled)
            log.trace("해당 엔티티를 모두 삭제합니다. entityClazz=[{}]", entityClazz);

        return getSession()
                .createQuery("delete from " + entityClazz.getName())
                .executeUpdate();
    }


    @Override
    public int executeUpdateByHql(final String hql, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("Update/Delete 구문을 수행합니다. hql=[{}], parameters=[{}]",
                      hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateByNamedQuery(final String queryName, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("Update/Delete 구문을 수행합니다. queryName=[{}], parameters=[{}]",
                      queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateBySQLString(final String sqlString, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("Update/Delete 구문을 수행합니다. sqlString=[{}], parameters=[{}]",
                      sqlString, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }

    /**
     * Build projection criteria.
     *
     * @param projectClass   the project class
     * @param criteria       the criteria
     * @param projection     the projection
     * @param distinctResult the distinct result
     * @return the criteria
     */
    protected <TProject> Criteria buildProjectionCriteria(Class<TProject> projectClass,
                                                          Criteria criteria,
                                                          Projection projection,
                                                          boolean distinctResult) {
        if (isTraceEnabled)
            log.trace("Criteria에 Projection을 적용합니다. projectClass=[{}], projection=[{}], distinctResult=[{}]",
                      projectClass, projection, distinctResult);

        if (distinctResult) {
            criteria.setProjection(Projections.distinct(projection));
        } else {
            criteria.setProjection(projection);
        }

        criteria.setResultTransformer(Transformers.aliasToBean(projectClass));
        return criteria;
    }

    @Override
    public <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, DetachedCriteria dc) {
        return reportOne(projectClass, projectionList, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, Criteria criteria) {
        Criteria projectCriteria = buildProjectionCriteria(projectClass, criteria, projectionList, true);
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
        Criteria projectCriteria = buildProjectionCriteria(projectClass, criteria, projectionList, false);
        return projectCriteria.list();
    }

    @Override
    public <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                                ProjectionList projectionList,
                                                Criteria criteria,
                                                int firstResult,
                                                int maxResults) {
        Criteria projectCriteria = buildProjectionCriteria(projectClass, criteria, projectionList, false);
        HibernateTool.setPaging(projectCriteria, firstResult, maxResults);

        return projectCriteria.list();
    }

    @Override
    public <TProject> PaginatedList<TProject> reportPage(Class<TProject> projectClass,
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
    public <TProject> PaginatedList<TProject> reportPage(Class<TProject> projectClass,
                                                         ProjectionList projectionList,
                                                         Criteria criteria,
                                                         int pageNo,
                                                         int pageSize) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, false);

        long itemCount = count(projectCriteria);
        int firstResult = (pageNo - 1) * pageSize;
        HibernateTool.setPaging(projectCriteria, firstResult, pageSize);

        return new PaginatedList<TProject>(projectCriteria.list(), pageNo, pageSize, itemCount);
    }

}
