package com.kt.vital.domain.model.history;

import com.google.common.base.Objects;
import com.kt.vital.domain.model.VitalLogEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * com.kt.vital.domain.model.history.WorkLogBase
 *
 * @author: sunghyouk.bae@gmail.com
 */
@Entity
@Table(name = "WorkLog")
@org.hibernate.annotations.Table(appliesTo = "WORK_LOG",
                                 indexes = {@Index(name = "ix_worklog_time",
                                                   columnNames = {"LogKind", "StartTime", "EndTime"})})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LogKind")
@DiscriminatorValue("WorkLog")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class WorkLogBase extends VitalLogEntityBase {

    private static final long serialVersionUID = 1673034768457656169L;

    protected WorkLogBase() {}

    protected WorkLogBase(DateTime startTime, DateTime endTime, Long itemCount, Boolean isSuccess) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.itemCount = itemCount;
        this.isSuccess = isSuccess;
    }

    /**
     * 작업 시작 시각
     */
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "StartTime")
    private DateTime startTime;

    /**
     * 작업 완료 시각
     */
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name = "EndTime")
    private DateTime endTime;

    /**
     * 작업 갯수
     */
    @Basic
    private Long itemCount;

    /**
     * 작업 결과
     */
    @Basic
    private Boolean isSuccess;

    /**
     * 작업 시 예외가 있는 경우 stackTrace 정보를 저장한다.
     */
    @Column(name = "StackTrace", length = 4000)
    private String stackTrace;

    /**
     * 작업 예외 시에 StackTrace 정보를 저장하도록 합니다.
     *
     * @param exception 작업 예외
     */
    @Transient
    public void setException(Throwable exception) {
        stackTrace = ExceptionUtils.getStackTrace(exception);
    }

    /**
     * 작업 시간
     */
    @Transient
    public long getWorkTime() {
        return endTime.minus(startTime.getMillis()).getMillis();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(getId());
        return HashTool.compute(startTime, endTime);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("itemCount", itemCount)
                .add("isSuccess", isSuccess);
    }
}
