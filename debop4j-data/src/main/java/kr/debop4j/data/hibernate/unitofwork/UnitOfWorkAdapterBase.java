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

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * {@link IUnitOfWorkImplementor} 를 구현한 기본 클래스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 29.
 */
@Slf4j
public abstract class UnitOfWorkAdapterBase implements IUnitOfWorkImplementor {

    /**
     * Transaction 하에서 현 Session 정보를 flush 합니다.
     */
    public void transactionalFlush() {
        transactionalFlush(new DefaultTransactionDefinition());
    }

    /**
     * 지정된 TransactionDefinition 에 따른 Transaction 하에서 현 Session 정보를 flush 합니다.
     */
    public void transactionalFlush(TransactionDefinition transactionDefinition) {
        if (log.isDebugEnabled())
            log.debug("Session 내용을 transaction 하에서 flush를 수행합니다...");

        if (transactionDefinition == null)
            transactionDefinition = new DefaultTransactionDefinition();

        IUnitOfWorkTransaction tx = null;

        try {
            // forces a flush of the current IUnitOfWork
            tx = UnitOfWorks.getCurrent().beginTransaction(transactionDefinition);
            tx.commit();

        } catch (Exception e) {
            log.error("Transaction 하에서 flush에 실패했습니다.", e);

            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception ignored) {}
            }

            throw new RuntimeException(e);
        }

        if (log.isDebugEnabled())
            log.debug("Session 내용을 transaction 하에서 flush를 수행했습니다!!!");
    }

}
