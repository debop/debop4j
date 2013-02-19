package kr.debop4j.data.hibernate.repository;

import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HibernateRepository를 생성해주는 Factory 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 27.
 */
@Slf4j
@Component
public class HibernateDaoFactory {

    private static final String HIBERNATE_DAO_KEY = "kr.debop4j.data.hibernate.dao.IHibernateDao";

    public <E extends IStatefulEntity> IHibernateDao<E> getOrCreateHibernateDao(Class<E> entityClass) {
        Guard.shouldNotBeNull(entityClass, "entityClass");
        return getOrCreateHibernateDaoInternal(entityClass);
    }

    @SuppressWarnings("unchecked")
    protected synchronized <E extends IStatefulEntity> IHibernateDao<E> getOrCreateHibernateDaoInternal(Class<E> entityClass) {

        String daoKey = getHibernateDaoKey(entityClass);
        HibernateDaoImpl<E> dao = (HibernateDaoImpl<E>) Local.get(daoKey);

        if (dao == null) {
            if (log.isDebugEnabled())
                log.debug("IHibernateDao<{}> 인스턴스를 생성합니다.", entityClass.getName());

            dao = new HibernateDaoImpl<E>(entityClass);
            Local.put(daoKey, dao);
        }

        return dao;
    }

    protected String getHibernateDaoKey(Class<?> entityClass) {
        return HIBERNATE_DAO_KEY + "." + entityClass.getName();
    }
}
