package kr.debop4j.data.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 지역화정보, 메타정보를 가지는 Tree 구조의 엔티티를 표현합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 19
 */
@MappedSuperclass
public abstract class LocaleMetaTreeEntityBase<T extends IEntity<TId> & ITreeEntity<T>,
        TId extends Serializable,
        TLocaleValue extends ILocaleValue>
        extends LocaleMetaEntityBase<TId, TLocaleValue> implements ITreeEntity<T> {

    private static final long serialVersionUID = -4521048731750418059L;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "parentId")
    private T parent;

    @Getter
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<T> children = Sets.newLinkedHashSet();

    @Getter
    @Setter
    @Embedded
    @AttributeOverrides({
                                @AttributeOverride(name = "level", column = @Column(name = "TREE_LEVEL")),
                                @AttributeOverride(name = "order", column = @Column(name = "TREE_ORDER")),
                        })
    private TreeNodePosition nodePosition = new TreeNodePosition();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("parent", parent)
                .add("nodePosition", nodePosition);
    }
}
