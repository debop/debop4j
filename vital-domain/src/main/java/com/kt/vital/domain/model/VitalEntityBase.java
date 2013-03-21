package com.kt.vital.domain.model;

import com.google.common.base.Objects;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

/**
 * VITAL 시스템의 기본 엔티티
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:59
 */
@MappedSuperclass
public abstract class VitalEntityBase extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 8685979366878442016L;

    @Getter
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime updatedTime;

    @Transient
    @Override
    public Date getUpdateTimestamp() {
        return updatedTime.toDate();
    }

    @Override
    public void updateUpdateTimestamp() {
        this.updatedTime = DateTime.now();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("updatedTime", updatedTime);
    }
}
