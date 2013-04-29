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

package kr.debop4j.search.hibernate.model;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * kr.debop4j.search.hibernate.model.Voc
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 29. 오후 3:47
 */
@Entity
@Table(name = "Voc")
@Indexed
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Voc extends AnnotatedEntityBase {

    private static final long serialVersionUID = -6113356114027050611L;

    @Id
    @GeneratedValue
    @Column(name = "VocId")
    @DocumentId
    private Long id;

    @Field
    @Boost(1.5f)
    @DateBridge(resolution = Resolution.SECOND)
    private Date createAt;

    @Field
    private String creator;

    @Field(store = Store.YES)
    @Analyzer(impl = KoreanAnalyzer.class)
    @Boost(2.0f)
    private String memo;

    @Field
    private String srtype;

    @IndexedEmbedded
    @OneToMany(cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<VocAttr> attrs = new HashSet<VocAttr>();

    public void addAttr(String name, String value) {
        VocAttr attr = new VocAttr(name, value);
        attrs.add(attr);
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(createAt, creator);
    }
}
