package kr.debop4j.data.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * 트리 형태의 엔티티의 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 15.
 */
public abstract class TreeEntityBase<T extends ITreeEntity<T>, TId extends Serializable>
        extends EntityBase<TId> implements ITreeEntity<T> {

    private static final long serialVersionUID = 5383928955741762564L;

    /**
     * {@inheritDoc}
     */
    @Getter
    @Setter
    private T parent;

    /**
     * {@inheritDoc}
     */
    @Getter
    private Set<T> children = Sets.newLinkedHashSet();

    /**
     * {@inheritDoc}
     */
    @Getter
    @Setter
    private TreeNodePosition nodePosition = new TreeNodePosition();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("parent", parent)
                .add("nodePosition", nodePosition);
    }
}
