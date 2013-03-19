package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * com.kt.vital.domain.model.VitalHistoryEntityBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 2:55
 */
@MappedSuperclass
@Getter
@Setter
public abstract class VitalHistoryEntityBase extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1746138896005813607L;

    protected VitalHistoryEntityBase() {
        this.createdTime = new Date();
    }

    @Id
    @GeneratedValue
    @Column(name = "HistoryId")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(createdTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("createdTime", createdTime);
    }
}
