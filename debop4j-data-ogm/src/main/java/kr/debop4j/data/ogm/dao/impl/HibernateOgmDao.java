package kr.debop4j.data.ogm.dao.impl;

import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.ogm.dao.IHibernateOgmDao;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * hibernate-ogm 용 DAO 입니다.<br />
 * hibernate의 Criteria 기능을 제공할 수 없어, Criteria 부분은 hibernate-search를 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 5:42
 */
@SuppressWarnings("unchecked")
public class HibernateOgmDao implements IHibernateOgmDao {

    private static final Logger log = LoggerFactory.getLogger(HibernateOgmDao.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private static final String FULL_TEXT_SESSION_KEY = HibernateOgmDao.class.getName() + ".FullTextSession";

    public Session getSession() {
        return UnitOfWorks.getCurrentSession();
    }

    public synchronized FullTextSession getFullTextSession() {
        FullTextSession fullTextSession = (FullTextSession) Local.get(FULL_TEXT_SESSION_KEY);
        if (fullTextSession == null) {
            fullTextSession = Search.getFullTextSession(getSession());
            Local.put(FULL_TEXT_SESSION_KEY, fullTextSession);
        }
        return fullTextSession;
    }

    public QueryBuilder getQueryBuilder(Class<?> clazz) {
        return getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return findAll(clazz, null);
    }

    public <T> List<T> findAll(Class<T> clazz, org.apache.lucene.search.Sort sort) {
        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(luceneQuery, clazz);

        // 필수!!! object lookup 및 DB 조회 방법 설정
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID);

        if (sort != null)
            fullTextQuery.setSort(sort);

        return (List<T>) fullTextQuery.list();
    }

    public <T> T load(Class<T> clazz, Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    public <T> T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    public void persist(Object entity) {
        getSession().persist(entity);
    }

    public Object merge(Object entity) {
        return getSession().merge(entity);
    }

    public Serializable save(Object entity) {
        return getSession().save(entity);
    }

    public void saveOrUpdate(Object entity) {
        getSession().saveOrUpdate(entity);
    }

    public void update(Object entity) {
        getSession().update(entity);
    }

    public void delete(Object entity) {
        getSession().delete(entity);
    }


    public <T> void deleteAll(Collection<T> entities) {
        Session session = getSession();
        for (T entity : entities) {
            session.delete(entity);
        }
    }
}
