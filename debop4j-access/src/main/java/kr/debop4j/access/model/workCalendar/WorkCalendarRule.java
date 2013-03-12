package kr.debop4j.access.model.workcalendar;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.timeperiod.ITimePeriod;
import kr.debop4j.core.timeperiod.TimeRange;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * kr.debop4j.access.model.workcalendar.WorkCalendarRule
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@Entity
@Table(name = "WorkCalendarRule")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class WorkCalendarRule extends AccessEntityBase {

    @Id
    @GeneratedValue
    @Column(name = "RuleId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "WorkCalendarId")
    @Index(name = "ix_WorkCalendarRule")
    @NaturalId
    private WorkCalendar workCalendar;

    @Column(name = "RuleName", nullable = false)
    @Index(name = "ix_WorkCalendarRule")
    @NaturalId
    private String name;

    /**
     * 특정 날짜 또는 휴일 등의 예외 규칙인지를 나타냅니다.
     */
    @Basic
    private Integer dayOrException;

    /**
     * 예외 규칙 종류
     */
    @Basic
    private Integer exceptionType;

    /**
     * 예외 패턴 (준비중이다. cron-expression을 사용할 예정)
     */
    @Basic
    private String exeptionPattern;

    /**
     * 예외를 규정할 클래스 명 (동적으로 로드해서 계산하도록 한다)
     */
    private String exceptionClassName;

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime"),
                             @Column(name = "EndTime")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod = new TimeRange();

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime1"),
                             @Column(name = "EndTime1")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod1 = new TimeRange();

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime2"),
                             @Column(name = "EndTime2")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod2 = new TimeRange();

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime3"),
                             @Column(name = "EndTime3")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod3 = new TimeRange();

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime4"),
                             @Column(name = "EndTime4")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod4 = new TimeRange();

    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns =
                     {
                             @Column(name = "StartTime5"),
                             @Column(name = "EndTime5")
                     })
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod5 = new TimeRange();

    private final ITimePeriod[] rulePeriods = new ITimePeriod[]{
            rulePeriod1, rulePeriod2, rulePeriod3, rulePeriod4, rulePeriod5
    };

    private Integer viewOrder;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(workCalendar, name, rulePeriod);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("rulePeriod", rulePeriod);
    }
}
