package kr.debop4j.data.mapping.model.hbm.join;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

/**
 * 주소 정보
 * Jpa@author 배성혁 ( sunghyouk.bae@gmail.com )
 *
 * @since 12. 11. 19.
 */
@Getter
@Setter
public class Address extends ValueObjectBase {

    private static final long serialVersionUID = 4556469620637965297L;

    private String street;

    private String zipcode;

    private String city;

    @Override
    public int hashCode() {
        return Objects.hashCode(street, zipcode, city);
    }

    @Override
    public Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("street", street)
                .add("zipcode", zipcode)
                .add("city", city);
    }
}
