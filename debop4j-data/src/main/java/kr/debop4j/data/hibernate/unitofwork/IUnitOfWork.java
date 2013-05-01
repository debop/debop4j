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

import org.springframework.transaction.TransactionDefinition;

/**
 * kr.nsoft.data.hibernate.unitofwork.IUnitOfWork
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 27.
 */
public interface IUnitOfWork extends AutoCloseable {

    /** Current {@link org.hibernate.Session} 의 변경 내용을 flush 해서 저장소에 적용되도록 합니다. */
    void flushSession();

    /** Current {@link org.hibernate.Session} 의 정보를 메모리에서 삭제합니다. */
    void clearSession();

    /** 현 IUnitOfWork 에 Transaction이 활성화 여부를 반환합니다. */
    boolean isInActiveTransaction();

    /** 현 IUnitOfWork 에 새로운 Transaction을 시작합니다. */
    IUnitOfWorkTransaction beginTransaction();

    /** 지정된 Transaction 정의를 이용하여 Transaction을 생성, 시작합니다. */
    IUnitOfWorkTransaction beginTransaction(TransactionDefinition transactionDefinition);

    /** 현 {@link org.hibernate.Session} 의 변경내용을 Transaction을 적용하여 flush를 수행합니다. */
    void transactionalFlush();

    /** 현 {@link org.hibernate.Session} 의 변경 내용을 {@link org.springframework.transaction.TransactionDefinition} 설정을 가지는 Transaction을 이용하여 flush 를 수행합니다. */
    void transactionalFlush(TransactionDefinition transactionDefinition);

    @Override
    void close();
}
