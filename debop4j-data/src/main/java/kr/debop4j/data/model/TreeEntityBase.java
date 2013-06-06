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

package kr.debop4j.data.model;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 트리 형태의 엔티티의 기본 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 15.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class TreeEntityBase<T extends ITreeEntity<T>, TId extends Serializable>
        extends EntityBase<TId> implements ITreeEntity<T> {

    private static final long serialVersionUID = 5383928955741762564L;

    /** 부모 엔티티 */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "ParentId" )
    private T parent;

    /** 자식 엔티티 컬렉션입니다. */
    @Setter( AccessLevel.PROTECTED )
    @OneToMany( mappedBy = "parent", cascade = { CascadeType.ALL } )
    @LazyCollection( LazyCollectionOption.EXTRA )
    private Set<T> children = Sets.newLinkedHashSet();

    /** 엔티티의 트리상의 위치를 나타냅니다. */
    @Embedded
    private TreeNodePosition nodePosition = new TreeNodePosition();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("parent", parent)
                .add("nodePosition", nodePosition);
    }
}
