package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.PreferenceBase;
import kr.debop4j.access.model.organization.User;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;

/**
 * 사용자 환경 설정 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Entity
@Table(name = "UserPreference")
//@Table(name = "UserPreference",
//       uniqueConstraints = {@UniqueConstraint(name = "uq_user_preference",
//                                              columnNames = {"ProductId", "UserId", "UserPrefKey"})})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserPreference extends PreferenceBase {

    private static final long serialVersionUID = -4417301208305719344L;

    protected UserPreference() {}

    public UserPreference(Product product, User user, String key, String value) {
        super(key, value);

        Guard.shouldNotBeNull(product, "product");
        Guard.shouldNotBeNull(user, "user");

        this.product = product;
        this.user = user;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    @Index(name = "ix_user_preference")
    @NaturalId
    private Product product;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    @Index(name = "ix_user_preference")
    @NaturalId
    private User user;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(product, user, getKey());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("product", product.getId())
                .add("user", user.getId())
                .add("key", getKey())
                .add("value", getValue());
    }
}
