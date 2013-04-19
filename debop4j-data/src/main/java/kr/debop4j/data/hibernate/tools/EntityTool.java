package kr.debop4j.data.hibernate.tools;

import com.google.common.collect.Sets;
import kr.debop4j.core.Function1;
import kr.debop4j.core.Guard;
import kr.debop4j.core.IDataObject;
import kr.debop4j.core.json.GsonSerializer;
import kr.debop4j.core.json.IJsonSerializer;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.core.tools.MapperTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
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


/**
 * Hibernate 엔티티 정보를 관리하기 위한 Utility Class 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 24
 */
@Slf4j
@SuppressWarnings("unchecked")
public class EntityTool {

    private EntityTool() {
    }

    public static final String PROPERTY_ANCESTORS = "ancestors";
    public static final String PROPERTY_DESCENDENTS = "descendents";


    @Getter(lazy = true)
    private static final IJsonSerializer gsonSerializer = new GsonSerializer();


    public static String entityToString(IDataObject entity) {
        return (entity != null) ? StringTool.objectToString(entity) : StringTool.NULL_STR;
    }

    public static String asGsonText(IDataObject entity) throws Exception {
        return getGsonSerializer().serializeToText(entity);
    }

    // region << Hierarchy >>

    public static <T extends IHierarchyEntity<T>> void assertNotCircularHierarchy(T child, T parent) {
        if (child == parent)
            throw new IllegalArgumentException("Child and Paremt are same.");

        if (child.getDescendents().contains(parent))
            throw new IllegalArgumentException("child 가 parent를 이미 자손으로 가지고 있습니다.");

        if (Sets.intersection(parent.getAncestors(), child.getDescendents()).size() > 0)
            throw new IllegalArgumentException("parent의 조상과 child의 조상이 같은 넘이 있으면 안됩니다.");
    }

    public static <T extends IHierarchyEntity<T>> void setHierarchy(T child, T oldParent, T newParent) {
        Guard.shouldNotBeNull(child, "child");

        if (log.isDebugEnabled())
            log.debug("현재 노드의 부모를 변경하고, 계층구조 정보를 변경합니다... child=[{}], oldParent=[{}], newParent=[{}]",
                      child, oldParent, newParent);

        if (oldParent != null)
            removeHierarchy(child, oldParent);

        if (newParent != null)
            setHierarchy(child, newParent);
    }

    public static <T extends IHierarchyEntity<T>> void setHierarchy(T child, T parent) {
        if (parent == null || child == null)
            return;

        if (log.isDebugEnabled())
            log.debug("노드의 부모 및 조상을 설정합니다. child=[{}], parent=[{}]", child, parent);

        parent.getDescendents().add(child);
        parent.getDescendents().addAll(child.getDescendents());

        for (T ancestor : parent.getAncestors()) {
            ancestor.getDescendents().add(child);
            ancestor.getDescendents().addAll(child.getDescendents());
        }

        child.getAncestors().add(parent);
        child.getAncestors().addAll(parent.getAncestors());
    }

    public static <T extends IHierarchyEntity<T>> void removeHierarchy(T child, T parent) {
        if (parent == null)
            return;

        Guard.shouldNotBeNull(child, "child");

        if (log.isDebugEnabled())
            log.debug("노드의 부모 및 조상을 제거합니다. child=[{}], parent=[{}]", child, parent);


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

    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria GetAncestorsCriteria(T entity, Session session, Class<T> entityClass) {
        return
                DetachedCriteria
                        .forClass(entityClass)
                        .createAlias(PROPERTY_DESCENDENTS, "des")
                        .add(Restrictions.eq("des.id", entity.getId()));
    }

    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria GetDescendentsCriteria(T entity, Session session, Class<T> entityClass) {
        return
                DetachedCriteria.forClass(entityClass)
                        .createAlias(PROPERTY_ANCESTORS, "ans")
                        .add(Restrictions.eq("ans.id", entity.getId()));
    }

    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria GetAncestorsIdCriteria(T entity, Session session, Class<T> entityClass) {
        return
                GetAncestorsCriteria(entity, session, entityClass)
                        .setProjection(Projections.distinct(Projections.id()));
    }

    public static <T extends IHierarchyEntity<T> & IEntity<TId>, TId extends Serializable>
    DetachedCriteria GetDescendentsIdCriteria(T entity, Session session, Class<T> entityClass) {
        return
                GetDescendentsCriteria(entity, session, entityClass)
                        .setProjection(Projections.distinct(Projections.id()));
    }

    // endregion

    // region << ILocaleEntity >>

    final static String GET_LIST_BY_LOCALE_KEY =
            "select distinct loen from %s loen where :key in indices (loen.localeMap)";

    final static String GET_LIST_BY_LOCALE_PROPERTY =
            "select distinct loen from %s loen join loen.localeMap locale where locale.%s = :%s";

    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue>
    void CopyLocale(T source, T destination) {
        for (Locale locale : source.getLocales())
            destination.addLocaleValue(locale, source.getLocaleValue(locale));
    }

    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue>
    List<T> containsLocale(Class<T> entityClass, Locale locale) {

        String hql = String.format(GET_LIST_BY_LOCALE_KEY, entityClass.getName());
        if (log.isDebugEnabled())
            log.debug("IEntity [{}] 의 Locale[{}]를 가지는 엔티티 조회 hql=[{}]",
                      entityClass.getName(), locale, hql);

        IHibernateRepository<T> dao = HibernateTool.getHibernateDao(entityClass);
        return dao.findByHql(hql, new HibernateParameter("key", locale, LocaleType.INSTANCE));

    }

    public static <T extends ILocaleEntity<TLocaleValue>, TLocaleValue extends ILocaleValue>
    List<T> containsLocale(Class<T> entityClass,
                           String propertyName,
                           Object value,
                           org.hibernate.type.Type type) {

        String hql = String.format(GET_LIST_BY_LOCALE_PROPERTY, entityClass.getName(), propertyName, propertyName);
        if (log.isDebugEnabled())
            log.debug("IEntity [{}] 에 Locale 속성[{}]의 값이 [{}] 인 엔티티를 조회합니다. hql=[{}]",
                      entityClass.getName(), propertyName, value, hql);

        IHibernateRepository<T> dao = HibernateTool.getHibernateDao(entityClass);
        return dao.findByHql(hql, new HibernateParameter(propertyName, value, ObjectType.INSTANCE));
    }

    // endregion


    // region << IMetaEntity >>

    static final String GET_LIST_BY_META_KEY =
            "select distinct me from %s me where :key in indices(me.metaMap)";
    static final String GET_LIST_BY_META_VALUE =
            "select distinct me from %s me join me.metaMap meta where meta.value = :value";

    public static <T extends IMetaEntity> List<T> containsMetaKey(Class<T> entityClass, String key) {
        Guard.shouldNotBeWhiteSpace(key, "key");

        String hql = String.format(GET_LIST_BY_META_KEY, entityClass.getName());

        if (log.isDebugEnabled())
            log.debug("엔티티 [{}]의 메타데이타 키 [{}] 를 가지는 엔티티 조회 hql=[{}]", entityClass.getName(), key, hql);

        IHibernateRepository<T> dao = HibernateTool.getHibernateDao(entityClass);
        return dao.findByHql(hql, new HibernateParameter("key", key, StringType.INSTANCE));
    }

    public static <T extends IMetaEntity> List<T> containsMetaValue(Class<T> entityClass, String value) {
        Guard.shouldNotBeWhiteSpace(value, "value");

        String hql = String.format(GET_LIST_BY_META_VALUE, entityClass.getName());
        if (log.isDebugEnabled())
            log.debug("메타데이타 value[{}]를 가지는 엔티티 조회 hql=[{}]", value, hql);

        IHibernateRepository<T> dao = HibernateTool.getHibernateDao(entityClass);
        return dao.findByHql(hql, new HibernateParameter("value", value, StringType.INSTANCE));
    }

    // endregion

    // region << IEntity Mapper >>

    /**
     * 원본 엔티티의 속성정보를 대상 엔티티의 속성정보로 매핑시킵니다.
     */
    public static <S, T> T mapEntity(S source, T target) {
        MapperTool.map(source, target);
        return target;
    }

    public static <S, T> T mapEntity(S source, Class<T> targetClass) {
        Guard.shouldNotBeNull(source, "source");
        return MapperTool.map(source, targetClass);
    }

    /**
     * 원본 엔티티를 대상 엔티티로 매핑을 수행합니다. {@link kr.debop4j.core.tools.MapperTool} 을 사용합니다.
     */
    public static <S, T> List<T> mapEntities(List<S> sources, List<T> targets) {
        Guard.shouldNotBeNull(sources, "sources");
        Guard.shouldNotBeNull(targets, "targets");

        int size = Math.min(sources.size(), targets.size());

        for (int i = 0; i < size; i++) {
            MapperTool.map(sources.get(i), targets.get(i));
        }
        return targets;
    }

    /**
     * 병렬 방식으로 원본으로부터 대상엔티티로 매핑합니다. 대용량 정보의 DTO 생성 시 유리합니다.
     */
    public static <S, T> List<T> mapEntitiesAsParallel(final List<S> sources,
                                                       final Class<T> targetClass) {
        if (sources == null || sources.size() == 0)
            return new ArrayList<>();

        return
                Parallels.runEach(sources, new Function1<S, T>() {
                    @Override
                    public T execute(@Nullable S input) {
                        return MapperTool.map(input, targetClass);
                    }
                });
    }

    // endregion

    // region << TreeNode >>

    public static <T extends ITreeEntity<T>> void updateTreeNodePosition(T entity) {
        Guard.shouldNotBeNull(entity, "entity");

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

    public static <T extends ITreeEntity<T>> Long getChildCount(T entity) {
        if (log.isDebugEnabled())
            log.debug("tree entity의 자식 엔티티의 수를 구합니다. entity=[{}]", entity);

        DetachedCriteria criteria = HibernateTool.createDetachedCriteria(entity.getClass());
        criteria.add(Restrictions.eq("parent", entity));

//		HibernateRepository<T> dao = (HibernateRepository<T>) HbRepositoryFactory.get(entity.getClass());
//		return dao.countByCriteria(criteria);
        // TODO: 구현 중
        return null;
    }

    public static <T extends ITreeEntity<T>> Boolean hasChildren(T entity) {
        if (log.isDebugEnabled())
            log.debug("tree entity 가 자식을 가지는지 확안합니다. entity=[{}]", entity);

        DetachedCriteria criteria = HibernateTool.createDetachedCriteria(entity.getClass());
        criteria.add(Restrictions.eq("parent", entity));

//		HibernateRepository<T> dao = (HibernateRepository<T>) HbRepositoryFactory.get(entity.getClass());
//		return dao.exists(criteria);
        // TODO: 구현 중
        return null;
    }


    // endregion
}
