package kr.debop4j.data.hibernate.unitofwork;

/**
 * {@link IUnitOfWork} 생성 옵션
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 29.
 */
public enum UnitOfWorkNestingOptions {

    /**
     * 기존에 {@link IUnitOfWork} 가 있다면, 그것을 사용하고, 아니면 새로운 UnitOfWork를 생성한다.
     */
    ReturnExistingOrCreateUnitOfWork(0),
    /**
     * 새로운 {@link IUnitOfWork} 인스턴스를 생성하고, 기존에 IUnitOfWork 인스턴스가 있다면, 새로 생성된 UnitOfWork를 중첩한다.
     */
    CreateNewOrNestUnitOfWork(1);

    private final int option;

    UnitOfWorkNestingOptions(int option) {
        this.option = option;
    }
}
