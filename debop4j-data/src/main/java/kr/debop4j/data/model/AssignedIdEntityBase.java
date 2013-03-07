package kr.debop4j.data.model;

import com.google.common.base.Objects;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 엔티티의 Identifier generator가 assigned 인 경우에 사용합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 27.
 */
@MappedSuperclass
public class AssignedIdEntityBase<TId extends Serializable> extends EntityBase<TId> implements IAssignedEntity<TId> {
    private static final long serialVersionUID = 2441448634046269391L;

    protected AssignedIdEntityBase() {}

    protected AssignedIdEntityBase(TId assignedId) {
        super.setId(assignedId);
    }

    /**
     * 엔티티의 Id 를 설정합니다.
     *
     * @param newId 새로운 Id 값
     */
    @Override
    public void setId(TId newId) {
        super.setId(newId);
    }

    /**
     * Entity의 HashCode 를 제공합니다. 저장소에 저장된 엔티티의 경우는 Identifier 의 HashCode를 제공합니다.
     *
     * @return hash code
     */
    public int hashCode() {
        return (getId() != null) ? Objects.hashCode(getId()) : System.identityHashCode(this);
    }
}
