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

package kr.debop4j.data.ehcache.ogm.dialect.ehcache;

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.dialect.ehcache.EhcacheDialect;
import org.hibernate.ogm.grid.RowKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * kr.debop4j.data.ehcache.ogm.dialect.ehcache.EhcacheDialectTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29. 오후 9:50
 */
@Slf4j
public class EhcacheDialectTest {

    private static final int LOOPS = 250;
    private static final int THREADS = 32;

    private EhcacheDialect dialect;

    @Before
    public void setup() {
        final EhcacheDatastoreProvider datastoreProvider = new EhcacheDatastoreProvider();
        datastoreProvider.configure(new HashMap());
        datastoreProvider.start();
        dialect = new EhcacheDialect(datastoreProvider);
    }

    @Test
    public void isThreadSafe() throws InterruptedException {

        final RowKey test = new RowKey("test", null, null);
        //Thread[] threads = new Thread[THREADS];

        Parallels.run(THREADS, new Action1<Integer>() {
            @Override
            public void perform(Integer arg) {

                    log.trace("perform [{}]", arg);
                final IdentifierGeneratorHelper.BigIntegerHolder value =
                        new IdentifierGeneratorHelper.BigIntegerHolder();
                for (int i = 0; i < LOOPS; i++) {
                    dialect.nextValue(test, value, 1, 1);
                }
            }
        });

        final IdentifierGeneratorHelper.BigIntegerHolder value = new IdentifierGeneratorHelper.BigIntegerHolder();
        dialect.nextValue(test, value, 0, 1);

        Assert.assertEquals(LOOPS * THREADS, value.makeValue().intValue());
    }
}
