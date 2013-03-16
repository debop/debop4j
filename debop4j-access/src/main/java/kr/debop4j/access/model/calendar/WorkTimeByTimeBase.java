package kr.debop4j.access.model.calendar;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 특정 단위 시각의 작업 시간에 대한 정보의 인터페이스 (일단위, 시간단위, 분단위, 5분단위, 월단위, 주단위 등 모두 가능하다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class WorkTimeByTimeBase extends AccessEntityBase implements IWorkTimeByTime {

    private static final long serialVersionUID = -5031854719315716211L;

    protected WorkTimeByTimeBase() {}

    public WorkTimeByTimeBase(WorkCalendar workCalendar, Date workTime) {
        Guard.shouldNotBeNull(workCalendar, "workcalendar");
        this.workCalendar = workCalendar;
        this.workTime = workTime;
    }

    @ManyToOne
    @JoinColumn(name = "workCalendarId", nullable = false)
    private WorkCalendar workCalendar;

    @Column(name = "WorkTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter(AccessLevel.PROTECTED)
    private Date workTime;

    @Column(name = "IsWorking")
    private Boolean isWorking;

    @Column(name = "WorkInMinute")
    private Integer workInMinute;

    @Column(name = "CumulatedInMinute")
    private Long cumulatedInMinute;

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("workTime", workTime)
                .add("isWorking", isWorking)
                .add("workInMinute", workInMinute)
                .add("workcalendar", workCalendar);
    }
}
