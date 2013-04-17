package kr.debop4j.data.mongodb.dao.impl;

import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.Session;
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
     * TODO: MongoTemplate 을 이용하여, Data를 작업하고, 인덱스는 따로 처리하려고 구현해 봤지만 실퍠했다.
     * 위의 시도 자체가 hibernate 의 의도에 벗어난다.
     * 그래도 deleteAll 같은 경우는 시도해 볼만 하다!!! MongoTemplate 에 대한 공부를 더 한 후 시도해 봐야 겠다.
     */
//    @Autowired
//    @Getter
//    MongoTemplate mongoTemplate;
    public MongoOgmDaoImpl() {}

    public MongoOgmDaoImpl(Session session) {
        super(session);
    }

    /**
     * 엔티티 수형에 해당하는 모든 엔티티를 조회합니다.
     */
    @Override
    public <T> List<T> findAll(Class<T> clazz, Sort luceneSort) {
        if (isTraceEnabled)
            log.trace("엔티티 수형 [{}]의 모든 레코드를 조회합니다...", clazz);

        Query luceneQuery = getQueryBuilder(clazz).all().createQuery();
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);

        if (luceneSort != null)
            ftq.setSort(luceneSort);

        return (List<T>) ftq.list();
    }

    @Override
    public <T> List<T> find(Class<T> clazz, Query luceneQuery, HibernateParameter... parameters) {
        if (isTraceEnabled)
            log.trace("루씬 조회를 수행합니다... clazz=[{}], luceneQuery=[{}], parameters=[{}]",
                      clazz, luceneQuery, StringTool.listToString(parameters));

        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        HibernateTool.setParameters(ftq, parameters);

        return (List<T>) ftq.list();
    }

    @Override
    public long count(Class<?> clazz) {
        return count(clazz, getQueryBuilder(clazz).all().createQuery());
    }

    @Override
    public long count(Class<?> clazz, Query luceneQuery) {
        if (isTraceEnabled)
            log.trace("수형[{}]에 대해 갯수를 구합니다. luceneQuery=[{}]", clazz, luceneQuery);
        FullTextQuery ftq = getFullTextQuery(luceneQuery, clazz);
        long count = ftq.getResultSize();
        if (isTraceEnabled)
            log.trace("수형[{}]의 질의[{}]에 대한 갯수=[{}]", clazz, luceneQuery, count);
        return count;
    }

    /**
     * 지정된 수형의 엔티티를 모두 삭제합니다.
     */
    @Override
    public void deleteAll(Class<?> clazz) {
        deleteAll(findAll(clazz));
    }
}
