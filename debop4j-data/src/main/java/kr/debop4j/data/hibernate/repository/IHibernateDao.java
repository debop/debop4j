package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.data.hibernate.HibernateParameter;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * kr.debop4j.data.hibernate.repository.IHibernateDao
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 2:05
 */
public interface IHibernateDao {

    Session getSession();

    void flushSession();

    void transactionalFlush();

    <T> T load(Class clazz, Serializable id);

    <T> T load(Class clazz, Serializable id, LockOptions lockOptions);

    <T> T get(Class<T> clazz, Serializable id);

    <T> T get(Class<T> clazz, Serializable id, LockOptions lockOptions);

    <T> List<T> getIn(Class<T> clazz, Collection ids);

    <T> List<T> getIn(Class<T> clazz, Serializable[] ids);

    /**
     * 모든 엔티티를 필터링 없이 반환합니다.
     */
    <T> List<T> findAll(Class<T> clazz, Order... orders);

    /**
     * 모든 엔티티를 필터링 없이 Paging 처리하여 반환합니다.
     */
    <T> List<T> findAll(Class<T> clazz, int firstResult, int maxResults, Order... orders);

    <T> List<T> find(Class<T> clazz, Criteria criteria, Order... orders);

    <T> List<T> find(Class<T> clazz, Criteria criteria, int firstResult, int maxResults, Order... orders);

    <T> List<T> find(Class<T> clazz, DetachedCriteria dc, Order... orders);

    <T> List<T> find(Class<T> clazz, DetachedCriteria dc, int firstResult, int maxResults, Order... orders);

    <T> List<T> find(Class<T> clazz, Query query, HibernateParameter... parameters);

    <T> List<T> find(Class<T> clazz, Query query, int firstResult, int maxResults, HibernateParameter... parameters);

    <T> List<T> find(Class<T> clazz, String hql, HibernateParameter... parameters);

    <T> List<T> find(Class<T> clazz, String hql, int firstResult, int maxResults, HibernateParameter... parameters);

    <T> List<T> findByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters);

    <T> List<T> findByNamedQuery(Class<T> clazz, String queryName, int firstResult, int maxResults, HibernateParameter... parameters);

    <T> List<T> findBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters);

    <T> List<T> findBySQLString(Class<T> clazz, String sqlString, int firstResult, int maxResults, HibernateParameter... parameters);

    <T> List<T> findByExample(Class<T> clazz, Example example);

    <T> IPagedList<T> getPage(Class<T> clazz, Criteria criteria, int pageNo, int pageSize, Order... orders);

    <T> IPagedList<T> getPage(Class<T> clazz, DetachedCriteria dc, int pageNo, int pageSize, Order... orders);

    <T> IPagedList<T> getPage(Class<T> clazz, Query query, int pageNo, int pageSize, HibernateParameter... parameters);

    <T> IPagedList<T> getPageByHql(Class<T> clazz, String hql, int pageNo, int pageSize, HibernateParameter... parameters);

    <T> IPagedList<T> getPageByNamedQuery(Class<T> clazz, String queryName, int pageNo, int pageSize, HibernateParameter... parameters);

    <T> IPagedList<T> getPageBySQLString(Class<T> clazz, String sqlString, int pageNo, int pageSize, HibernateParameter... parameters);

    <T> T findUnique(Class<T> clazz, DetachedCriteria dc);

    <T> T findUnique(Class<T> clazz, Criteria criteria);

    <T> T findUnique(Class<T> clazz, Query query, HibernateParameter... parameters);

    <T> T findUniqueByHql(Class<T> clazz, String hql, HibernateParameter... parameters);

    <T> T findUniqueByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters);

    <T> T findUniqueBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters);

    <T> T findFirst(Class<T> clazz, DetachedCriteria dc, Order... orders);

    <T> T findFirst(Class<T> clazz, Criteria criteria, Order... orders);

    <T> T findFirst(Class<T> clazz, Query query, HibernateParameter... parameters);

    <T> T findFirstByHql(Class<T> clazz, String hql, HibernateParameter... parameters);

    <T> T findFirstByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters);

    <T> T findFirstBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters);

    <T> boolean exists(Class<T> clazz);

    <T> boolean exists(Class<T> clazz, DetachedCriteria dc);

    <T> boolean exists(Class<T> clazz, Criteria criteria);

    <T> boolean exists(Class<T> clazz, Query query, HibernateParameter... parameters);

    <T> boolean existsByHql(Class<T> clazz, String hql, HibernateParameter... parameters);

    <T> boolean existsByNamedQuery(Class<T> clazz, String queryName, HibernateParameter... parameters);

    <T> boolean existsBySQLString(Class<T> clazz, String sqlString, HibernateParameter... parameters);

    <T> long count(Class<T> clazz, Criteria criteria);

    <T> Long count(Class<T> clazz, DetachedCriteria dc);

    <T> Long count(Class<T> clazz, Query query, HibernateParameter... parameters);

    <T> Object merge(T entity);

    <T> void persist(T entity);

    <T> Serializable save(T entity);

    <T> void saveOrUpdate(T entity);

    <T> void update(T entity);

    <T> void delete(T entity);

    <T> void deleteById(Class<T> clazz, Serializable id);

    <T> void deleteAll(Class<T> clazz);

    <T> void deleteAll(Collection<T> entities);

    <T> void deleteAll(Class<T> clazz, DetachedCriteria dc);

    <T> void deleteAll(Class<T> clazz, Criteria criteria);

    /**
     * Cascade 적용 없이 엔티티들을 모두 삭제합니다.
     */
    <T> int deleteAllWithoutCascade(Class<T> clazz);

    int executeUpdateByHql(String hql, HibernateParameter... parameters);

    int executeUpdateByNamedQuery(String queryName, HibernateParameter... parameters);

    int executeUpdateBySQLString(String sqlString, HibernateParameter... parameters);

    <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, DetachedCriteria dc);

    <TProject> TProject reportOne(Class<TProject> projectClass, ProjectionList projectionList, Criteria criteria);

    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         DetachedCriteria dc);

    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         DetachedCriteria dc,
                                         int firstResult,
                                         int maxResults);

    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         Criteria criteria);

    <TProject> List<TProject> reportList(Class<TProject> projectClass,
                                         ProjectionList projectionList,
                                         Criteria criteria,
                                         int firstResult,
                                         int maxResults);

    <T, TProject> IPagedList<TProject> reportPage(Class<T> clazz,
                                                  Class<TProject> projectClass,
                                                  ProjectionList projectionList,
                                                  DetachedCriteria dc,
                                                  int pageNo,
                                                  int pageSize);

    <T, TProject> IPagedList<TProject> reportPage(Class<T> clazz,
                                                  Class<TProject> projectClass,
                                                  ProjectionList projectionList,
                                                  Criteria criteria,
                                                  int pageNo,
                                                  int pageSize);
}
