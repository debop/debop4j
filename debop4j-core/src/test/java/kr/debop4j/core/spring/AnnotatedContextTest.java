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

package kr.debop4j.core.spring;

import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.AutoCloseableAction;
import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.ICompressor;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * kr.debop4j.core.spring.AnnotatedContextTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 2.
 */
public class AnnotatedContextTest extends AbstractTest {

    @Test
    public void initByAnnotatedClasses() {
        try (AutoCloseableAction action =
                     Springs.useLocalContext(new AnnotationConfigApplicationContext(AnnotatedBeanConfig.class))) {
            assertTrue(Springs.isInitialized());

            ICompressor compressor = (ICompressor) Springs.getBean("defaultCompressor");
            assertNotNull(compressor);
            assertTrue(compressor instanceof GZipCompressor);
        }
    }

    @Test
    public void initByPackages() {
        final String packageName = AnnotatedBeanConfig.class.getPackage().getName();

        try (AutoCloseableAction action =
                     Springs.useLocalContext(new AnnotationConfigApplicationContext(packageName))) {
            Springs.initByPackages(AnnotatedBeanConfig.class.getPackage().getName());
            assertTrue(Springs.isInitialized());

            String[] beanNames = Springs.getBeanNamesForType(ICompressor.class, true, true);

            assertTrue(beanNames.length > 0);

            ICompressor compressor = (ICompressor) Springs.getBean("defaultCompressor");
            assertThat(compressor).isNotNull();
            assertThat(compressor).isInstanceOf(GZipCompressor.class);
        }
    }
}
