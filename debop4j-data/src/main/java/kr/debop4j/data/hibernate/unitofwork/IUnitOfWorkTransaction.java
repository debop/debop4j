package kr.debop4j.data.hibernate.unitofwork;


/**
 * kr.nsoft.data.hibernate.unitofwork.IUnitOfWorkTransaction
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 29.
 */
public interface IUnitOfWorkTransaction {

    /**
     * Transaction을 commit 합니다.
     */
    void commit();

    /**
     * Transaction을 Rollback합니다.
     */
    void rollback();
}
