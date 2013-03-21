package com.kt.vital.domain.model.history;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * MDM으로부터 마스터 데이터 (마스터코드, 직원, 부서 정보) Import 작업의 로그
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Entity
@DiscriminatorValue("MasterDataImport")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class MasterDataImportLog extends WorkLogBase {

    private static final long serialVersionUID = -1832558525593491987L;

    protected MasterDataImportLog() {}

    public MasterDataImportLog(DateTime startTime, DateTime endTime, Long userCount, Boolean isSuccess) {
        super(startTime, endTime, userCount, isSuccess);
    }
}
