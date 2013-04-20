package kr.debop4j.access.model;

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
 * debop4j access 모듈의 엔티티들의 기본 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 8 오후 1:12
 */
@MappedSuperclass
public abstract class AccessEntityBase extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = -7640693368412411167L;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Getter
    private DateTime updatedTime;

    @Transient
    @Override
    public Date getUpdateTimestamp() {
        return updatedTime.toDate();
    }

    @Override
    public void updateUpdateTimestamp() {
        updatedTime = DateTime.now();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("updatedTime", updatedTime);
    }

}
