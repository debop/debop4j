package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.model.IStatefulEntity;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Hibernate 용 Data Access Object Interface 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 27.
 */
public interface IHibernateRepository<E extends IStatefulEntity> {

    Class<E> getEntityClass();

    void flush();

    E load(Serializable id);

    E load(Serializable id, LockOptions lockOptions);

    E get(Serializable id);

    E get(Serializable id, LockOptions lockOptions);

    List<E> getIn(Collection ids);

    List<E> getIn(Serializable[] ids);

    List<E> getAll();

    List<E> find(DetachedCriteria dc);

    List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders);

    List<E> findByExample(Example example);

    List<E> find(Query query, HibernateParameter... parameters);

    List<E> find(Query query, int firstResult, int maxResults, HibernateParameter... parameters);

    List<E> findByHql(String hql, HibernateParameter... parameters);

    List<E> findByHql(String hql, int firstResult, int maxResults, HibernateParameter... parameters);

    List<E> findByNamedQuery(String queryName, HibernateParameter... parameters);

    List<E> findByNamedQuery(String queryName, int firstResult, int maxResults, HibernateParameter... parameters);

    List<E> findBySQLString(String sqlString, HibernateParameter... parameters);

    List<E> findBySQLString(String sqlString, int firstResult, int maxResults, HibernateParameter... parameters);

    IPagedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders);

    IPagedList<E> getPage(Query query, int pageNo, int pageSize, HibernateParameter... parameters);

    IPagedList<E> getPageByHql(String hql, int pageNo, int pageSize, HibernateParameter... parameters);

    IPagedList<E> getPageByNamedQuery(String queryName, int pageNo, int pageSize, HibernateParameter... parameters);

    IPagedList<E> getPageBySQLString(String sqlString, int pageNo, int pageSize, HibernateParameter... parameters);

    E findOne(DetachedCriteria dc);

    E findOne(Criteria criteria);

    E findOne(Query query, HibernateParameter... parameters);

    E findOneByHql(String hql, HibernateParameter... parameters);

    E findOneByNamedQuery(String queryName, HibernateParameter... parameters);

    E findOneBySQLString(String sqlString, HibernateParameter... parameters);

    E findFirst(DetachedCriteria dc);

    E findFirst(Criteria criteria);

    E findFirst(Query query, HibernateParameter... parameters);

    E findFirstByHql(String hql, HibernateParameter... parameters);

    E findFirstByNamedQuery(String queryName, HibernateParameter... parameters);

    E findFirstBySQLString(String sqlString, HibernateParameter... parameters);

    boolean exists();

    boolean exists(DetachedCriteria dc);

    boolean exists(Criteria criteria);

    boolean exists(Query query, HibernateParameter... parameters);

    boolean existsByHql(String hql, HibernateParameter... parameters);

    boolean existsByNamedQuery(String queryName, HibernateParameter... parameters);

    boolean existsBySQLString(String sqlString, HibernateParameter... parameters);

    long count();

    long count(DetachedCriteria dc);

    long count(Criteria criteria);


    E merge(E entity);

    void persist(E entity);

    Serializable save(E entity);

    void saveOrUpdate(E entity);

    void update(E entity);

    void delete(E entity);

    void deleteEntities(Collection<E> entities);

    void delete(DetachedCriteria dc);

    void deleteById(Serializable id);

    int deleteAllWithoutCascade();

    int executeUpdateByHql(String hql, HibernateParameter... parameters);

    int executeUpdateByNamedQuery(String queryName, HibernateParameter... parameters);


    <TProject> TProject reportOne(Class<TProject> projectClass,
                                  ProjectionList projectionList,
                                  DetachedCriteria dc);

    <TProject> TProject reportOne(Class<TProject> projectClass,
                                  ProjectionList projectionList,
                                  Criteria criteria);

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

    <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                               ProjectionList projectionList,
                                               DetachedCriteria dc,
                                               int pageNo,
                                               int pageSize);

    <TProject> IPagedList<TProject> reportPage(Class<TProject> projectClass,
                                               ProjectionList projectionList,
                                               Criteria criteria,
                                               int pageNo,
                                               int pageSize);

}
