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
 * kr.debop4j.access.model.calendar.WorkTimeByMinute
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
 */
@Entity
@Table(name = "WorkTimeByMinute")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class WorkTimeByMinute extends WorkTimeByTimeBase {
    private static final long serialVersionUID = 5542870547481028168L;

    protected WorkTimeByMinute() {}

    public WorkTimeByMinute(WorkCalendar workCalendar, Date workMinute) {
        super(workCalendar, workMinute);
    }

    @Id
    @GeneratedValue
    @Column(name = "WorkTimeId")
    private Long id;

    @Transient
    public Date getWorkMinute() {
        return super.getWorkTime();
    }

    public void setWorkMinute(Date workMinute) {
        super.setWorkTime(workMinute);
    }


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(getWorkCalendar(), getWorkMinute());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("workHour", getWorkMinute());
    }
}
