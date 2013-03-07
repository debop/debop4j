package kr.debop4j.data.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

/**
 * kr.debop4j.data.model.AnnotatedTreeEntityBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AnnotatedTreeEntityBase<T extends ITreeEntity<T>> extends AnnotatedEntityBase implements ITreeEntity<T> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    private T parent;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<T> children = Sets.newLinkedHashSet();

    @Embedded
    private TreeNodePosition nodePosition = new TreeNodePosition();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("parent", parent)
                .add("nodePosition", nodePosition);
    }
}
