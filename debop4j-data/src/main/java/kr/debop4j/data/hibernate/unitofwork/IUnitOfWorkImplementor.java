package kr.debop4j.data.hibernate.unitofwork;

import org.hibernate.Session;

/**
 * kr.nsoft.data.hibernate.unitofwork.IUnitOfWorkImplementor
 *
 * @author sunghyouk.bae@gmail.com
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
