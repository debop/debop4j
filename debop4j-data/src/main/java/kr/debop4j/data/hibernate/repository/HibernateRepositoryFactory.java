package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.Guard;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link HibernateRepository}를 생성해주는 Factory 입니다. 이것보다는 Spring framework의 @Repository를 사용하는 것이 좋다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 27.
 */
@Component
@Slf4j
public class HibernateRepositoryFactory implements IHibernateRepositoryFactory {

    private static final String HIBERNATE_DAO_KEY = "kr.debop4j.data.hibernate.repository.IHibernateRepository.";

    public <E extends IStatefulEntity> IHibernateRepository<E> getOrCreateHibernateRepository(Class<E> entityClass) {
        Guard.shouldNotBeNull(entityClass, "entityClass");
        return getOrCreateHibernateRepositoryInternal(entityClass);
    }

    @SuppressWarnings("unchecked")
    protected synchronized <E extends IStatefulEntity> IHibernateRepository<E> getOrCreateHibernateRepositoryInternal(Class<E> entityClass) {

        String repositoryKey = getHibernateRepositoryKey(entityClass);
        HibernateRepository<E> repository = null;
        try {
            repository = (HibernateRepository<E>) Springs.getBean(repositoryKey);
        } catch (Exception ignored) {}

        if (repository == null) {
            if (log.isDebugEnabled())
                log.debug("HibernateRepository<{}> 인스턴스를 생성합니다.", entityClass.getName());

            repository = new HibernateRepository<E>(entityClass);
            Springs.registerSingletonBean(repositoryKey, repository);
        }
        return repository;

//        String daoKey = getHibernateRepositoryKey(entityClass);
//        HibernateRepository<E> dao = (HibernateRepository<E>) Local.get(daoKey);
//
//        if (dao == null) {
//            if (log.isDebugEnabled())
//                log.debug("IHibernateRepository<{}> 인스턴스를 생성합니다.", entityClass.getName());
//
//            dao = new HibernateRepository<E>(entityClass);
//            Local.put(daoKey, dao);
//        }
//
//        return dao;
    }

    protected String getHibernateRepositoryKey(Class<?> entityClass) {
        return HIBERNATE_DAO_KEY + entityClass.getName();
    }
}
