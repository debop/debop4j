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

package kr.debop4j.search.tools;

import com.google.common.collect.Sets;
import kr.debop4j.core.Action1;
import kr.debop4j.core.Guard;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.search.QueryMethod;
import kr.debop4j.search.SearchParameter;
import kr.debop4j.search.SearchRangeParameter;
import kr.debop4j.search.dao.HibernateSearchDao;
import kr.debop4j.search.dao.IHibernateSearchDao;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.event.impl.FullTextIndexEventListener;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * Hibernate Search 관련 Utility class s
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 28.
 */
public class SearchTool {

    private static final Logger log = LoggerFactory.getLogger(SearchTool.class);

    private SearchTool() {}

    /**
     * Hibernate-Search의 FullTextIndexEventListener를 SessionFactory에 등록합니다.
     *
     * @param sessionFactory the session factory
     * @param listener       the listener
     */
    public static void registerFullTextIndexEventListener(SessionFactory sessionFactory, FullTextIndexEventListener listener) {
        assert sessionFactory != null;
        log.info("sessionFactory에 FullTestIndexEventListener를 등록합니다... listener=[{}]", listener);

        try {
            HibernateTool.registerEventListener(sessionFactory, listener,
                                                EventType.POST_UPDATE,
                                                EventType.POST_INSERT,
                                                EventType.POST_DELETE,
                                                EventType.FLUSH);
        } catch (Throwable t) {
            log.warn("listener를 등록하는데 실패했습니다. 단 이미 등록된 경우에는 무시해도 됩니다.", t);
        }
    }

    /**
     * 루씬용 Query를 빌드합니다.
     *
     * @param fts       the fts
     * @param clazz     the clazz
     * @param fieldName the field name
     * @param values    the values
     * @return the org . apache . lucene . search . query
     */
    public static Query bulidLuceneQuery(FullTextSession fts, Class<?> clazz, String fieldName, String values) {

            log.trace("루씬 쿼리를 빌드합니다. clazz=[{}], fieldName=[{}], values=[{}]", clazz, fieldName, values);

        Analyzer analyzer;
        if (clazz == null) {
            analyzer = new SimpleAnalyzer(Version.LUCENE_36);
        } else {
            analyzer = fts.getSearchFactory().getAnalyzer(clazz);
        }

        QueryParser parser = new QueryParser(Version.LUCENE_36, fieldName, analyzer);
        Query luceneQuery = null;
        try {
            luceneQuery = parser.parse(values);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to parse search entry into a Lucene query", e);
        }
        return luceneQuery;
    }

    /**
     * FullTextQuery 인스턴스를 생성합니다.
     *
     * @param fts         FullTextSession instance.
     * @param luceneQuery the lucene query
     * @param clazz       the clazz
     * @return the full text query
     */
    public static FullTextQuery createFullTextQuery(FullTextSession fts, Query luceneQuery, Class<?> clazz) {

        FullTextQuery ftq = fts.createFullTextQuery(luceneQuery, clazz);

        // 기본값이 SKIP, QUERY 입니다.
        // ftq.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.QUERY);
        return ftq;
    }

    /**
     * 인덱싱된 엔티티의 수형을 반환합니다.
     *
     * @param sessionFactory the session factory
     * @return 인덱싱된 엔티티의 수형들
     */
    public static Set<Class> getIndexedClasses(SessionFactory sessionFactory) {
        if (log.isDebugEnabled())
            log.debug("매핑된 엔티티중에 인덱싱을 수행할 엔티티들을 조회합니다.");

        final Set<Class> classes = Sets.newHashSet();
        Collection<ClassMetadata> metadatas = sessionFactory.getAllClassMetadata().values();

        for (ClassMetadata meta : metadatas) {
            Class clazz = meta.getMappedClass();
            if (clazz.getAnnotation(Indexed.class) != null) {
                classes.add(clazz);

                    log.trace("인덱싱된 엔티티=[{}]", clazz);
            }
        }
        return classes;
    }

    /**
     * 모든 엔티티의 인덱스를 재구성합니다.
     * see {@link SearchTool#getIndexedClasses(org.hibernate.SessionFactory)}
     *
     * @param sessionFactory SessionFactory 인스턴스
     * @param classes        인덱싱을 수행할 대상 엔티티의 수형
     * @param clear          기존 인덱스를 삭제할 것인가 여부
     */
    public static void indexAll(final SessionFactory sessionFactory, final Set<Class> classes, final boolean clear) {
        if (log.isDebugEnabled())
            log.debug("모든 엔티티에 대해 전체 인덱싱을 수행합니다. classes=[{}], clear=[{}]", StringTool.listToString(classes), clear);

        final IHibernateSearchDao searchDao = new HibernateSearchDao();
        for (Class clazz : classes) {
            if (clear)
                searchDao.clearIndex(clazz);
            searchDao.indexAll(clazz, 100);
        }
    }

    /**
     * 모든 엔티티의 인덱스를 병렬 방식으로 재구성합니다.
     * see {@link SearchTool#getIndexedClasses(org.hibernate.SessionFactory)}
     *
     * @param sessionFactory SessionFactory 인스턴스
     * @param classes        인덱싱을 수행할 대상 엔티티의 수형
     * @param clear          기존 인덱스를 삭제할 것인가 여부
     */
    public static void indexAllAsParallel(final SessionFactory sessionFactory, final Set<Class> classes, final boolean clear) {
        if (log.isDebugEnabled())
            log.debug("병렬 방식으로 모든 엔티티에 대해 전체 인덱싱을 수행합니다. classes=[{}], clear=[{}]", StringTool.listToString(classes), clear);

        Parallels.runEach(classes, new Action1<Class>() {
            @Override
            public void perform(Class clazz) {
                IHibernateSearchDao searchDao = new HibernateSearchDao();
                if (clear)
                    searchDao.clearIndex(clazz);
                searchDao.indexAll(clazz, 100);
            }
        });
    }

    /**
     * 루씬 검색용 QueryBuilder 를 생성합니다.
     *
     * @param clazz      검색할 엔티티 수형
     * @param fts        FullTextSession instance
     * @param parameters 사용할 파라미터 정보
     * @return {@link QueryBuilder} instance.
     */
    public static QueryBuilder buildLuceneQuery(final Class<?> clazz, final FullTextSession fts, SearchParameter... parameters) {
        Guard.shouldNotBeNull(fts, "fts");

        QueryBuilder builder = fts.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();

        for (SearchParameter sp : parameters) {

                log.trace("QueryBulider에 다음 parameter를 추가합니다... SearchParameter=[{}]", sp);

            QueryMethod queryMethod = sp.getQueryMethod();

            switch (queryMethod) {
                case Term:
                    builder.keyword().onField(sp.getName()).matching(sp.getValue());
                    break;

                case Phrase:
                    builder.phrase().onField(sp.getName()).sentence(sp.getValue());
                    break;

                case Wildcard:
                    final WildcardQuery wildcardQuery = new WildcardQuery(new Term(sp.getName(), sp.getValue()));
                    builder.bool().should(wildcardQuery);
                    break;

                case Prefix:
                    final PrefixQuery prefixQuery = new PrefixQuery(new Term(sp.getName(), sp.getValue()));
                    builder.bool().should(prefixQuery);
                    break;

                case Fuzzy:
                    final FuzzyQuery query = new FuzzyQuery(new Term(sp.getName(), sp.getValue()));
                    builder.bool().should(query);
                    break;

                case Range:
                    SearchRangeParameter srp = (SearchRangeParameter) sp;
                    builder.range().onField(sp.getName()).from(srp.getFrom()).to(srp.getTo());
                    break;

                case Boolean:
                default:
                    final TermQuery termQuery = new TermQuery(new Term(sp.getName(), sp.getValue()));
                    builder.bool().should(termQuery);
                    break;
            }
        }
        return builder;
    }
}
