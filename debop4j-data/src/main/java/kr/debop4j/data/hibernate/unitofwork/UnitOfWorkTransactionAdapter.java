package kr.debop4j.data.hibernate.unitofwork;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;


/**
 * IUnitOfWork 에서 사용할 Transaction
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 29.
 */
@Slf4j
public class UnitOfWorkTransactionAdapter implements IUnitOfWorkTransaction {

    private final Transaction transaction;

    public UnitOfWorkTransactionAdapter(Transaction transaction) {
        this.transaction = transaction;
    }


    @Override
    public void commit() {
        if (log.isDebugEnabled())
            log.debug("현 Transaction의 Commit 작업을 시작합니다...");

        transaction.commit();

        if (log.isDebugEnabled())
            log.debug("현 Transaction의 Commit 작업을 완료했습니다.");
    }

    @Override
    public void rollback() {

        if (log.isDebugEnabled())
            log.debug("현 Transaction에 예외가 발생하여 rollback 합니다...");

        transaction.rollback();

        if (log.isDebugEnabled())
            log.debug("현 Transaction rollback 작업을 완료했습니다.");
    }
}
