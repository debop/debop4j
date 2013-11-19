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

/**
 * kr.nsoft.data.hibernate.unitofwork.IUnitOfWorkImplementor
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 29.
 */
public interface IUnitOfWorkImplementor extends IUnitOfWork {

    /**
     * 인스턴스의 사용 횟수를 구한다.
     */
    int getUsage();

    /**
     * 현 인스턴스의 사용 Count를 증가 시킨다.
     */
    int increseUsage();

    /**
     * 중첩 방식으로 IUnitOfWork 를 사욜할 때, 바로 전의 {@link IUnitOfWorkImplementor} 를 나타낸다.
     * 중첩이 아니면 null을 반환한다.
     */
    IUnitOfWorkImplementor getPrevious();

    /**
     * 현 Thread-Context 에서 사용할 {@link org.hibernate.Session}
     */
    Session getSession();
}
