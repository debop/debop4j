package kr.debop4j.data.mapping.model.annotated.onetoone;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.onetoone.OneToOne_User
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 4.
 */
@Entity
@Table(name = "JPA_ONE_TO_ONE_USER")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class OneToOne_User extends JpaEntityBase {

    private static final long serialVersionUID = -1767636271426001202L;

    @Id
    @GeneratedValue
    @Column(name = "SHIPMENT_ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstname;

    @Column(name = "LAST_NAME")
    private String lastname;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_PASSWD")
    private String password;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "RANKING")
    private Integer ranking;

    @Column(name = "ADMIN")
    private Boolean admin;

    @OneToOne
    @JoinColumn(name = "SHIPPING_ADDRESS_ID")
    private OneToOne_Address shippingAddress = new OneToOne_Address();

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(username, password);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .add("admin", admin);
    }
}
