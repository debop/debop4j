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
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Hibernate Dao 기본 클래스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 15. 오전 10:21
 */
@Repository("hibernateDao")
@Transactional
@SuppressWarnings("unchecked")
public class HibernateDao implements IHibernateDao {
    private static final Logger log = LoggerFactory.getLogger(HibernateDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Autowired
    SessionFactory sessionFactory;

    private boolean cacheable;

    /**
     * Instantiates a new Hibernate dao.
     */
    public HibernateDao() {
        this(false);
    }

    /**
     * Instantiates a new Hibernate dao.
     *
     * @param cacheable the cacheable
     */
    public HibernateDao(boolean cacheable) {
        this.cacheable = cacheable;
    }

    @Override
    public Session getSession() {
        // return UnitOfWorks.getCurrentSession();
        return sessionFactory.getCurrentSession();
    }

    @Override
    public final void flushSession() {

        log.debug("Session 정보를 flush 합니다...");

        getSession().flush();
    }

    @Override
    public final void transactionalFlush() {
        UnitOfWorks.getCurrent().transactionalFlush();
    }

    @Override
    public <T> T load(Class clazz, Serializable id) {

        log.trace("load entity... clazz=[{}], id=[{}]", clazz, id);

        return (T) getSession().load(clazz, id);
    }

    @Override
    public <T> T load(Class clazz, Serializable id, LockOptions lockOptions) {

        log.trace("load entity... clazz=[{}], id=[{}], lockOptions=[{}]", clazz, id, lockOptions);

        return (T) getSession().load(clazz, id, lockOptions);
    }

    @Override
    public <T> T get(Class<T> clazz, Serializable id) {

        log.trace("get entity... clazz=[{}], id=[{}]", clazz, id);

        return (T) getSession().get(clazz, id);
    }

    @Override
    public <T> T get(Class<T> clazz, Serializable id, LockOptions lockOptions) {

        log.trace("get entity... clazz=[{}], id=[{}], lockOptions=[{}]", clazz, id, lockOptions);

        return (T) getSession().get(clazz, id, lockOptions);
    }

    @Override
    public <T> List<T> getIn(Class<T> clazz, Collection<? extends Serializable> ids) {
        if (ArrayTool.isEmpty(ids))
            return Lists.newArrayList();

        DetachedCriteria dc = CriteriaTool.addIn(DetachedCriteria.forClass(clazz), "id", ids);
        return find(clazz, dc);
    }

    @Override
    public <T> List<T> getIn(Class<T> clazz, Serializable[] ids) {
        if (ArrayTool.isEmpty(ids))
            return Lists.newArrayList();

        DetachedCriteria dc = CriteriaTool.addIn(DetachedCriteria.forClass(clazz), "id", ids);
        return find(clazz, dc);
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
    @Transactional(readOnly = true)
    public final <T> List<T> findAll(Class<T> clazz, Order... orders) {
        if (ArrayTool.isEmpty(orders)) {
            Query query = getSession().createQuery("from " + clazz.getName());
            return (List<T>) query.setCacheable(cacheable).list();
        } else {
            Criteria criteria = getSession().createCriteria(clazz);
            HibernateTool.addOrders(criteria, orders);
            return criteria.setCacheable(cacheable).list();
        }
    }

    @Override
    public final <T> List<T> findAll(Class<T> clazz, int firstResult, int maxResults, Order... orders) {
        if (ArrayTool.isEmpty(orders)) {
            Query query = getSession().createQuery("from " + clazz.getName());
            HibernateTool.setPaging(query, firstResult, maxResults);

            return (List<T>) query.setCacheable(cacheable).list();
        } else {
            Criteria criteria = getSession().createCriteria(clazz);
            HibernateTool.setPaging(criteria, firstResult, maxResults);
            if (!ArrayTool.isEmpty(orders))
                HibernateTool.addOrders(criteria, orders);

            return criteria.setCacheable(cacheable).list();
        }
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, Criteria criteria, Order... orders) {
        if (!ArrayTool.isEmpty(orders))
            HibernateTool.addOrders(criteria, orders);

        return criteria.setCacheable(cacheable).list();
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, Criteria criteria, int firstResult, int maxResults, Order... orders) {
        HibernateTool.setPaging(criteria, firstResult, maxResults);
        if (!ArrayTool.isEmpty(orders))
            HibernateTool.addOrders(criteria, orders);

        return criteria.setCacheable(cacheable).list();
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, DetachedCriteria dc, Order... orders) {
        return find(clazz, dc.getExecutableCriteria(getSession()), orders);
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, DetachedCriteria dc, int firstResult, int maxResults, Order... orders) {
        return find(clazz, dc.getExecutableCriteria(getSession()), firstResult, maxResults, orders);
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, Query query, HibernateParameter... parameters) {
        return find(clazz, query, -1, -1, parameters);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query query, int firstResult, int maxResults, HibernateParameter... parameters) {
        shouldNotBeNull(query, "query");
        HibernateTool.setPaging(query, firstResult, maxResults);
        HibernateTool.setParameters(query, parameters);

        return (List<T>) query.list();
    }

    @Override
    public final <T> List<T> find(Class<T> clazz, String hql, HibernateParameter... parameters) {
        return find(clazz, hql, -1, -1, parameters);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String hql, int firstResult, int maxResults, HibernateParameter... parameters) {

        log.trace("HQL문을 실행합니다. clazz=[{}], hql=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                  clazz, hql, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return find(clazz, query, firstResult, maxResults, parameters);
    }

    @Override
    public final <T> List<T> findByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters) {
        return findByNamedQuery(clazz, queryName, -1, -1, parameters);
    }

    @Override
    public <T> List<T> findByNamedQuery(Class<T> clazz, String queryName, int firstResult, int maxResults, HibernateParameter... parameters) {

        log.trace("NamedQuery를 실행합니다. clazz=[{}], sqlString=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                  clazz, queryName, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return find(clazz, query, firstResult, maxResults, parameters);
    }

    @Override
    public final <T> List<T> findBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters) {
        return findBySQLString(clazz, sqlString, -1, -1, parameters);
    }

    @Override
    public <T> List<T> findBySQLString(Class<T> clazz, String sqlString, int firstResult, int maxResults, HibernateParameter... parameters) {

        log.trace("일반 SQL 문 실행합니다. clazz=[{}], sqlString=[{}], firstResult=[{}], maxResults=[{}], parameters=[{}]",
                  clazz, sqlString, firstResult, maxResults, StringTool.listToString(parameters));

        Query query = getSession().createSQLQuery(sqlString);
        return find(clazz, query, firstResult, maxResults, parameters);
    }

    @Override
    public <T> List<T> findByExample(Class<T> clazz, Example example) {
        return getSession().createCriteria(clazz).add(example).list();
    }

    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, Criteria criteria, int pageNo, int pageSize, Order... orders) {
        Criteria countCriteria = HibernateTool.copyCriteria(criteria);
        long itemCount = count(clazz, countCriteria);

        int firstResult = (pageNo - 1) * pageSize;
        List<T> list = find(clazz, criteria, firstResult, pageSize, orders);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, DetachedCriteria dc, int pageNo, int pageSize, Order... orders) {
        DetachedCriteria countDc = HibernateTool.copyDetachedCriteria(dc);
        long itemCount = count(clazz, countDc);

        int firstResult = (pageNo - 1) * pageSize;
        List<T> list = find(clazz, dc, firstResult, pageSize, orders);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <T> PaginatedList<T> getPage(Class<T> clazz, Query query, int pageNo, int pageSize, HibernateParameter... parameters) {
        Query countQuery = getSession().createQuery(query.getQueryString());
        long itemCount = count(clazz, countQuery, parameters);

        int firstResult = (pageNo - 1) * pageSize;
        List<T> list = find(clazz, query, firstResult, pageSize, parameters);
        return new PaginatedList(list, pageNo, pageSize, itemCount);
    }

    @Override
    public <T> IPagedList<T> getPageByHql(Class<T> clazz, String hql, int pageNo, int pageSize, HibernateParameter... parameters) {

        log.trace("HQL문을 실행하고, 결과를 Paging처리합니다. clazz=[{}], hql=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                  clazz, hql, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return getPage(clazz, query, pageNo, pageSize, parameters);
    }

    @Override
    public <T> IPagedList<T> getPageByNamedQuery(Class<T> clazz, String queryName, int pageNo, int pageSize, HibernateParameter... parameters) {

        log.trace("NamedQuery를 실행하고, 결과를 Paging처리합니다. clazz=[{}], sqlString=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                  clazz, queryName, pageNo, pageSize, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return getPage(clazz, query, pageNo, pageSize, parameters);
    }

    @Override
    public <T> IPagedList<T> getPageBySQLString(Class<T> clazz, String sqlString, int pageNo, int pageSize, HibernateParameter... parameters) {

        log.trace("일반 SQL문을 실행하고, 결과를 Paging처리합니다. clazz=[{}], sqlString=[{}], pageNo=[{}], pageSize=[{}], parameters=[{}]",
                  clazz, sqlString, pageNo, pageSize, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return getPage(clazz, query, pageNo, pageSize, parameters);
    }


    @Override
    public <T> T findUnique(Class<T> clazz, DetachedCriteria dc) {
        return findUnique(clazz, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public <T> T findUnique(Class<T> clazz, Criteria criteria) {
        return (T) criteria.setCacheable(cacheable).uniqueResult();
    }

    @Override
    public <T> T findUnique(Class<T> clazz, Query query, HibernateParameter... parameters) {
        HibernateTool.setParameters(query, parameters);
        return (T) query.setCacheable(cacheable).uniqueResult();
    }

    @Override
    public <T> T findUniqueByHql(Class<T> clazz, String hql, HibernateParameter... parameters) {

        log.trace("hql을 수행합니다. clazz=[{}], hql=[{}], parameters=[{}]",
                  clazz, hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findUnique(clazz, query, parameters);
    }

    @Override
    public <T> T findUniqueByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters) {

        log.trace("NamedQuery를 수행합니다. clazz=[{}], queryName=[{}], parameters=[{}]",
                  clazz, queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findUnique(clazz, query, parameters);
    }

    @Override
    public <T> T findUniqueBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters) {

        log.trace("일반 SQL문을 수행합니다. clazz=[{}], sqlString=[{}], parameters=[{}]",
                  clazz, sqlString, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return findUnique(clazz, query, parameters);
    }

    @Override
    public <T> T findFirst(Class<T> clazz, DetachedCriteria dc, Order... orders) {
        return findFirst(clazz, dc.getExecutableCriteria(getSession()), orders);
    }

    @Override
    public <T> T findFirst(Class<T> clazz, Criteria criteria, Order... orders) {
        List<T> list = find(clazz, criteria, 0, 1, orders);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public <T> T findFirst(Class<T> clazz, Query query, HibernateParameter... parameters) {
        HibernateTool.setParameters(query, parameters);
        List<T> list = find(clazz, query, 0, 1, parameters);
        if (list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public <T> T findFirstByHql(Class<T> clazz, String hql, HibernateParameter... parameters) {

        log.trace("hql을 수행합니다. clazz=[{}], hql=[{}], parameters=[{}]",
                  clazz, hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        return findFirst(clazz, query, parameters);
    }

    @Override
    public <T> T findFirstByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters) {

        log.trace("NamedQuery를 수행합니다. clazz=[{}], queryName=[{}], parameters=[{}]",
                  clazz, queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        return findUnique(clazz, query, parameters);
    }

    @Override
    public <T> T findFirstBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters) {

        log.trace("일반 SQL문을 수행합니다. clazz=[{}], sqlString=[{}], parameters=[{}]",
                  clazz, sqlString, StringTool.listToString(parameters));

        SQLQuery query = getSession().createSQLQuery(sqlString);
        return findUnique(clazz, query, parameters);
    }

    @Override
    public boolean exists(Class<?> clazz) {
        return exists(clazz, getSession().createCriteria(clazz));
    }

    @Override
    public boolean exists(Class<?> clazz, DetachedCriteria dc) {
        return exists(clazz, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public boolean exists(Class<?> clazz, Criteria criteria) {
        return findFirst(clazz, criteria) != null;
    }

    @Override
    public boolean exists(Class<?> clazz, Query query, HibernateParameter... parameters) {
        return findFirst(clazz, query, parameters) != null;
    }

    @Override
    public boolean existsByHql(Class<?> clazz, String hql, HibernateParameter... parameters) {
        return findFirstByHql(clazz, hql, parameters) != null;
    }

    @Override
    public boolean existsByNamedQuery(Class<?> clazz, String queryName, HibernateParameter... parameters) {
        return findFirstByNamedQuery(clazz, queryName, parameters) != null;
    }

    @Override
    public boolean existsBySQLString(Class<?> clazz, String sqlString, HibernateParameter... parameters) {
        return findFirstBySQLString(clazz, sqlString, parameters) != null;
    }

    @Override
    public long count(Class<?> clazz) {
        return count(clazz, getSession().createCriteria(clazz));
    }

    @Override
    public long count(Class<?> clazz, Criteria criteria) {
        Object count = criteria.setProjection(Projections.rowCount()).uniqueResult();

        log.trace("count=" + count);
        return (count == null) ? 0 : ((Number) count).longValue();
    }

    @Override
    public long count(Class<?> clazz, DetachedCriteria dc) {
        return count(clazz, dc.getExecutableCriteria(getSession()));
    }

    @Override
    public long count(Class<?> clazz, Query query, HibernateParameter... parameters) {
        assert query != null;
        Object count = HibernateTool.setParameters(query, parameters)
                .setResultTransformer(Criteria.PROJECTION)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .uniqueResult();


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
    public void deleteById(Class<?> clazz, Serializable id) {

        log.trace("엔티티[{}]를 삭제합니다. id=[{}]", clazz, id);

        getSession().delete(load(clazz, id));
    }

    @Override
    public void deleteAll(Class<?> clazz) {

        log.trace("해당 엔티티를 모두 삭제합니다. clazz=[{}]", clazz);

        deleteAll(clazz, DetachedCriteria.forClass(clazz));
    }

    @Override
    public void deleteAll(Collection<?> entities) {

        log.trace("엔티티들을 모두 삭제합니다. entities=[{}]", StringTool.listToString(entities));

        final Session session = getSession();
        for (Object entity : entities) {
            session.delete(entity);
        }
    }

    @Override
    public void deleteAll(Class<?> clazz, DetachedCriteria dc) {
        deleteAll(find(clazz, dc.getExecutableCriteria(getSession())));
    }

    @Override
    public void deleteAll(Class<?> clazz, Criteria criteria) {
        deleteAll(find(clazz, criteria));
    }

    @Override
    public int deleteAllWithoutCascade(Class<?> clazz) {

        log.trace("해당 엔티티를 모두 삭제합니다. clazz=[{}]", clazz);

        return getSession()
                .createQuery("delete from " + clazz.getName())
                .executeUpdate();
    }


    @Override
    public int executeUpdateByHql(String hql, HibernateParameter... parameters) {

        log.trace("Update/Delete 구문을 수행합니다. hql=[{}], parameters=[{}]",
                  hql, StringTool.listToString(parameters));

        Query query = getSession().createQuery(hql);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateByNamedQuery(String queryName, HibernateParameter... parameters) {

        log.trace("Update/Delete 구문을 수행합니다. queryName=[{}], parameters=[{}]",
                  queryName, StringTool.listToString(parameters));

        Query query = getSession().getNamedQuery(queryName);
        HibernateTool.setParameters(query, parameters);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateBySQLString(String sqlString, HibernateParameter... parameters) {

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
    public <T, TProject> PaginatedList<TProject> reportPage(Class<T> clazz,
                                                            Class<TProject> projectClass,
                                                            ProjectionList projectionList,
                                                            DetachedCriteria dc,
                                                            int pageNo,
                                                            int pageSize) {
        return reportPage(clazz,
                          projectClass,
                          projectionList,
                          dc.getExecutableCriteria(getSession()),
                          pageNo,
                          pageSize);
    }

    @Override
    public <T, TProject> PaginatedList<TProject> reportPage(Class<T> clazz,
                                                            Class<TProject> projectClass,
                                                            ProjectionList projectionList,
                                                            Criteria criteria,
                                                            int pageNo,
                                                            int pageSize) {
        Criteria projectCriteria =
                buildProjectionCriteria(projectClass, criteria, projectionList, false);

        long itemCount = count(clazz, projectCriteria);
        int firstResult = (pageNo - 1) * pageSize;
        HibernateTool.setPaging(projectCriteria, firstResult, pageSize);

        return new PaginatedList<TProject>(projectCriteria.list(), pageNo, pageSize, itemCount);
    }
}
