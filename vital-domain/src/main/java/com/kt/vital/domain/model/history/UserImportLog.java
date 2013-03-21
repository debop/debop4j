package com.kt.vital.domain.model.history;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * IDMS로부터 사용자 정보 ({@link com.kt.vital.domain.model.User}) 정보를 추가합니다.
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Entity
@DiscriminatorValue("UserImport")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserImportLog extends WorkLogBase {

    private static final long serialVersionUID = 8821984746051962415L;

    protected UserImportLog() {}

    public UserImportLog(DateTime startTime, DateTime endTime, Long userCount, Boolean isSuccess) {
        super(startTime, endTime, userCount, isSuccess);
    }
}
