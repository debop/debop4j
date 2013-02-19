package kr.debop4j.data.mapping.model.hbm;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

/**
 * 주소 정보
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
@Getter
@Setter
public class Address extends ValueObjectBase {

    private static final long serialVersionUID = -4051226015707369322L;

    private String street;
    private String zipcode;
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
