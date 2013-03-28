package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.collection.IPagedList;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.model.IStatefulEntity;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.*;

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

    List<E> getIn(Object[] ids);

    List<E> getAll();

    List<E> find(DetachedCriteria dc);

    List<E> find(DetachedCriteria dc, int firstResult, int maxResults, Order... orders);

    List<E> findByCriteria(Criterion... criterions);

    List<E> findByCriteria(Criterion[] criterions, int firstResult, int maxResults, Order... orders);

    List<E> findByExample(Example example);

    List<E> findByQuery(Query query, HibernateParameter... parameters);

    List<E> findByQuery(Query query, int firstResult, int maxResults, HibernateParameter... parameters);

    List<E> findByQueryString(String queryString, HibernateParameter... parameters);

    List<E> findByQueryString(String queryString, int firstResult, int maxResults, HibernateParameter... parameters);

    List<E> findByNamedQuery(String queryName, HibernateParameter... parameters);

    List<E> findByNamedQuery(String queryName, int firstResult, int maxResults, HibernateParameter... parameters);


    IPagedList<E> getPage(DetachedCriteria dc, int pageNo, int pageSize, Order... orders);

    IPagedList<E> getPageByQuery(Query query, int pageNo, int pageSize, HibernateParameter... parameters);

    IPagedList<E> getPageByQueryString(String queryString, int pageNo, int pageSize, HibernateParameter... parameters);

    IPagedList<E> getPageByNamedQuery(String queryName, int pageNo, int pageSize, HibernateParameter... parameters);


    E findOne(DetachedCriteria dc);

    E findOneByCriteria(Criterion... criterions);

    E findOneByQuery(Query query, HibernateParameter... parameters);

    E findOneByQueryString(String queryString, HibernateParameter... parameters);

    E findOneByNamedQuery(String queryName, HibernateParameter... parameters);


    E findFirst(DetachedCriteria dc);

    E findFirstByCriteria(Criterion... criterions);

    E findFirstByQuery(Query query, HibernateParameter... parameters);

    E findFirstByQueryString(String queryString, HibernateParameter... parameters);

    E findFirstByNamedQuery(String queryName, HibernateParameter... parameters);

    boolean exists();

    boolean exists(DetachedCriteria dc);

    boolean existsByCriteria(Criterion... criterions);

    boolean existsByQuery(Query query, HibernateParameter... parameters);

    boolean existsByQueryString(String queryString, HibernateParameter... parameters);

    boolean existsByNamedQuery(String queryName, HibernateParameter... parameters);

    long count();

    long count(DetachedCriteria dc);

    long countByCriteria(Criterion... criterions);


    void merge(E entity);

    void persist(E entity);

    void save(E entity);

    void saveOrUpdate(E entity);

    void update(E entity);

    void delete(E entity);

    void deleteEntities(Collection<E> entities);

    void delete(DetachedCriteria dc);

    void deleteById(Serializable id);

    int deleteAllWithoutCascade();

    int executeUpdateByQueryString(String queryString, HibernateParameter... parameters);

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
