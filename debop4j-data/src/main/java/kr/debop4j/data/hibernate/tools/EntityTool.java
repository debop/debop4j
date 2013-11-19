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

import com.google.common.collect.Sets;
import kr.debop4j.core.Function1;
import kr.debop4j.core.IDataObject;
import kr.debop4j.core.json.GsonSerializer;
import kr.debop4j.core.json.IJsonSerializer;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.core.tools.MapperTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.repository.IHibernateDao;
import kr.debop4j.data.hibernate.repository.impl.HibernateDao;
import kr.debop4j.data.model.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LocaleType;
import org.hibernate.type.ObjectType;
import org.hibernate.type.StringType;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;


/**
 * Hibernate 엔티티 정보를 관리하기 위한 Utility Class 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 24
 */
@Slf4j
@SuppressWarnings("unchecked")
public class EntityTool {

    private EntityTool() { }

    /**
     * The constant PROPERTY_ANCESTORS.
     */
    public static final String PROPERTY_ANCESTORS = "ancestors";
    /**
     * The constant PROPERTY_DESCENDENTS.
     */
    public static final String PROPERTY_DESCENDENTS = "descendents";


    @Getter(lazy = true)
    private static final IJsonSerializer gsonSerializer = new GsonSerializer();


    /**
     * Entity to string.
     *
     * @param entity the entity
     * @return the string
     */
    public static String entityToString(IDataObject entity) {
        return (entity != null) ? StringTool.objectToString(entity) : StringTool.NULL_STR;
    }

    /**
     * As gson text.
     *
     * @param entity the entity
     * @return the string
     * @throws Exception the exception
     */
    public static String asGsonText(IDataObject entity) throws Exception {
        return getGsonSerializer().serializeToText(entity);
    }

    // region << Hierarchy >>

    /**
     * Assert not circular hierarchy.
     *
     * @param child  the child
     * @param parent the parent
     */
    public static <T extends IHierarchyEntity<T>> void assertNotCircularHierarchy(T child, T parent) {
        if (child == parent)
            throw new IllegalArgumentException("Child and Parent are same.");

        if (child.getDescendents().contains(parent))
            throw new IllegalArgumentException("child 가 parent를 이미 자손으로 가지고 있습니다.");

        if (Sets.intersection(parent.getAncestors(), child.getDescendents()).size() > 0)
            throw new IllegalArgumentException("parent의 조상과 child의 조상이 같은 넘이 있으면 안됩니다.");
    }

    /**
     * Sets hierarchy.
     *
     * @param child     the child
     * @param oldParent the old parent
     * @param newParent the new parent
     */
    public static <T extends IHierarchyEntity<T>> void setHierarchy(T child, T oldParent, T newParent) {
        shouldNotBeNull(child, "child");

        if (log.isTraceEnabled())
            log.trace("현재 노드의 부모를 변경하고, 계층구조 정보를 변경합니다... child=[{}], oldParent=[{}], newParent=[{}]",
                      child, oldParent, newParent);

        if (oldParent != null)
            removeHierarchy(child, oldParent);

        if (newParent != null)
            setHierarchy(child, newParent);
    }

    /**
     * Sets hierarchy.
     *
     * @param child  the child
     * @param parent the parent
     */
    public static <T extends IHierarchyEntity<T>> void setHierarchy(T child, T parent) {
        if (parent == null || child == null)
            return;

        if (log.isTraceEnabled())
            log.trace("노드의 부모 및 조상을 설정합니다. child=[{}], parent=[{}]", child, parent);

        parent.getDescendents().add(child);
        parent.getDescendents().addAll(child.getDescendents());

        for (T ancestor : parent.getAncestors()) {
            ancestor.getDescendents().add(child);
            ancestor.getDescendents().addAll(child.getDescendents());
        }

        child.getAncestors().add(parent);
        child.getAncestors().addAll(parent.getAncestors());
    }

    /**
     * Remove hierarchy.
     *
     * @param child  the child
     * @param parent the parent
     */
    public static <T extends IHierarchyEntity<T>> void removeHierarchy(T child, T parent) {
        if (parent == null)
            return;

        shouldNotBeNull(child, "child");
        if (log.isTraceEnabled())
            log.trace("노드의 부모 및 조상을 제거합니다. child=[{}], parent=[{}]", child, parent);


        child.getAncestors().remove(parent);
        child.getAncestors().removeAll(parent.getAncestors());

        for (T ancestor : parent.getAncestors()) {
            ancestor.getDescendents().remove(child);
            ancestor.getDescendents().removeAll(child.getDescendents());
        }
        for (T descendent : child.getDescendents()) {
            descendent.getAncestors().remove(parent);
            descendent.getAncestors().removeAll(parent.getAncestors());
        }
    }

    /**
     * Get ancestors criteria.
     *
     * @param entity      the entity
     * @param session     the session
     * @param entityClass the entity class
     * @return the detached criteria
     */
    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria getAncestorsCriteria(T entity, Session session, Class<T> entityClass) {
        return DetachedCriteria
                .forClass(entityClass)
                .createAlias(PROPERTY_DESCENDENTS, "des")
                .add(Restrictions.eq("des.id", entity.getId()));
    }

    /**
     * Get descendents criteria.
     *
     * @param entity      the entity
     * @param session     the session
     * @param entityClass the entity class
     * @return the detached criteria
     */
    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria getDescendentsCriteria(T entity, Session session, Class<T> entityClass) {
        return DetachedCriteria.forClass(entityClass)
                .createAlias(PROPERTY_ANCESTORS, "ans")
                .add(Restrictions.eq("ans.id", entity.getId()));
    }

    /**
     * Get ancestors id criteria.
     *
     * @param entity      the entity
     * @param session     the session
     * @param entityClass the entity class
     * @return the detached criteria
     */
    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria getAncestorsIdCriteria(T entity, Session session, Class<T> entityClass) {
        return getAncestorsCriteria(entity, session, entityClass)
                .setProjection(Projections.distinct(Projections.id()));
    }

    /**
     * Get descendents id criteria.
     *
     * @param entity      the entity
     * @param session     the session
     * @param entityClass the entity class
     * @return the detached criteria
     */
    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria getDescendentsIdCriteria(T entity, Session session, Class<T> entityClass) {
        return getDescendentsCriteria(entity, session, entityClass)
                .setProjection(Projections.distinct(Projections.id()));
    }

    // endregion

    // region << ILocaleEntity >>

    /**
     * 특정 로케일 키를 가지는 엔티티를 조회하는 HQL 문.
     */
    public static final String GET_LIST_BY_LOCALE_KEY =
            "select distinct loen from %s loen where :key in indices (loen.localeMap)";

    /**
     * 특정 로케일 속성값에 따른 엔티티를 조회하는 HQL 문.
     */
    public static final String GET_LIST_BY_LOCALE_PROPERTY =
            "select distinct loen from %s loen join loen.localeMap locale where locale.%s = :%s";

    /**
     * Copy locale.
     *
     * @param source      the source
     * @param destination the destination
     */
    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue> void CopyLocale(T source, T destination) {
        for (Locale locale : source.getLocales())
            destination.addLocaleValue(locale, source.getLocaleValue(locale));
    }

    /**
     * Contains locale.
     *
     * @param entityClass the entity class
     * @param locale      the locale
     * @return the list
     */
    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue>
    List<T> containsLocale(Class<T> entityClass, Locale locale) {

        String hql = String.format(GET_LIST_BY_LOCALE_KEY, entityClass.getName());
        if (log.isDebugEnabled())
            log.debug("IEntity [{}] 의 Locale[{}]를 가지는 엔티티 조회 hql=[{}]",
                      entityClass.getName(), locale, hql);

        IHibernateDao dao = new HibernateDao();
        return dao.find(entityClass, hql, new HibernateParameter("key", locale, LocaleType.INSTANCE));

    }

    /**
     * Contains locale.
     *
     * @param entityClass  the entity class
     * @param propertyName the property name
     * @param value        the value
     * @param type         the type
     * @return the list
     */
    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue>
    List<T> containsLocale(final Class<T> entityClass,
                           final String propertyName,
                           final Object value,
                           final org.hibernate.type.Type type) {

        final String hql = String.format(GET_LIST_BY_LOCALE_PROPERTY, entityClass.getName(), propertyName, propertyName);
        if (log.isDebugEnabled())
            log.debug("IEntity [{}] 에 Locale 속성[{}]의 값이 [{}] 인 엔티티를 조회합니다. hql=[{}]",
                      entityClass.getName(), propertyName, value, hql);

        IHibernateDao dao = new HibernateDao();
        return dao.find(entityClass, hql, new HibernateParameter(propertyName, value, ObjectType.INSTANCE));
    }

    // endregion


    // region << IMetaEntity >>

    private static final String GET_LIST_BY_META_KEY =
            "select distinct me from %s me where :key in indices(me.metaMap)";
    public static final String GET_LIST_BY_META_VALUE =
            "select distinct me from %s me join me.metaMap meta where meta.value = :value";

    /**
     * Contains meta key.
     *
     * @param entityClass the entity class
     * @param key         the key
     * @return the list
     */
    public static <T extends IMetaEntity> List<T> containsMetaKey(Class<T> entityClass, String key) {
        shouldNotBeWhiteSpace(key, "key");

        String hql = String.format(GET_LIST_BY_META_KEY, entityClass.getName());

        if (log.isDebugEnabled())
            log.debug("엔티티 [{}]의 메타데이타 키 [{}] 를 가지는 엔티티 조회 hql=[{}]", entityClass.getName(), key, hql);

        IHibernateDao dao = new HibernateDao();
        return dao.find(entityClass, hql, new HibernateParameter("key", key, StringType.INSTANCE));
    }

    /**
     * Contains meta value.
     *
     * @param entityClass the entity class
     * @param value       the value
     * @return the list
     */
    public static <T extends IMetaEntity> List<T> containsMetaValue(final Class<T> entityClass, final String value) {
        shouldNotBeWhiteSpace(value, "value");

        String hql = String.format(GET_LIST_BY_META_VALUE, entityClass.getName());
        if (log.isDebugEnabled())
            log.debug("메타데이타 value[{}]를 가지는 엔티티 조회 hql=[{}]", value, hql);

        IHibernateDao dao = new HibernateDao();
        return dao.find(entityClass, hql, new HibernateParameter("value", value, StringType.INSTANCE));
    }

    // endregion

    // region << IEntity Mapper >>

    /**
     * 원본 엔티티의 속성정보를 대상 엔티티의 속성정보로 매핑시킵니다.
     *
     * @param source the source
     * @param target the target
     * @return target entity
     */
    public static <S, T> T mapEntity(S source, T target) {
        MapperTool.map(source, target);
        return target;
    }

    /**
     * Map entity.
     *
     * @param source      the source
     * @param targetClass the target class
     * @return target entity
     */
    public static <S, T> T mapEntity(S source, Class<T> targetClass) {
        shouldNotBeNull(source, "source");
        return MapperTool.map(source, targetClass);
    }

    /**
     * 원본 엔티티를 대상 엔티티로 매핑을 수행합니다. {@link kr.debop4j.core.tools.MapperTool} 을 사용합니다.
     *
     * @param sources the sources
     * @param targets the targets
     * @return the list
     */
    public static <S, T> List<T> mapEntities(List<S> sources, List<T> targets) {
        shouldNotBeNull(sources, "sources");
        shouldNotBeNull(targets, "targets");

        int size = Math.min(sources.size(), targets.size());

        for (int i = 0; i < size; i++) {
            MapperTool.map(sources.get(i), targets.get(i));
        }
        return targets;
    }

    /**
     * 병렬 방식으로 원본으로부터 대상엔티티로 매핑합니다. 대용량 정보의 DTO 생성 시 유리합니다.
     *
     * @param sources     the sources
     * @param targetClass the target class
     * @return the list
     */
    public static <S, T> List<T> mapEntitiesAsParallel(final List<S> sources,
                                                       final Class<T> targetClass) {
        if (sources == null || sources.size() == 0)
            return new ArrayList<>();

        return Parallels.runEach(sources, new Function1<S, T>() {
            @Override
            public T execute(@Nullable S input) {
                return MapperTool.map(input, targetClass);
            }
        });
    }

    // endregion

    // region << TreeNode >>

    /**
     * Update tree node position.
     *
     * @param entity the entity
     */
    public static <T extends ITreeEntity<T>> void updateTreeNodePosition(T entity) {
        shouldNotBeNull(entity, "entity");

        if (log.isTraceEnabled())
            log.trace("update tree node position... entity=[{}]", entity);

        TreeNodePosition nodePosition = entity.getNodePosition();

        if (entity.getParent() != null) {
            nodePosition.setLevel(entity.getParent().getNodePosition().getLevel() + 1);
            if (!entity.getParent().getChildren().contains(entity))
                nodePosition.setOrder(entity.getParent().getChildren().size());
        } else {
            nodePosition.setLevel(0);
            nodePosition.setOrder(0);
        }
    }

    /**
     * 트리구조를 가지는 엔티티의 자식 수를 계산합니다.
     *
     * @param entity the entity
     * @return the child count
     */
    public static <T extends ITreeEntity<T>> Long getChildCount(T entity) {
        if (log.isTraceEnabled())
            log.trace("tree entity의 자식 엔티티의 수를 구합니다. entity=[{}]", entity);

        DetachedCriteria dc = HibernateTool.createDetachedCriteria(entity.getClass());
        dc.add(Restrictions.eq("parent", entity));

        IHibernateDao dao = new HibernateDao();
        return dao.count(entity.getClass(), dc);
    }

    /**
     * Has children.
     *
     * @param entity the entity
     * @return the boolean
     */
    public static <T extends ITreeEntity<T>> Boolean hasChildren(T entity) {
        if (log.isTraceEnabled())
            log.trace("tree entity 가 자식을 가지는지 확안합니다. entity=[{}]", entity);

        DetachedCriteria dc = HibernateTool.createDetachedCriteria(entity.getClass());
        dc.add(Restrictions.eq("parent", entity));

        IHibernateDao dao = new HibernateDao();
        return dao.exists(entity.getClass(), dc);
    }


    // endregion
}
