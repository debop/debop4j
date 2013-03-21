package com.kt.vital.domain.model.admin;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 토픽을 표현하는 엔티티의 기본 클래스
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:27
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Topic")
@org.hibernate.annotations.Table(appliesTo = "TOPIC",
                                 indexes = {@Index(name = "ix_topic_name",
                                                   columnNames = {"TopicKind", "TopicName"})})
@DiscriminatorColumn(name = "TopicKind", discriminatorType = DiscriminatorType.STRING, length = 31)
@DiscriminatorValue("topic")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class TopicBase extends VitalEntityBase {

    private static final long serialVersionUID = -7243194330033348977L;

    protected TopicBase() { }

    protected TopicBase(String topicName) {
        this.name = topicName;
    }

    @Id
    @GeneratedValue
    @Column(name = "TopicId")
    private Long id;

    @NotEmpty
    @Column(name = "TopicName", nullable = false, length = 128)
    private String name;

    @Basic
    private Boolean enabled = true;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime createdTime = DateTime.now();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("enabled", enabled)
                .add("createdTime", createdTime);
    }
}
