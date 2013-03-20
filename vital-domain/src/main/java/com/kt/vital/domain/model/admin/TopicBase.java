package com.kt.vital.domain.model.admin;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * 토픽
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 20 오전 11:27
 */
@Entity
@Table(name = "Topic")
@org.hibernate.annotations.Table(appliesTo = "Topic",
                                 indexes = {@Index(name = "ix_topic_name",
                                                   columnNames = {"TopicKind", "TopicName"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TopicKind", discriminatorType = DiscriminatorType.STRING, length = 31)
@DiscriminatorValue("TopicBase")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class TopicBase extends VitalEntityBase {

    private static final long serialVersionUID = -7243194330033348977L;

    protected TopicBase() {
        this.enabled = true;
        this.createdTime = new Date();
    }

    protected TopicBase(String topicName) {
        this();
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
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

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
