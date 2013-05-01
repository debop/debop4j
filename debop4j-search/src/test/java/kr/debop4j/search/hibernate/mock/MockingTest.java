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

package kr.debop4j.search.hibernate.mock;

import kr.debop4j.search.hibernate.model.Item;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.SearchFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;


/**
 * kr.debop4j.search.hibernate.mock.MockingTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오전 11:42
 */
public class MockingTest {

    @Test
    public void searchTest() throws Exception {
        FullTextQuery ftq = createMock(FullTextQuery.class);
        FullTextSession fts = createMock(FullTextSession.class);
        SearchFactory factory = createMock(SearchFactory.class);

        expect(fts.getSearchFactory()).andReturn(factory);
        expect(factory.getAnalyzer(Item.class)).andReturn(new StandardAnalyzer(Version.LUCENE_36));
        expect(fts.createFullTextQuery(isA(Query.class), eq(Item.class))).andReturn(ftq);

        expect(ftq.setProjection("title")).andReturn(ftq);

        List<Object[]> results = new ArrayList<Object[]>();
        results.add(new Object[] { "The Incredibles" });
        expect(ftq.list()).andReturn(results);

        replay(factory);
        replay(ftq);
        replay(fts);

    }
}
