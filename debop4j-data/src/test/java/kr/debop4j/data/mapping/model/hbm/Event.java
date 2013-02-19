package kr.debop4j.data.mapping.model.hbm;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import kr.debop4j.data.model.EntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class Event extends EntityBase<Long> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 930773110476290116L;

    public Event() {
    }

    public Event(String title, Date date) {

        assert !Strings.isNullOrEmpty(title);

        this.title = title;
        this.date = date;
    }

    private Date date;
    private String title;
    private Category category;

    @Setter(value = AccessLevel.PROTECTED)
    private Timestamp updateTimestamp;

    public void updateUpdateTimestamp() {
        updateTimestamp = new Timestamp(DateTime.now().getMillis());
    }


    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(title, date);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("title", title)
                .add("moment", date)
                .add("updateTimestamp", updateTimestamp)
                .add("categoryId", category.getId());
    }
}

