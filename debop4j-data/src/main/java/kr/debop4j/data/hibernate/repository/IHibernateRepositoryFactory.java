package kr.debop4j.data.hibernate.repository;

import kr.debop4j.data.model.IStatefulEntity;

/**
 * {@link IHibernateRepository} 를 생성하는 factory 클래스의 인터패이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 18
 */
public interface IHibernateRepositoryFactory {

    <E extends IStatefulEntity> IHibernateRepository<E> getOrCreateHibernateRepository(Class<E> entityClass);
}
