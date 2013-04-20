package kr.debop4j.data.hibernate.unitofwork;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * {@link IUnitOfWorkImplementor} 를 구현한 기본 클래스
 *
 * @author sunghyouk.bae@gmail.com
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
