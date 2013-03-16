package kr.debop4j.access.model.calendar;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
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
import java.util.Set;

/**
 * Working Calendar에서 작업 시간, 예외 시간 등의 룰을 나타냅니다.
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

    private static final long serialVersionUID = 8363967938674620509L;

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
    @Columns(columns = {@Column(name = "StartTime"), @Column(name = "EndTime")})
    @Setter(AccessLevel.PROTECTED)
    private ITimePeriod rulePeriod = new TimeRange();

    @CollectionTable(name = "WorkCalendarRulePeriod", joinColumns = {@JoinColumn(name = "RuleId")})
    @ElementCollection(targetClass = ITimePeriod.class, fetch = FetchType.EAGER)
    @Type(type = "kr.debop4j.data.hibernate.usertype.TimeRangeUserType")
    @Columns(columns = {@Column(name = "StartTime"), @Column(name = "EndTime")})
    private final Set<ITimePeriod> rulePeriods = Sets.newHashSet();

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
