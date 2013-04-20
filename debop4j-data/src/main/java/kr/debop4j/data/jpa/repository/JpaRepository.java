package kr.debop4j.data.jpa.repository;

import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.ReflectTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * JPA 에서 사용할 Repository 입니다.
 * {@ref http://www.baeldung.com/2011/12/13/the-persistencexml-layer-with-spring-3-1-and-annotated/}
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 25.
 */
@Repository
@Slf4j
public class JpaRepository<E extends JpaEntityBase> {

    @PersistenceContext
    private EntityManager entityManager;

    @Getter
    private Class<E> entityType;

    public JpaRepository() {
        entityType = ReflectTool.getGenericParameterType(this);
        if (log.isDebugEnabled())
            log.debug("JpaRepository<{}> 를 생성했습니다.", entityType.getName());
    }

    public void setEntityManager(EntityManager entityManager) {
        Guard.shouldNotBeNull(entityManager, "entityManager");
        if (log.isDebugEnabled())
            log.debug("EntityManager 설정.");

        this.entityManager = entityManager;
    }

    public E get(Serializable id) {
        return entityManager.getReference(entityType, id);
    }

    public E find(Serializable id) {
        return entityManager.find(entityType, id);
    }

}
