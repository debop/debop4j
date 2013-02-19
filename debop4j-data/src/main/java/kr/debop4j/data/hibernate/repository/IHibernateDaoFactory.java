package kr.debop4j.data.hibernate.repository;

import kr.debop4j.data.model.IStatefulEntity;

/**
 * 설명을 추가하세요.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 18
 */
public interface IHibernateDaoFactory {

    <E extends IStatefulEntity> IHibernateDao<E> getOrCreateHibernateDao(Class<E> entityClass);
}
