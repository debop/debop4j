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

package kr.debop4j.search.hibernate.query;

import kr.debop4j.search.hibernate.SearchTestBase;
import kr.debop4j.search.hibernate.model.Dvd;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.fest.assertions.Assertions;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.search.hibernate.query.FuzzyQueryTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 23. 오후 9:33
 */
@Slf4j
public class FuzzyQueryTest extends SearchTestBase {

    String[] titles = new String[]{
            "Titan A.E.",
            "Little Women",
            "Little Shop of Horrors",
            "The Gren Mile",
            "Somewhere in Time",
            "태극기 휘날리며",
            "트랜스포머 3",
            "트랜스포터 3",
            "해리포터 1",
    };

    private void buildIndex(FullTextSession fts) {
        Transaction tx = fts.beginTransaction();
        for (int i = 0; i < titles.length; i++) {
            Dvd dvd = new Dvd();
            dvd.setTitle(titles[i]);
            dvd.setId(i);
            fts.saveOrUpdate(dvd);
        }
        tx.commit();
        fts.clear();
    }

    @Test
    public void testFuzzyQuery() throws Exception {

        buildIndex(fts);

        try {
            String userInput = "title";
            FuzzyQuery luceneQuery = new FuzzyQuery(new Term("title", userInput), 0.4f);

            log.debug("Query=" + luceneQuery.toString());

            FullTextQuery ftq = fts.createFullTextQuery(luceneQuery, Dvd.class);
            List<Dvd> results = ftq.list();

            Assertions.assertThat(results.size()).isEqualTo(5);
            // Assertions.assertThat(results.get(0).getTitle()).isEqualTo(titles[0]);

            for (Dvd dvd : results) {
                log.debug("Title=" + dvd.getTitle());
            }
        } finally {
            for (Object element : fts.createQuery("from " + Dvd.class.getName()).list()) {
                fts.delete(element);
            }
        }
    }


    @Test
    public void testKoreanFuzzyQuery() throws Exception {

        buildIndex(fts);

        try {
            //
            //TODO:  한글 분석기로는 FuzzyQuery가 제대로 작동하지 않는다.
            //
            String userInput = "포터";
            FuzzyQuery luceneQuery = new FuzzyQuery(new Term("title", userInput), 0.4f);

            log.debug("Query=" + luceneQuery.toString());

            FullTextQuery ftq = fts.createFullTextQuery(luceneQuery, Dvd.class);
            List<Dvd> results = ftq.list();

            // Assertions.assertThat(results.size()).isEqualTo(3);
            // Assertions.assertThat(results.get(0).getTitle()).isEqualTo(titles[7]);

            for (Dvd dvd : results) {
                log.debug("Title=" + dvd.getTitle());
            }
        } finally {
            for (Object element : fts.createQuery("from " + Dvd.class.getName()).list()) {
                fts.delete(element);
            }
        }
    }
}
