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

package kr.debop4j.data.mongodb.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Indexed
@Getter
@Setter
@Analyzer(impl = CJKAnalyzer.class)
public class Voc extends UuidEntityBase {

    private static final long serialVersionUID = -6113356114027050611L;

    public static final String DefaultMemo =
            "1. 동해 물과 백두산이 마르고 닳도록, 하느님이 보우하사 우리나라 만세." +
                    "   (후렴) 무궁화 삼천리 화려 강산, 대한 사람 대한으로 길이 보전하세." +
                    "2. 남산 위에 저 소나무 철갑을 두른 듯, 바람 서리 불변함은 우리 기상일세." +
                    "3. 가을 하늘 공활한데 높고 구름 없이, 밝은 달은 우리 가슴 일편단심일세." +
                    "4. 이 기상과 이 맘으로 충성을 다하여, 괴로우나 즐거우나 나라 사랑하세.";

    public Voc() {}

    public Voc(String creator) {
        this.creator = creator;
    }

//    @Id
//    @GeneratedValue
//    @DocumentId
//    private Long id;

    @Field
    @Boost(1.5f)
    @DateBridge(resolution = Resolution.SECOND)
    private Date createAt = new Date();

    @Field
    private String creator;

    @Field
    @Boost(2.0f)
    @Analyzer(impl = KoreanAnalyzer.class)
    private String memo = DefaultMemo;

    @Field
    private String srtype;

    @IndexedEmbedded
    @ElementCollection(targetClass = VocAttr.class, fetch = FetchType.EAGER)
    private Set<VocAttr> attrs = new HashSet<VocAttr>();

    public void addAttr(String name, String value) {
        VocAttr attr = new VocAttr(name, value);
        attrs.add(attr);
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(createAt, creator);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("createAt", createAt)
                .add("creator", creator);

    }
}
