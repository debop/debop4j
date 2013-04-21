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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

/**
 * kr.debop4j.data.model.AnnotatedTreeEntityBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 7.
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class AnnotatedTreeEntityBase<T extends ITreeEntity<T>> extends AnnotatedEntityBase implements ITreeEntity<T> {

    private static final long serialVersionUID = 7865247655329371282L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    private T parent;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL })
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
