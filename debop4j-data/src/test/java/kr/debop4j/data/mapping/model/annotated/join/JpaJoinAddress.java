package kr.debop4j.data.mapping.model.annotated.join;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * org.annotated.mapping.domain.model.join.JpaJoinAddress
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 5.
 */
@Embeddable
@Getter
@Setter
public class JpaJoinAddress extends ValueObjectBase {

    private static final long serialVersionUID = -3754337530016792857L;

    @Column(name = "STREET", length = 128)
    private String street;

    @Column(name = "ZIPCODE", length = 12)
    private String zipcode;

    @Column(name = "CITY", length = 64)
    private String city;

    @Override
    public int hashCode() {
        return Objects.hashCode(street, zipcode, city);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("street", street)
                .add("zipcode", zipcode)
                .add("city", city);
    }
}
