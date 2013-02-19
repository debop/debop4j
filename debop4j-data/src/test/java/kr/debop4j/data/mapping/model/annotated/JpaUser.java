package kr.debop4j.data.mapping.model.annotated;

import com.google.common.base.Objects;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.JpaUser
 * 참고 : 테이블이나 컬럼명에 예러 표시 날 때 : Inspection 'DataSource ORM Annotations Problem' options
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 20.
 */
@Entity
@Table(name = "JPA_USERS")
@Cacheable
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class JpaUser extends JpaEntityBase {

    private static final long serialVersionUID = -4278711858304883834L;

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstname;

    @Column(name = "LAST_NAME")
    private String lastname;

    @Column(name = "USER_NAME", length = 128, nullable = false)
    @Index(name = "IX_USER_USER_NAME")
    private String username;

    @Column(name = "USER_PWD", length = 64, nullable = false)
    @Index(name = "IX_USER_USER_NAME")
    private String password;

    @Column(name = "USER_EMAIL")
    @Index(name = "IX_USER_EMAIL")
    private String email;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String exAttrs;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "street", column = @Column(name = "HOME_STREET", length = 128)),
                    @AttributeOverride(name = "zipcode", column = @Column(name = "HOME_ZIPCODE", length = 24)),
                    @AttributeOverride(name = "city", column = @Column(name = "HOME_CITY", length = 128)),
            }
    )
    private JpaAddress homeAddress = new JpaAddress();

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "street", column = @Column(name = "OFFICE_STREET", length = 128)),
                    @AttributeOverride(name = "zipcode", column = @Column(name = "OFFICE_ZIPCODE", length = 24)),
                    @AttributeOverride(name = "city", column = @Column(name = "OFFICE_CITY", length = 128)),
            }
    )
    private JpaAddress officeAddress = new JpaAddress();

    @Override
    public int hashCode() {
        if (isPersisted())
            return Objects.hashCode(id);

        return Objects.hashCode(username, password);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("firstname", firstname)
                .add("lastname", lastname)
                .add("username", username)
                .add("userpwd", password)
                .add("userEmail", email)
                .add("homeAddress", homeAddress)
                .add("officeAddress", officeAddress);
    }
}
