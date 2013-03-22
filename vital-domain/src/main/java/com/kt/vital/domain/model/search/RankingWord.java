package com.kt.vital.domain.model.search;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Map;

/**
 * 특정 시간의 실시간 순위 Word
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 22 오후 1:01
 */
@Entity
@Table(name = "RankingWord")
@org.hibernate.annotations.Table(appliesTo = "RANKING_WORD",
                                 indexes = {@Index(name = "ix_ranking_word",
                                                   columnNames = {"RankingName", "RankingTime"})})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class RankingWord extends AnnotatedEntityBase {

    private static final long serialVersionUID = -4845644155812129186L;

    protected RankingWord() {}

    public RankingWord(String name, DateTime rankingTime) {
        this.name = name;
        this.rankingTime = rankingTime;
    }

    /**
     * Ranking Id
     */
    @Id
    @GeneratedValue
    @Column(name = "RankingId")
    private Long id;

    /**
     * 순위 명
     */
    @NotEmpty
    @Column(name = "RankingName", nullable = false, length = 128)
    private String name;

    /**
     * 순위를 매긴 시각
     */
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "RankingTime")
    private DateTime rankingTime;

    /**
     * 최상위 Ranking Words
     */
    @CollectionTable(name = "RaninkgWordItem", joinColumns = {@JoinColumn(name = "RankingId")})
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "Rank")
    @Column(name = "Word", nullable = false, length = 128)
    private Map<Integer, String> words = Maps.newHashMap();

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("rankingTime", rankingTime)
                    .add("words", words);
    }
}
