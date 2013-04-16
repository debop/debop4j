package kr.debop4j.data.mongodb.dao.impl;

import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.FullTextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MongoDB를 저장소로 사용하는 hibernate-ogm Data Access Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오전 11:14
 */
@SuppressWarnings("unchecked")
public class MongoOgmDaoImpl extends HibernateOgmDaoImpl {

    private static final Logger log = LoggerFactory.getLogger(MongoOgmDaoImpl.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    /**
     * 엔티티 수형에 해당하는 모든 엔티티를 조회합니다.
     */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Sort luceneSort) {
        if (log.isTraceEnabled())
            log.trace("엔티티 수형 [{}]의 모든 레코드를 조회합니다.", clazz);

        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);

        if (luceneSort != null)
            ftq.setSort(luceneSort);

        return (List<T>) ftq.list();
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("find... clazz=[{}], luceneQuery=[{}], parameters=[{}]",
                      clazz, luceneQuery, StringTool.listToString(parameters));

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setParameters(ftq, parameters);
        return (List<T>) ftq.list();
    }

    @Override
    public int count(Class<?> clazz) {
        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        return count(clazz, luceneQuery);
    }

    @Override
    public int count(Class<?> clazz, Query luceneQuery) {
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        return ftq.getResultSize();
    }


    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(findAll(clazz));
    }
}
