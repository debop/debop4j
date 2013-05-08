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

package kr.debop4j.data.hibernate.repository.impl;

import kr.debop4j.core.Guard;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link kr.debop4j.data.hibernate.repository.impl.HibernateRepository}를 생성해주는 Factory 입니다. 이것보다는 Spring framework의 @Repository를 사용하는 것이 좋다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 27.
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

//        try {
//            repository = (HibernateRepository<E>) Springs.getBean(repositoryKey);
//        } catch (Exception ignored) {}
//
//        if (repository == null) {
//            if (HibernateRepositoryFactory.log.isDebugEnabled())
//                HibernateRepositoryFactory.log.debug("HibernateRepository<{}> 인스턴스를 생성합니다.", entityClass.getName());
//
//            repository = new HibernateRepository<E>(entityClass);
//            Springs.registerSingletonBean(repositoryKey, repository);
//        }
//        return repository;

        String daoKey = getHibernateRepositoryKey(entityClass);
        IHibernateRepository<E> dao = (IHibernateRepository<E>) Local.get(daoKey);

        if (dao == null) {
            if (log.isDebugEnabled())
                log.debug("IHibernateRepository<{}> 인스턴스를 생성합니다.", entityClass.getName());

            dao = new HibernateRepository<E>(entityClass);
            Local.put(daoKey, dao);
        }

        return dao;
    }

    protected String getHibernateRepositoryKey(Class<?> entityClass) {
        return HIBERNATE_DAO_KEY + entityClass.getName();
    }
}
