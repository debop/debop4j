package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.organization.User;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * kr.debop4j.access.model.product.UserPreference
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Entity
@Table(name = "UserPreference",
       uniqueConstraints = {@UniqueConstraint(name = "uq_user_preference",
                                              columnNames = {"ProductId", "UserId", "UserPrefKey"})})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserPreference extends AccessEntityBase {

    private static final long serialVersionUID = -4417301208305719344L;

    protected UserPreference() {}

    public UserPreference(Product product, User user, String key, String value) {
        Guard.shouldNotBeNull(product, "product");
        Guard.shouldNotBeNull(user, "user");
        Guard.shouldNotBeEmpty(key, "key");

        this.product = product;
        this.user = user;
        this.key = key;
        this.value = value;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    @Index(name = "ix_user_preference")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    @Index(name = "ix_user_preference")
    private User user;

    @Column(name = "UserPrefKey", nullable = false, length = 256)
    @Index(name = "ix_user_preference")
    private String key;

    @Column(name = "UserPrefValue", length = 4000)
    private String value;

    @Column(name = "DefaultValue", length = 4000)
    private String defaultValue;

    @Column(name = "Description", length = 1024)
    private String description;

    @Column(name = "ExAttr", length = 1024)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(product, user, key);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("product", product.getId())
                .add("user", user.getId())
                .add("key", key)
                .add("value", value);
    }
}
