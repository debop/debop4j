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

package kr.debop4j.data.hibernate.unitofwork;

import kr.debop4j.core.AutoCloseableAction;
import kr.debop4j.core.Local;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.concurrent.ThreadSafe;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Unit of Work 패턴을 구현한 Static 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 18
 */
@Component
@Slf4j
@ThreadSafe
public final class UnitOfWorks {

    private UnitOfWorks() { }

    static {
        log.info("UnitOfWorks 인스턴스가 생성되었습니다.");
    }

    private static final String UNIT_OF_WORK_NOT_STARTED = "UnitOfWorks가 시작되지 않았습니다. 사용 전에 UnitOfWorks.start()를 호출하세요.";

    private static volatile IUnitOfWork globalNonThreadSafeUnitOfWork;
    private static volatile IUnitOfWorkFactory unitOfWorkFactory;

    /** UnitOfWork 가 이미 시작되었는지 확인한다. */
    public static synchronized boolean isStarted() {
        return globalNonThreadSafeUnitOfWork != null || Local.get(IUnitOfWork.CURRENT_UNIT_OF_WORK_KEY) != null;
    }

    /** 현재 시작된 {@link IUnitOfWork}의 인스턴스 ({@link UnitOfWorkAdapter}를 반환합니다. */
    public static synchronized IUnitOfWork getCurrent() {
        if (!isStarted())
            throw new HibernateException(UNIT_OF_WORK_NOT_STARTED);

        if (globalNonThreadSafeUnitOfWork != null)
            return globalNonThreadSafeUnitOfWork;

        return Local.get(IUnitOfWork.CURRENT_UNIT_OF_WORK_KEY, IUnitOfWork.class);
    }

    /**
     * Gets current session factory.
     *
     * @return the current session factory
     */
    public static synchronized SessionFactory getCurrentSessionFactory() {
        return getUnitOfWorkFactory().getSessionFactory();
    }

    /**
     * Gets current session.
     *
     * @return the current session
     */
    public static synchronized Session getCurrentSession() {
        return getUnitOfWorkFactory().getCurrentSession();
    }

    /**
     * Gets unit of work factory.
     *
     * @return {@link IUnitOfWorkFactory} instance.
     */
    public static synchronized IUnitOfWorkFactory getUnitOfWorkFactory() {
        if (unitOfWorkFactory == null)
            throw new RuntimeException("Spring 환경설정에서 UnitOfWorks를 ComponentScan에 추가해주세요.");

        return unitOfWorkFactory;
    }

    /**
     * Spring으로부터 {@link IUnitOfWorkFactory}를 injection 을 받습니다.<p/>
     * 참고 : http://debop.blogspot.kr/2013/05/spring-framework-static-field-injection.html
     *
     * @param factory {@link IUnitOfWorkFactory} instance.
     */
    @Autowired
    public void injectUnitOfWorkFactory(IUnitOfWorkFactory factory) {
        log.info("Spring에서 UnitOfWorkFactory를 인젝션합니다. unitOfWorkFactory=[{}]", factory);

        unitOfWorkFactory = factory;
    }

    /**
     * Sets unit ozf work factory.
     *
     * @param factory the factory
     */
    public static synchronized void setUnitOfWorkFactory(IUnitOfWorkFactory factory) {
        log.info("UnitOfWorkFactory를 설정합니다. unitOfWorkFactory=[{}]", factory);

        unitOfWorkFactory = factory;
    }

    /**
     * Set current unit of work.
     *
     * @param unitOfWork the unit of work
     */
    public static void setCurrent(IUnitOfWork unitOfWork) {
        if (log.isDebugEnabled())
            log.debug("현 Thread Context의 UnitOfWork 인스턴스를 설정합니다. unitOfWork=[{}]", unitOfWork);

        Local.put(IUnitOfWork.CURRENT_UNIT_OF_WORK_KEY, unitOfWork);
    }

    /**
     * Register global unit of work.
     *
     * @param globalUnitOfWork the global unit of work
     * @return the auto closeable action
     */
    public static synchronized AutoCloseableAction registerGlobalUnitOfWork(IUnitOfWork globalUnitOfWork) {
        if (log.isDebugEnabled())
            log.debug("전역 IUnitOfWork를 설정합니다. globalUnitOfWork=[{}]", globalUnitOfWork);

        globalNonThreadSafeUnitOfWork = globalUnitOfWork;

        return new AutoCloseableAction(new Runnable() {
            @Override
            public void run() {
                globalNonThreadSafeUnitOfWork = null;
            }
        });
    }

    /**
     * Start new unit of work.
     *
     * @return {@link IUnitOfWork} instance.
     */
    public static synchronized IUnitOfWork start() {
        return start(null, UnitOfWorkNestingOptions.ReturnExistingOrCreateUnitOfWork);
    }

    /**
     * Start new unit of work.
     *
     * @param nestingOptions 생성 옵션 {@link UnitOfWorkNestingOptions}
     * @return {@link IUnitOfWork} instance.
     */
    public static synchronized IUnitOfWork start(UnitOfWorkNestingOptions nestingOptions) {
        return start(null, nestingOptions);
    }

    /**
     * Start new unit of work.
     *
     * @param sessionFactory {@link SessionFactory} instance.
     * @return {@link IUnitOfWork} instance.
     */
    public static synchronized IUnitOfWork start(SessionFactory sessionFactory) {
        return start(sessionFactory, UnitOfWorkNestingOptions.ReturnExistingOrCreateUnitOfWork);
    }

    /**
     * Start new unit of work.
     *
     * @param sessionFactory {@link SessionFactory} instance.
     * @param nestingOptions 생성 옵션 {@link UnitOfWorkNestingOptions}
     * @return {@link IUnitOfWork} instance.
     */
    public static synchronized IUnitOfWork start(SessionFactory sessionFactory, UnitOfWorkNestingOptions nestingOptions) {
        if (log.isDebugEnabled())
            log.debug("새로운 UnitOfWork를 시작합니다... sessionFactory=[{}], nestingOptions=[{}]", sessionFactory, nestingOptions);

        if (globalNonThreadSafeUnitOfWork != null)
            return globalNonThreadSafeUnitOfWork;

        IUnitOfWorkImplementor existing = Local.get(IUnitOfWork.CURRENT_UNIT_OF_WORK_KEY, IUnitOfWorkImplementor.class);
        boolean useExisting =
                existing != null &&
                        nestingOptions == UnitOfWorkNestingOptions.ReturnExistingOrCreateUnitOfWork;

        if (useExisting) {

                log.trace("기존 IUnitOfWork 가 존재하므로, 사용횟수만 증가시키고, 기존 IUnitOfWork 인스턴스를 반환합니다. 사용횟수=[{}]", existing.getUsage());

            existing.increseUsage();
            return existing;
        }


            log.trace("새로운 IUnitOfWorkFactory 와 IUnitOfWork 를 생성합니다...");

        if (existing != null && sessionFactory == null) {
            sessionFactory = existing.getSession().getSessionFactory();
        } else if (existing == null) {
            sessionFactory = getCurrentSessionFactory();
        }

        setCurrent(getUnitOfWorkFactory().create(sessionFactory, existing));

        if (log.isDebugEnabled())
            log.debug("새로운 IUnitOfWork를 시작했습니다. sessionFactory=[{}]", sessionFactory);

        return getCurrent();
    }

    /** 현재 실행중인 UnitOfWork를 종료합니다. */
    public static synchronized void stop() {
        stop(false);
    }

    /**
     * 현재 실행중인 UnitOfWork를 종료합니다.
     *
     * @param needFlushing Session에 반영된 내용을 flushing 할 것인지 여부
     */
    public static synchronized void stop(boolean needFlushing) {
        log.trace("현재 실행중인 UnitOfWork를 중지합니다... needFlushing=[{}]", needFlushing);

        if (isStarted() && getCurrent() != null) {
            if (needFlushing) {
                try {
                    log.trace("현 UnitOfWork의 Session에 대해 flushing 작업을 시작합니다...");

                    getCurrent().flushSession();

                    log.trace("현 UnitOfWork의 Session에 대해 flushing 작업을 완료합니다...");
                } catch (Exception ignored) {
                    log.error("UnitOfWork의 Session을 Flushing하는 중 예외가 발생했습니다.", ignored);
                }
            }

            getCurrent().close();
        }
        setCurrent(null);
        log.debug("현재 실행중인 UnitOfWork를 종료했습니다.");
    }

    /**
     * UnitOfWork 내에서 runnable을 수행합니다.
     *
     * @param runnable 수행할 코드 블럭
     */
    public static void run(Runnable runnable) {
        shouldNotBeNull(runnable, "runnable");
        try (IUnitOfWork unitOfWork = start()) {
            runnable.run();
        }
    }

    /**
     * UnitOfWork 내에서 runnable을 수행합니다.
     *
     * @param sessionFactory the session factory
     * @param runnable       수행할 코드 블럭
     */
    public static void run(SessionFactory sessionFactory, Runnable runnable) {
        shouldNotBeNull(runnable, "runnable");
        try (IUnitOfWork unitOfWork = start(sessionFactory)) {
            runnable.run();
        }
    }

    /**
     * UnitOfWork 내에서 runnable을 수행합니다.
     *
     * @param options  Unit of work 생성 옵션
     * @param runnable 수행할 코드 블럭
     */
    public static void run(UnitOfWorkNestingOptions options, Runnable runnable) {
        shouldNotBeNull(runnable, "runnable");
        try (IUnitOfWork unitOfWork = start(options)) {
            runnable.run();
        }
    }

    /**
     * UnitOfWork 내에서 runnable을 수행합니다.
     *
     * @param sessionFactory the session factory
     * @param options        Unit of work 생성 옵션
     * @param runnable       수행할 코드 블럭
     */
    public static void run(SessionFactory sessionFactory, UnitOfWorkNestingOptions options, Runnable runnable) {
        shouldNotBeNull(runnable, "runnable");
        try (IUnitOfWork unitOfWork = start(sessionFactory, options)) {
            runnable.run();
        }
    }

    /**
     * Unit of work 를 종료합니다.
     *
     * @param unitOfWork 닫을 unit of work
     */
    public static synchronized void closeUnitOfWork(IUnitOfWork unitOfWork) {
        if (log.isDebugEnabled())
            log.debug("UnitOfWork를 종료합니다. 종료되는 IUnitOfWork 의 Previous 를 Current UnitOfWork로 교체합니다.");

        setCurrent((unitOfWork != null) ? ((IUnitOfWorkImplementor) unitOfWork).getPrevious() : null);
    }

    /** Close unit of work factory. */
    public static synchronized void closeUnitOfWorkFactory() {
        log.info("UnitOfWorkFactory를 종료합니다.");
        unitOfWorkFactory = null;
    }
}
