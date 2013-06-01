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

package kr.debop4j.data.ogm.test.loader;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.junit.Test;

/**
 * kr.debop4j.data.ogm.test.loader.LoaderFromTupleTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class LoaderFromTupleTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Feeling.class };
    }

    @Test
    public void loadingFromTuple() throws Exception {

//        final Session session = openSession();
//        Transaction transaction = session.beginTransaction();
//
//        Feeling feeling = new Feeling();
//        feeling.setName("Moody");
//        session.persist(feeling);
//
//        transaction.commit();
//        session.clear();
//
//        EntityKey key = new EntityKey(new EntityKeyMetadata("Feeling", new String[] { "id" }),
//                                      new Object[] { feeling.getId() });
//        Map<String, Object> entityTuple = (Map<String, Object>) extractEntityTuple(sessions, key);
//        final Tuple tuple = new Tuple(new MapTupleSnapshot(entityTuple));
//
//        EntityPersister persister =
//                ((SessionFactoryImplementor) session.getSessionFactory())
//                        .getEntityPersister(Feeling.class.getName());
//
//        OgmLoader loader = new OgmLoader(new OgmEntityPersister[] { (OgmEntityPersister) persister });
//        OgmLoadingContext ogmLoadingContext = new OgmLoadingContext();
//        List<Tuple> tuples = new ArrayList<Tuple>();
//        tuples.add(tuple);
//        ogmLoadingContext.setTuples(tuples);
//
//        List<Object> entities = loader.loadEntities((SessionImplementor) session, LockOptions.NONE, ogmLoadingContext);
//        assertThat(entities.size()).isEqualTo(1);
//        assertThat(((Feeling) entities.get(0)).getName()).isEqualTo("Moody");
//
//        session.close();
    }
}
