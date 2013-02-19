package kr.debop4j.data.mapping.model.hbm.join;

import com.google.common.base.Objects;
import kr.debop4j.data.model.EntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 고객 정보
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19.
 */
@Getter
@Setter
public class Join_Customer extends EntityBase<Long> implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 9221823986414874215L;

    protected Join_Customer() {
    }

    public Join_Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    private String name;
    private String email;
    private Date created;

    private Address address = new Address();

    @Setter(value = AccessLevel.PRIVATE)
    private Timestamp updateTimestamp;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(name, email);
    }

    @Override
    public Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("email", email)
                .add("created", created);
    }

    @Override
    public void updateUpdateTimestamp() {
        this.updateTimestamp = new Timestamp(DateTime.now().getMillis());
    }
}
