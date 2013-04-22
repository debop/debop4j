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

import kr.debop4j.data.hibernate.tools.HibernateTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.event.impl.FullTextIndexEventListener;

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
}
