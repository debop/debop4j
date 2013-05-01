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
import org.apache.lucene.search.PhraseQuery;
import org.fest.assertions.Assertions;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.junit.Test;

import java.util.List;
import java.util.StringTokenizer;

/**
 * kr.debop4j.search.hibernate.query.PhraseQueryTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 23. 오후 9:06
 */
@Slf4j
public class PhraseQueryTest extends SearchTestBase {

    String[] descs = new String[] {
            "he hits the road as a traveling salesman",
            "Star Trek The Next Generation",
            "the fifth season of star trek",
            "to Star Trek fans everywhere the stellar second season",
            "a once-successful salesman"
    };

    private void buildIndex(FullTextSession fts) {
        Transaction tx = fts.beginTransaction();
        for (int i = 0; i < descs.length; i++) {
            Dvd dvd = new Dvd();
            dvd.setDescription(descs[i]);
            dvd.setId(i);
            fts.saveOrUpdate(dvd);
        }
        tx.commit();
        fts.clear();
    }

    @Test
    public void testPhaseQuery() throws Exception {
        buildIndex(fts);

        String userInput = "star trek";

        StringTokenizer st = new StringTokenizer(userInput, " ");
        PhraseQuery query = new PhraseQuery();
        while (st.hasMoreTokens()) {
            query.add(new Term("description", st.nextToken()));
        }
        log.debug("query=" + query.toString());

        FullTextQuery ftq = fts.createFullTextQuery(query, Dvd.class);
        List<Dvd> results = ftq.list();

        Assertions.assertThat(results.size()).isEqualTo(3);
        Assertions.assertThat(results.get(0).getDescription()).isEqualTo(descs[1]);

        for (Dvd dvd : results) {
            log.debug("Dvd=" + dvd);
        }

        for (Object element : fts.createQuery("from " + Dvd.class.getName()).list()) {
            fts.delete(element);
        }
    }
}
