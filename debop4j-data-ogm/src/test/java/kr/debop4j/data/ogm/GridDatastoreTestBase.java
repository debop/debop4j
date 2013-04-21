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

package kr.debop4j.data.ogm;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * kr.debop4j.data.ogm.GridDatastoreTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 12:05
 */
public class GridDatastoreTestBase {

    @BeforeClass
    public static void beforeClass() {
        Springs.initByAnnotatedClasses(GridDatastoreConfiguration.class);
    }

    @AfterClass
    public static void afterClass() {
        Springs.reset();
    }

    @Before
    public void before() {
        UnitOfWorks.start();
    }

    @After
    public void after() {
        UnitOfWorks.stop();
    }
}
