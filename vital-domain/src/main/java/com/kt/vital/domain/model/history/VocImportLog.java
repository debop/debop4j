package com.kt.vital.domain.model.history;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * N분마다 Genesis로부터 Voc 정보를 얻어 Vital에 Import 를 수행한 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 2:44
 */
@Entity
@DiscriminatorValue("VocImport")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class VocImportLog extends WorkLogBase {

    private static final long serialVersionUID = -6180572636513317073L;

    protected VocImportLog() {}

    public VocImportLog(DateTime startTime, DateTime endTime, Long userCount, Boolean isSuccess) {
        super(startTime, endTime, userCount, isSuccess);
    }
}
