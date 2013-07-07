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
import kr.debop4j.core.compress.*;
import kr.debop4j.core.unitTesting.TestTool;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static kr.debop4j.core.spring.Springs.getOrRegisterBean;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * kr.debop4j.core.spring.SpringTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 2.
 */
@Slf4j
public class SpringTest extends AbstractTest {

    private static final Object syncLock = new Object();

    private static Class[] compressorClasses =
            new Class[] { BZip2Compressor.class,
                    DeflateCompressor.class,
                    GZipCompressor.class,
                    XZCompressor.class };

    @Override
    protected void onBefore() {
        if (Springs.isNotInitialized())
            Springs.init(new GenericApplicationContext());
    }

    @Override
    protected void onAfter() {
        Springs.reset();
    }

    @Test
    public void localContainerOverrideGlobalOne() {
        synchronized (syncLock) {

            Springs.reset();

            GenericApplicationContext context = new GenericApplicationContext();

            Springs.init(context);

            assertSame(context, Springs.getContext());

            GenericApplicationContext localContext = new GenericApplicationContext();

            try (AutoCloseableAction action = Springs.useLocalContext(localContext)) {
                assertSame(localContext, Springs.getContext());
            } catch (Exception ex) {
                fail(ex.getMessage());
            }

            Assertions.assertThat(Springs.getContext()).isEqualTo(context);
        }
        Springs.reset();
    }

    @Test
    public void getBeanIfNoRegisteredBean() {

        try {
            Integer bean = Springs.getBean(Integer.class);
            assertNull(bean);
        } catch (Exception e) {
        }

        Long longBean = Springs.tryGetBean(Long.class);
        assertNull(longBean);
    }

    @Test
    public void getOrRegisterBean_NotRegisteredBean() {

        ArrayList arrayList = getOrRegisterBean(ArrayList.class);
        assertNotNull(arrayList);
    }

    @Test
    public void getOrRegisterBean_NotRegisteredBean_WithScope() {

        ICompressor compressor = getOrRegisterBean(GZipCompressor.class, BeanDefinition.SCOPE_PROTOTYPE);
        assertThat(compressor).isNotNull();
        assertThat(compressor).isInstanceOf(GZipCompressor.class);

        Springs.removeBean(GZipCompressor.class);

        compressor = Springs.tryGetBean(GZipCompressor.class);
        assertNull(compressor);

        ICompressor deflator = getOrRegisterBean(DeflateCompressor.class, BeanDefinition.SCOPE_SINGLETON);
        assertThat(deflator).isNotNull();
        assertThat(deflator).isInstanceOf(DeflateCompressor.class);
    }

    @Test
    public void getOrRegisterBean_WithSubClass_WithScope() {

        Object compressor = getOrRegisterBean(ICompressor.class, GZipCompressor.class, BeanDefinition.SCOPE_PROTOTYPE);
        assertNotNull(compressor);
        assertTrue(compressor instanceof GZipCompressor);

        Springs.removeBean(GZipCompressor.class);

        compressor = Springs.tryGetBean(GZipCompressor.class);
        assertNull(compressor);

        ICompressor deflator = getOrRegisterBean(ICompressor.class, DeflateCompressor.class, BeanDefinition.SCOPE_SINGLETON);
        assertNotNull(deflator);
        assertTrue(deflator instanceof DeflateCompressor);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void getAllTypes() {

        for (Class clazz : compressorClasses) {
            ICompressor bean = (ICompressor) getOrRegisterBean(clazz, BeanDefinition.SCOPE_PROTOTYPE);
            assertThat(bean).isNotNull();
        }

        Map<String, ICompressor> compressorMap = Springs.getBeansOfType(ICompressor.class, true, true);
        assertTrue(compressorMap.size() > 0);
        assertEquals(compressorClasses.length, compressorMap.size());

        ICompressor gzip = Springs.getBean(GZipCompressor.class);
        assertThat(gzip).isNotNull();
        assertThat(gzip).isInstanceOf(GZipCompressor.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getBeansByTypeTest() {

        for (Class clazz : compressorClasses) {
            ICompressor bean = (ICompressor) getOrRegisterBean(clazz, BeanDefinition.SCOPE_PROTOTYPE);
            assertNotNull(bean);
        }

        List<ICompressor> compressors = Springs.getBeansByType(ICompressor.class);
        assertTrue(compressors.size() > 0);
        for (ICompressor compressor : compressors)
            assertNotNull(compressor);
    }

    @Test
    public void getAllTypesInMultiThread() {
        TestTool.runTasks(5, new Runnable() {
            @Override
            public void run() {
                getAllTypes();
            }
        });
    }
}
