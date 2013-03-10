package kr.debop4j.access.model;

import kr.debop4j.data.model.AnnotatedLocaleEntityBase;
import kr.debop4j.data.model.ILocaleValue;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 다국어를 지원하는 Entity를 나타냅니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 3:46
 */
@MappedSuperclass
public abstract class AccessLocaledEntityBase<TLocaleValue extends ILocaleValue>
        extends AnnotatedLocaleEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 4105817032363041651L;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTimestamp")
    private Date updateTimestamp;

    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }
}
