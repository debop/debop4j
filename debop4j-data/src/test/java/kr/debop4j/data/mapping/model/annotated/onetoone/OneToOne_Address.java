package kr.debop4j.data.mapping.model.annotated.onetoone;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.onetoone.OneToOne_Address
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 4.
 */
@Getter
@Setter
@Entity
@Table(name = "JPA_ONE_TO_ONE_ADDR")
public class OneToOne_Address extends JpaEntityBase {

    private static final long serialVersionUID = -1601813512426750448L;

    @Id
    @GeneratedValue
    @Column(name = "ADDRESS_ID")
    private Long id;

    @OneToOne(mappedBy = "shippingAddress")
    private OneToOne_User user;

    @Column(name = "ADDR_STREET", length = 128)
    private String street;

    @Column(name = "ADDR_ZIPCODE", length = 16)
    private String zipcode;

    @Column(name = "ADDR_CITY", length = 48)
    private String city;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(street, zipcode, city);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("street", street)
                .add("zipcode", zipcode)
                .add("city", city);
    }
}
