package kr.debop4j.data.ogm.test.embeddable;

import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * kr.debop4j.data.ogm.test.embeddable.Address
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Embeddable
@Getter
@Setter
public class Address extends ValueObjectBase {

    private String street1;

    private String street2;

    private String city;

    @Column(name = "postal_code")
    private String zipCode;

    public String country;
}


