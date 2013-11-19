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

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.core.tools.StringTool;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 검색 서비스에서 제공하는 기본 엔티티입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 4. 오후 6:32
 */
@Entity
@Indexed
@Getter
@Setter
public class Document extends UuidEntityBase {

    private static final long serialVersionUID = 88626353422125201L;

    public Document() {}

    public Document(String rowId) {
        this.rowId = rowId;
    }

    /**
     * Document의 고유 RowId
     */
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    private String rowId;

    @Field
    @Analyzer(impl = KoreanAnalyzer.class)
    @Boost(2.0f)
    private String body;

    /**
     * Document 생성자의 Id 또는 이름
     */
    @Field(analyze = Analyze.NO)
    private String creatorId;

    /**
     * Document 생성 일자 (milliseconds)
     */
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    private long createdTime = 0L;


    @IndexedEmbedded
    @ElementCollection(targetClass = DocumentAttr.class, fetch = FetchType.EAGER)
    private Set<DocumentAttr> attrs = new LinkedHashSet<DocumentAttr>();

    /**
     * 새로운 Attribute 를 추가합니다.
     *
     * @param attrName  Attribute 명
     * @param attrValue Attribute 값
     */
    public void addAttr(String attrName, String attrValue) {
        attrs.add(new DocumentAttr(attrName, attrValue));
    }

    /**
     * 지정한 이름의 Attribute 를 삭제합니다.
     *
     * @param attrName 특성명
     */
    public void removeAttr(String attrName) {
        DocumentAttr toRemove = getAttr(attrName);
        if (toRemove != null)
            attrs.remove(toRemove);
    }

    /**
     * 지정한 특성명에 해당하는 {@link DocumentAttr} 를 반환합니다.
     *
     * @param attrName 특성 명
     * @return 특성 ({@link DocumentAttr})
     */
    public DocumentAttr getAttr(String attrName) {
        for (DocumentAttr attr : attrs) {
            if (attr.getName().equalsIgnoreCase(attrName)) {
                return attr;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        if (StringTool.isNotWhiteSpace(getId()))
            return super.hashCode();

        return HashTool.compute(rowId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("rowId", rowId)
                .add("creatorId", creatorId)
                .add("createdTime", createdTime)
                .add("attrs", StringTool.listToString(attrs));
    }
}
