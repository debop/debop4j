package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * com.kt.vital.domain.model.VitalEntityBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:59
 */
@MappedSuperclass
@Getter
@Setter
public abstract class VitalEntityBase extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 8685979366878442016L;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        this.updateTimestamp = new Date();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("updateTimestamp", updateTimestamp);
    }
}
