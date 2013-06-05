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

package kr.debop4j.data.hibernate.forTesting;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 여러 Database에 대해, Hibernate 작업 Test를 위한 기본 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19.
 */
@Slf4j
public abstract class DatabaseTestFixtureBase {

    @Getter
    private static final List<UnitOfWorkTestContextBase> contexts = Lists.newArrayList();

    @Getter
    private static UnitOfWorkTestContextBase currentContext;

    private static final String TEST_MODE_KEY = DatabaseTestFixtureBase.class.getName() + ".isRunningInTestMode";

    /**
     * Is running in test mode.
     *
     * @return the boolean
     */
    public static boolean isRunningInTestMode() {
        Boolean isTestMode = (Boolean) Local.get(TEST_MODE_KEY);
        return (isTestMode != null) ? isTestMode : false;
    }

    /**
     * Sets running in test mode.
     *
     * @param testMode the test mode
     */
    public static void setRunningInTestMode(boolean testMode) {
        Local.put(TEST_MODE_KEY, testMode);
    }

    /**
     * Init Hibernate and Spring.
     *
     * @param dbConfigurationClass the db configuration class
     */
    public static void initHibernateAndSpring(Class dbConfigurationClass) {

        UnitOfWorkTestContextBase context = getUnitOfWorkTestContext(dbConfigurationClass);
        setRunningInTestMode(true);

        if (currentContext != context) {
            context.initializeContainerAndUnitOfWorkFactory();
        }

        currentContext = context;
    }

    /**
     * Gets unit of work test context.
     *
     * @param dbConfigurationClass the db configuration class
     * @return the unit of work test context
     */
    protected static UnitOfWorkTestContextBase getUnitOfWorkTestContext(final Class dbConfigurationClass) {

        UnitOfWorkTestContextBase context = null;
        if (contexts.size() > 0) {
            Predicate<UnitOfWorkTestContextBase> criteria =
                    new Predicate<UnitOfWorkTestContextBase>() {
                        @Override
                        public boolean apply(@Nullable UnitOfWorkTestContextBase input) {
                            return (input != null) && dbConfigurationClass == input.getDbConfigurationClass();
                        }
                    };
            try {
                context = Iterables.find(contexts, criteria);
            } catch (NoSuchElementException ignored) {
                context = null;
            }
        }

        if (context == null) {
            context = UnitOfWorkTestContextBase.create(dbConfigurationClass);
            contexts.add(context);

            if (log.isDebugEnabled())
                log.debug("create UnitOfWorkTestContext by [{}]", dbConfigurationClass);
        }

        return context;
    }

    /** Close unit of work test contexts. */
    public static void closeUnitOfWorkTestContexts() {
        if (log.isDebugEnabled())
            log.debug("모든 테스트용 UnitOfWorkTestContext 를 종료합니다...");

        for (UnitOfWorkTestContextBase context : contexts) {
            try {
                if (context != null)
                    context.close();

            } catch (Exception ignored) {
                log.warn("Context 종료 시 예외가 발생했습니다. 무시합니다.", ignored);
            }
        }
        try {
            currentContext.close();
            currentContext = null;
        } catch (Exception ignored) { }

        setRunningInTestMode(false);
        contexts.clear();

        UnitOfWorks.closeUnitOfWorkFactory();

        if (log.isDebugEnabled())
            log.debug("모든 UnitOfWorkTestContext를 종료했습니다.");
    }
}
