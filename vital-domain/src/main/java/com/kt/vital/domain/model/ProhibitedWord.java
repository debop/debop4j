package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * VoiceOfCustomer 메모내용에서 변환되지 말아야 할 예약어 (메모 내용을 볼때, 민감한 정보를 * 로 변한하는데, 변환하지 말아야할 단어를 등록해 놓으면, 대체하지 않는다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 12:58
 */
@Entity
@Table(name = "ProhibitedWord")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProhibitedWord extends VitalEntityBase {

    private static final long serialVersionUID = 8147413421593793669L;

    protected ProhibitedWord() {}

    public ProhibitedWord(String word) {
        Guard.shouldNotBeEmpty(word, "word");

        this.word = word;
        this.enabled = true;
        this.createdTime = new Date();
    }

    @Id
    @GeneratedValue
    @Column(name = "WordId")
    private Long id;

    @Column(name = "word", nullable = false, length = 1024)
    @NaturalId
    @Index(name = "ix_prohibit_word")
    private String word;

    @Basic
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(word);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("word", word)
                    .add("enabled", enabled);
    }
}
