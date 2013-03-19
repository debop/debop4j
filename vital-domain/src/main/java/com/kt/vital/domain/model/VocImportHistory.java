package com.kt.vital.domain.model;

import com.google.common.base.Objects;
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

    public VocImportHistory(Date importTime, Long vocCount, Long elapsedTime) {
        this.importTime = importTime;
        this.vocCount = vocCount;
        this.elapsedTime = elapsedTime;
    }

    @Id
    @GeneratedValue
    @Column(name = "HistoryId")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date importTime;

    @Column(name = "VocCount")
    private Long vocCount;

    @Column(name = "ElapsedTime")
    private Long elapsedTime;

    @Override
    public int hashCode() {
        if (isPersisted())
            HashTool.compute(id);
        return HashTool.compute(vocCount, vocCount, elapsedTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id)
                    .add("importTime", importTime)
                    .add("vocCount", vocCount)
                    .add("elapsedTime", elapsedTime);
    }
}
