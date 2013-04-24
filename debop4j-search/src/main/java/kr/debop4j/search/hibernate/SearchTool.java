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

package kr.debop4j.search.hibernate;

import com.google.common.collect.Sets;
import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import kr.debop4j.search.dao.HibernateSearchDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.event.impl.FullTextIndexEventListener;

import java.util.Collection;
import java.util.Set;

/**
 * kr.debop4j.search.hibernate.SearchTool
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 28.
 */
@Slf4j
public class SearchTool {

    private SearchTool() {}

    /**
     * Hibernate-Search의 FullTextIndexEventListener를 SessionFactory에 등록합니다.
     */
    public static void registerFullTextIndexEventListener(SessionFactory sessionFactory, FullTextIndexEventListener listener) {
        assert sessionFactory != null;
        log.info("sessionFactory에 FullTestIndexEventListener를 등록합니다...");

        try {
            HibernateTool.registerEventListener(sessionFactory, listener,
                                                EventType.POST_UPDATE,
                                                EventType.POST_INSERT,
                                                EventType.POST_DELETE,
                                                EventType.FLUSH);
        } catch (Throwable t) {
            log.warn("listener를 등록하는데 실패했습니다.", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 루씬용 Query를 빌드합니다.
     */
    public org.apache.lucene.search.Query bulidLuceneQuery(FullTextSession fts, Class<?> clazz, String fieldName, String values) {
        if (log.isTraceEnabled())
            log.trace("루씬 쿼리를 빌드합니다. clazz=[{}], fieldName=[{}], values=[{}]", clazz, fieldName, values);

        Analyzer analyzer;
        if (clazz == null) {
            analyzer = new SimpleAnalyzer(Version.LUCENE_36);
        } else {
            analyzer = fts.getSearchFactory().getAnalyzer(clazz);
        }

        QueryParser parser = new QueryParser(Version.LUCENE_36, fieldName, analyzer);
        org.apache.lucene.search.Query luceneQuery = null;
        try {
            luceneQuery = parser.parse(values);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to parse search entry into a Lucene query", e);
        }
        return luceneQuery;
    }

    public FullTextQuery createFullTextQuery(FullTextSession fts, org.apache.lucene.search.Query luceneQuery, Class<?> clazz) {

        FullTextQuery ftq = fts.createFullTextQuery(luceneQuery, clazz);

        // 기본값이 SKIP, QUERY 입니다.
        // ftq.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.QUERY);
        return ftq;
    }

    public Set<Class> getIndexedClasses(SessionFactory sessionFactory) {
        if (log.isDebugEnabled())
            log.debug("매핑된 엔티티중에 인덱싱을 수행할 엔티티들을 조회합니다.");

        final Set<Class> classes = Sets.newHashSet();
        Collection<ClassMetadata> metadatas = sessionFactory.getAllClassMetadata().values();

        for (ClassMetadata meta : metadatas) {
            Class clazz = meta.getMappedClass();
            if (clazz.getAnnotation(Indexed.class) != null) {
                classes.add(clazz);
                if (log.isTraceEnabled())
                    log.trace("인덱싱된 엔티티=[{}]", clazz);
            }
        }
        return classes;
    }

    /**
     * 모든 엔티티의 인덱스를 재구성합니다.
     * see {@link SearchTool#getIndexedClasses(org.hibernate.SessionFactory)}
     */
    public void indexAll(final SessionFactory sessionFactory, final Set<Class> classes, final boolean clear) {
        if (log.isDebugEnabled())
            log.debug("모든 엔티티에 대해 전체 인덱싱을 수행합니다. classes=[{}], clear=[{}]", StringTool.listToString(classes), clear);

        final HibernateSearchDaoImpl searchDao = new HibernateSearchDaoImpl(sessionFactory);
        for (Class clazz : classes) {
            if (clear)
                searchDao.clearIndex(clazz);
            searchDao.indexAll(clazz, 100);
        }
    }

    /**
     * 모든 엔티티의 인덱스를 병렬 방식으로 재구성합니다.
     * see {@link SearchTool#getIndexedClasses(org.hibernate.SessionFactory)}
     */
    public void indexAllAsParallel(final SessionFactory sessionFactory, final Set<Class> classes, final boolean clear) {
        if (log.isDebugEnabled())
            log.debug("병렬 방식으로 모든 엔티티에 대해 전체 인덱싱을 수행합니다. classes=[{}], clear=[{}]", StringTool.listToString(classes), clear);
        Parallels.runEach(classes, new Action1<Class>() {
            @Override
            public void perform(Class clazz) {
                HibernateSearchDaoImpl searchDao = new HibernateSearchDaoImpl(sessionFactory);
                if (clear)
                    searchDao.clearIndex(clazz);
                searchDao.indexAll(clazz, 100);
            }
        });
    }
}
