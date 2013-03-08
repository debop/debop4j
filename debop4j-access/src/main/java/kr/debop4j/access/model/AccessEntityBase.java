package kr.debop4j.access.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * debop4j access 모듈의 엔티티들의 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 1:12
 */
@MappedSuperclass
public abstract class AccessEntityBase extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIMESTAMP")
    private Date updateTimestamp;

    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }
}
