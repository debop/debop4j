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

import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Hibernate 용 IUnitOfWork Factory 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 30.
 */
@Slf4j
public class UnitOfWorkFactory implements IUnitOfWorkFactory {

    protected final Object syncLock = new Object();
    protected SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        Guard.shouldNotBeNull(sessionFactory, "sessionFactory");
        if (log.isInfoEnabled())
            log.info("SessionFactory를 설정합니다. sessionFactory=[{}]", sessionFactory);

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getCurrentSession() {
        Session session = (Session) Local.get(IUnitOfWorkFactory.CURRENT_HIBERNATE_SESSION);
        Guard.shouldBe(session != null, "Session이 현 Thread Context에서 생성되지 않았습니다. UnitOfWorks.getStart() 를 먼저 호출하셔야 합니다.");
        return session;
    }

    @Override
    public void setCurrentSession(Session session) {
        if (log.isDebugEnabled())
            log.debug("현 ThreadContext의 Session을 설정합니다. session=[{}]", session);
        Local.put(IUnitOfWorkFactory.CURRENT_HIBERNATE_SESSION, session);
    }


    @Override
    public void init() {
        if (log.isInfoEnabled())
            log.info("Hibernate 용 UnitOfWorkFactory를 초기화합니다.");
    }

    @Override
    public IUnitOfWorkImplementor create(SessionFactory factory, IUnitOfWorkImplementor previous) {
        if (log.isDebugEnabled())
            log.debug("새로운 IUnitOfWorkImplementor 인스턴스를 생성합니다... factory=[{}], previous=[{}]",
                      factory, previous);

        if (factory == null)
            factory = this.sessionFactory;

        if (log.isDebugEnabled())
            log.debug("Local ThreadContext 에 Session을 설정합니다...");

        // Builder 패턴을 사용해도 됩니다.
        // sSession session = factory.withOptions().interceptor(interceptor).openSession();
        Session session = factory.openSession();
        setCurrentSession(session);

        return new UnitOfWorkAdapter(this, session, (UnitOfWorkAdapter) previous);
    }

    @Override
    public void closeUnitOfWork(IUnitOfWorkImplementor adapter) {
        Guard.shouldNotBeNull(adapter, "adapter");

        if (log.isDebugEnabled())
            log.debug("[{}]를 close 합니다.", adapter.getClass().getName());

        Session session = null;
        if (adapter.getPrevious() != null)
            session = adapter.getPrevious().getSession();

        setCurrentSession(session);
        UnitOfWorks.closeUnitOfWork(adapter);
    }
}
