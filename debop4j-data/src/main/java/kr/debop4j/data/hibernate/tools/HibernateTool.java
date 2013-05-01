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

package kr.debop4j.data.hibernate.tools;

import com.google.common.collect.Maps;
import kr.debop4j.core.Guard;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.core.tools.SerializeTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.listener.UpdateTimestampedEventListener;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepositoryFactory;
import kr.debop4j.data.model.IStatefulEntity;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.ObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static kr.debop4j.core.Guard.firstNotNull;
import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * Hibernate 관련 Tool
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19
 */
public class HibernateTool {

    private static final Logger log = LoggerFactory.getLogger(HibernateTool.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final boolean isDebugEnabled = log.isDebugEnabled();


    private HibernateTool() { }

    public static SessionFactory buildSessionFactory(Configuration cfg) {
        assert cfg != null;
        if (log.isInfoEnabled())
            log.info("SessionFactory를 빌드합니다.");

        ServiceRegistryBuilder registryBuilder = new ServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory factory = cfg.buildSessionFactory(registryBuilder.buildServiceRegistry());

        if (log.isInfoEnabled())
            log.info("SessionFactory를 빌드했습니다.");

        return factory;
    }

    public static HibernateRepositoryFactory getHibernateDaoFactory() {
        // 이 작업이 가능하려면 Springs 로 Initialize 를 수행할 수 있도록 해 주어야 합니다.
        return Springs.getFirstBeanByType(HibernateRepositoryFactory.class);
    }

    public static <E extends IStatefulEntity> IHibernateRepository getHibernateDao(Class<E> entityClass) {
        return getHibernateDaoFactory().getOrCreateHibernateRepository(entityClass);
    }

    @SuppressWarnings("unchecked")
    public static void registerEventListener(SessionFactory sessionFactory, Object listener, EventType... eventTypes) {
        Guard.shouldNotBeNull(sessionFactory, "sessionFactory");
        Guard.shouldNotBeNull(listener, "listener");

        if (log.isDebugEnabled())
            log.debug("sessionFactory에 event listener를 등록합니다... listener=[{}], eventTypes=[{}]",
                      listener, StringTool.listToString(eventTypes));

        EventListenerRegistry registry =
                ((SessionFactoryImpl) sessionFactory)
                        .getServiceRegistry()
                        .getService(EventListenerRegistry.class);

        for (EventType eventType : eventTypes) {
            try {
                registry.getEventListenerGroup(eventType).appendListener(listener);
            } catch (Exception ignored) {}
        }

    }

    public static void registerUpdateTimestampEventListener(SessionFactory sessionFactory) {
        registerEventListener(sessionFactory,
                              new UpdateTimestampedEventListener(),
                              EventType.PRE_INSERT, EventType.PRE_UPDATE);
    }

    /** {@link HibernateParameter} 정보를 Name, Value 형태의 맵으로 변환합니다. */
    public static Map<String, Object> toMap(HibernateParameter... parameters) {
        Map<String, Object> map = Maps.newHashMap();
        for (HibernateParameter parameter : parameters) {
            map.put(parameter.getName(), parameter.getValue());
        }
        return map;
    }

    /** 지정된 수형에 대한 Detached Criteria 를 제공합니다. */
    public static <T extends IStatefulEntity> DetachedCriteria createDetachedCriteria(Class<T> clazz) {
        return DetachedCriteria.forClass(clazz);
    }

    /** 지정된 수형에 대한 Detached Criteria 를 제공합니다. */
    public static Criteria createCriteria(Class entityClass, Session session, Order[] orders, Criterion... criterions) {
        if (log.isDebugEnabled())
            log.debug("엔티티 [{}] 에 대한 Criteria를 생성합니다...", entityClass.getName());

        Criteria criteria = session.createCriteria(entityClass);
        addOrders(criteria, orders);
        return addCriterions(criteria, criterions);
    }

    /** {@link org.hibernate.criterion.DetachedCriteria} 를 복사합니다. */
    public static DetachedCriteria copyDetachedCriteria(DetachedCriteria dc) {
        shouldNotBeNull(dc, "dc");
        return (DetachedCriteria) SerializeTool.copyObject(dc);
    }

    public static Criteria copyCriteria(Criteria criteria) {
        shouldNotBeNull(criteria, "criteria");
        return (Criteria) SerializeTool.copyObject((CriteriaImpl) criteria);
    }

    /** {@link org.hibernate.criterion.DetachedCriteria} 를 현 {@link org.hibernate.Session} 에서 사용할 {@link org.hibernate.Criteria} 로 변환합니다. */
    public static Criteria getExecutableCriteria(Session session, DetachedCriteria dc) {
        return dc.getExecutableCriteria(session);
    }

    /** {@link org.hibernate.criterion.DetachedCriteria} 를 가지고 {@link org.hibernate.Criteria} 를 빌드합니다. */
    public static Criteria getExecutableCriteria(DetachedCriteria dc, Session session, Order... orders) {
        Guard.shouldNotBeNull(dc, "dc");
        Guard.shouldNotBeNull(session, "session");

        if (log.isDebugEnabled())
            log.debug("DetachedCriteria를 가지고 Criteria를 빌드합니다.");

        Criteria criteria = dc.getExecutableCriteria(session);

        for (Order order : orders) {
            criteria.addOrder(order);
        }
        return criteria;
    }

    public static DetachedCriteria addOrders(DetachedCriteria dc, Order... orders) {
        shouldNotBeNull(dc, "dc");

        for (Order order : orders) {
            dc.addOrder(order);
        }
        return dc;
    }

    public static DetachedCriteria addOrders(DetachedCriteria dc, Iterable<Order> orders) {
        shouldNotBeNull(dc, "dc");

        for (Order order : orders)
            dc.addOrder(order);

        return dc;
    }

    public static Criteria addOrders(Criteria criteria, Order... orders) {
        shouldNotBeNull(criteria, "criteria");

        for (Order order : orders) {
            criteria.addOrder(order);
        }
        return criteria;
    }

    public static Criteria addOrders(Criteria criteria, Iterable<Order> orders) {
        shouldNotBeNull(criteria, "criteria");

        for (Order order : orders) {
            criteria.addOrder(order);
        }
        return criteria;
    }

    /** {@link org.hibernate.Criteria} 에 {@link org.hibernate.criterion.Criterion} 들을 AND 로 추가합니다. */
    public static Criteria addCriterions(Criteria criteria, Criterion... criterions) {
        shouldNotBeNull(criteria, "criteria");

        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        return criteria;
    }

    /** {@link org.hibernate.Query} 의 인자에 값을 설정합니다. */
    public static Query setParameters(Query query, HibernateParameter... params) {
        Guard.shouldNotBeNull(query, "query");

        for (HibernateParameter param : params) {
            if (log.isDebugEnabled())
                log.debug("쿼리문의 인자값을 설정합니다. param=[{}]", param);

            query.setParameter(param.getName(),
                               param.getValue(),
                               firstNotNull(param.getType(), ObjectType.INSTANCE));
        }
        return query;
    }

    /** {@link org.hibernate.Criteria} 에 조회 범위를 지정합니다. */
    public static Criteria setPaging(Criteria criteria, Integer firstResult, Integer maxResults) {
        Guard.shouldNotBeNull(criteria, "criteria");

        if (log.isDebugEnabled())
            log.debug("criteria에 fetch range를 지정합니다. firstResult=[{}], maxResults=[{}]", firstResult, maxResults);

        if (firstResult != null && firstResult >= 0)
            criteria.setFirstResult(firstResult);

        if (maxResults != null && maxResults > 0)
            criteria.setMaxResults(maxResults);

        return criteria;
    }

    /** {@link org.hibernate.Query} 에 조회 범위를 지정합니다. */
    public static Query setPaging(Query query, Integer firstResult, Integer maxResults) {
        Guard.shouldNotBeNull(query, "query");

        if (log.isDebugEnabled())
            log.debug("query에 fetch range를 지정합니다. firstResult=[{}], maxResults=[{}]", firstResult, maxResults);

        if (firstResult != null && firstResult >= 0)
            query.setFirstResult(firstResult);

        if (maxResults != null && maxResults > 0)
            query.setMaxResults(maxResults);

        return query;
    }
}
