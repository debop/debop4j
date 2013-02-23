package kr.debop4j.data.mapping.northwind.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * kr.debop4j.data.mapping.northwind.model.AddressInfo
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Embeddable
@Getter
@Setter
public class AddressInfo extends ValueObjectBase {

    private String address;

    private String city;

    private String postalCode;

    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @Override
    public int hashCode() {
        return HashTool.compute(address, city, postalCode, country);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("address", address)
                .add("city", city)
                .add("postalCode", postalCode)
                .add("country", country);
    }
}
