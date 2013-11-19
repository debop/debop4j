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

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * {@link IUnitOfWork} 를 생성하는 Factory 의 인터페이스 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 29.
 */
public interface IUnitOfWorkFactory {

    /**
     * Current Hibernate Session 용 Key
     */
    public final String CURRENT_HIBERNATE_SESSION = IUnitOfWorkFactory.class.getName() + ".Current.Hibernate.Session";

    /**
     * 현 UnitOfWorkFactory가 사용하는 {@link org.hibernate.SessionFactory}
     */
    SessionFactory getSessionFactory();

    /**
     * 현 IUnitOfWorkFactory 가 사용할 {@link org.hibernate.SessionFactory} 를 설정합니다.
     *
     * @param sessionFactory 사용할 Session Factory
     */
    void setSessionFactory(SessionFactory sessionFactory);

    /**
     * 현 Thread-Context 에서 사용할 Session 을 반환합니다.
     * {@link UnitOfWorks#start()} 시에 Session은 생성됩니다.
     */
    Session getCurrentSession();

    /**
     * 현 Thread-Context 에서 사용할 Session 을 설정합니다.
     */
    void setCurrentSession(Session session);

    /**
     * 현 UnitOfWorkFactory를 초기화합니다.
     */
    void init();

    /**
     * 새로운 {@link IUnitOfWorkImplementor} 인스턴스를 생성합니다.
     *
     * @param factory  SessionFactory 인스턴스
     * @param previous 기존 {@link IUnitOfWorkImplementor} 인스턴스, 없다면 null
     * @return 새로운 {@link IUnitOfWorkImplementor} 인스턴스
     */
    IUnitOfWorkImplementor create(SessionFactory factory, IUnitOfWorkImplementor previous);


    /**
     * 지정된 {@link IUnitOfWorkImplementor} 인스턴스를 닫습니다.
     *
     * @param adapter {@link IUnitOfWorkImplementor} instance to be closed.
     */
    void closeUnitOfWork(IUnitOfWorkImplementor adapter);
}
