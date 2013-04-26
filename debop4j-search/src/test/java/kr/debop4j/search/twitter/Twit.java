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

package kr.debop4j.search.twitter;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

/**
 * hibernate-ogm 은 Hibernate의 criteria를 지원하지 않습니다. 이를 보조하기 위해 hibernate-search를 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 3:00
 */
@Entity
@Indexed
@Getter
@Setter
//@Analyzer(impl = CJKAnalyzer.class)
@Analyzer(impl = KoreanAnalyzer.class)
public class Twit extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1831686112282898189L;

    @Id
    @DocumentId
    private Long id;

    @Column(name = "UserName")
    @Field(index = Index.YES, store = Store.YES, analyze = Analyze.YES)
    @Boost(1.5f)
    private String username;

    @Column(name = "Text", length = 300)
    @Field(index = Index.YES, store = Store.COMPRESS, analyze = Analyze.YES)
    private String text;

    @DateBridge(resolution = Resolution.SECOND)
    @Temporal(TemporalType.TIMESTAMP)
    @Field(index = Index.YES, store = Store.YES, analyze = Analyze.NO)
    private Date createdAt;
}
