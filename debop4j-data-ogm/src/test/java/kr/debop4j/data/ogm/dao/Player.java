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

package kr.debop4j.data.ogm.dao;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

/**
 * kr.debop4j.data.ogm.dao.Player
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 16. 오전 11:34
 */
@Entity
@Table(name = "player")
@Indexed
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Player extends UuidEntityBase {

    private static final long serialVersionUID = 7317574732346075920L;

    @Column(name = "player_name")
    @Field
    private String name;

    @Column(name = "player_surname")
    @Field
    private String surname;

    @Column(name = "player_age")
    @NumericField
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    private int age;

    @Column(name = "player_birth")
    @Temporal(TemporalType.DATE)
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
    @DateBridge(resolution = Resolution.DAY)
    private Date birth;

}
