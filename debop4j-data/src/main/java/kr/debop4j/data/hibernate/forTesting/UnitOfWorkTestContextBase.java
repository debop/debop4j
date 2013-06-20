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

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.UUID;

/**
 * kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19.
 */
@Slf4j
public abstract class UnitOfWorkTestContextBase implements AutoCloseable {

    /** The Current hibernate session key. */
    public final String CurrentHibernateSessionKey = UUID.randomUUID().toString();

    /** The Spring context. */
    protected GenericApplicationContext springContext;

    @Getter
    private Class dbConfigurationClass;
    @Getter
    private int unitOfWorkNestingLevel = -1;

    /**
     * Instantiates a new Unit of work test context base.
     *
     * @param dbConfigurationClass the db configuration class
     */
    protected UnitOfWorkTestContextBase(Class dbConfigurationClass) {
        this.dbConfigurationClass = dbConfigurationClass;

        springContext = new AnnotationConfigApplicationContext(dbConfigurationClass);
        Springs.init(springContext);
    }

    /**
     * Gets spring context.
     *
     * @return the spring context
     */
    public GenericApplicationContext getSpringContext() {
        synchronized (this) {
            if (springContext == null) {
                springContext = new AnnotationConfigApplicationContext(dbConfigurationClass);
            }
        }
        return springContext;
    }

    /** Create unit of work. */
    public void createUnitOfWork() {
        Guard.shouldBe(unitOfWorkNestingLevel != 0, "중첩된 UnitOfWork를 만들려면 createNestedUnitOfWork() 메소드를 사용하세요.");

        if (log.isInfoEnabled())
            log.info("현재 Session을 사용하여, UnitOfWork를 시작하고, Database를 설정합니다.");

        try {
            if (UnitOfWorks.isStarted()) {
                UnitOfWorks.stop();
                UnitOfWorks.closeUnitOfWorkFactory();
            }

            if (Springs.isNotInitialized())
                Springs.init(getSpringContext());

            UnitOfWorks.start();

            setupDatabase(UnitOfWorks.getCurrentSession());
        } finally {
            unitOfWorkNestingLevel = 0;
        }
    }

    /**
     * Create nested unit of work.
     *
     * @return the i unit of work
     */
    public IUnitOfWork createNestedUnitOfWork() {
        Guard.shouldBe(this.unitOfWorkNestingLevel != -1, "부모 UnitOfWork가 존재하지 않습니다. 먼저 createUnitOfWork() 를 호출하세요");

        try {
            IUnitOfWork uow = UnitOfWorks.start(UnitOfWorks.getCurrentSessionFactory(),
                                                UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork);
            return uow;
        } finally {
            unitOfWorkNestingLevel++;
        }
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public SessionFactory getSessionFactory() {
        return Springs.getBean(SessionFactory.class);
    }

    /**
     * Create session.
     *
     * @return the session
     */
    public Session createSession() {
        Session session = getSessionFactory().openSession();
        return session;
    }

    /**
     * Dispose session.
     *
     * @param sessionToClose the session to close
     */
    public void disposeSession(Session sessionToClose) {
        if (sessionToClose == null)
            return;

        if (log.isDebugEnabled())
            log.debug("hibernate session을 close 합니다...");

        sessionToClose.close();

        if (log.isDebugEnabled())
            log.debug("hibernate session을 close 했습니다.");
    }

    /** Dispose unit of work. */
    public void disposeUnitOfWork() {
        try {
            UnitOfWorks.stop();
        } finally {
            unitOfWorkNestingLevel--;
        }
    }

    /** Initialize container and unit of work factory. */
    abstract public void initializeContainerAndUnitOfWorkFactory();

    /**
     * Sets database.
     *
     * @param session the session
     */
    public void setupDatabase(Session session) {
        // Nothing to do.
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) &&
                (obj instanceof UnitOfWorkTestContextBase) &&
                (hashCode() == obj.hashCode());
    }

    @Override
    public int hashCode() {
        return HashTool.compute(CurrentHibernateSessionKey);
    }

    public String toString() {
        return Objects.toStringHelper(this)
                      .toString();
    }

    @Override
    public void close() throws Exception {
        this.dbConfigurationClass = null;

        UnitOfWorks.stop();

        if (springContext != null) {
            springContext.close();
            springContext = null;
        }
    }


    // region << static methods >>

    /**
     * Create unit of work test context base.
     *
     * @param dbConfigurationClass the db configuration class
     * @return the unit of work test context base
     */
    public static UnitOfWorkTestContextBase create(Class dbConfigurationClass) {
        return new UnitOfWorkTestContext(dbConfigurationClass);
    }

    // endregion
}
