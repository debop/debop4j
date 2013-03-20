package com.kt.vital.domain.model.admin;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalHistoryEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * N분마다 Genesis로부터 Voc 정보를 얻어 Vital에 Import 를 수행한 이력
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 19 오후 2:44
 */
@Entity
@Table(name = "VocImportHistory")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class VocImportHistory extends VitalHistoryEntityBase {

    private static final long serialVersionUID = -6180572636513317073L;

    protected VocImportHistory() {}

    public VocImportHistory(Date importTime, Long importCount, Long workTime) {
        this.importTime = importTime;
        this.importCount = importCount;
        this.workTime = workTime;
    }

    @Id
    @GeneratedValue
    @Column(name = "HistoryId")
    private Long id;

    /**
     * Import 작업 시작 시각
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ImportTime")
    private Date importTime;

    /**
     * Import 한 VoC의 갯수
     */
    @Column(name = "ImportCount")
    private Long importCount;

    /**
     * Import 작업 시간
     */
    @Column(name = "WorkTime")
    private Long workTime;

    /**
     * Import 작업 성공 여부
     */
    @Basic
    private Boolean isSuccess;

    /**
     * 작엽 설명
     */
    @Column(name = "Description", length = 1024)
    private String description;

    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(importCount, importCount, workTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("importTime", importTime)
                    .add("importCount", importCount)
                    .add("workTime", workTime)
                    .add("isSuccess", isSuccess);
    }
}
