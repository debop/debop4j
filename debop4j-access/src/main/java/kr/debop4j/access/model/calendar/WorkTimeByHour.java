package kr.debop4j.access.model.calendar;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * kr.debop4j.access.model.calendar.WorkTimeByHour
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
 */
@Entity
@Table(name = "WorkTimeByHour")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class WorkTimeByHour extends WorkTimeByTimeBase {

    private static final long serialVersionUID = -2800451651797911788L;

    protected WorkTimeByHour() {}

    public WorkTimeByHour(WorkCalendar workCalendar, Date workMinute) {
        super(workCalendar, workMinute);
    }

    @Id
    @GeneratedValue
    @Column(name = "WorkTimeId")
    private Long id;

    @Transient
    public Date getWorkHour() {
        return super.getWorkTime();
    }

    public void setWorkHour(Date workHour) {
        super.setWorkTime(workHour);
    }


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(getWorkCalendar(), getWorkHour());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("workHour", getWorkHour());
    }
}
