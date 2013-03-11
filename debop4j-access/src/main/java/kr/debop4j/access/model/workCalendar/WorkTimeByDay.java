package kr.debop4j.access.model.workcalendar;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * kr.debop4j.access.model.workcalendar.WorkTimeByDay
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@Entity
@Table(name = "WorkTimeByDay")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class WorkTimeByDay extends WorkTimeByTimeBase {

    private static final long serialVersionUID = 1758460185774053063L;

    protected WorkTimeByDay() {}

    public WorkTimeByDay(WorkCalendar workCalendar, Date workMinute) {
        super(workCalendar, workMinute);
    }

    @Id
    @GeneratedValue
    @Column(name = "WorkTimeId")
    private Long id;

    private Integer dayOfWeek;

    @Temporal(TemporalType.DATE)
    public Date getWorkDay() {
        return super.getWorkTime();
    }

    public void setWorkDay(Date workDay) {
        super.setWorkTime(workDay);
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(getWorkCalendar(), getWorkDay());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("dayOfWeek", dayOfWeek)
                .add("workDay", getWorkDay());
    }
}
