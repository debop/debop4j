package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.PreferenceBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 사용자 환경 설정 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "UserPreference",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_userpreference",
                                                                            columnNames = {
                                                                                    "UserId",
                                                                                    "PrefKey"}))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserPreference extends PreferenceBase {

    private static final long serialVersionUID = -4417301208305719344L;

    protected UserPreference() {}

    public UserPreference(User user, String key, String value) {
        super(key, value);

        Guard.shouldNotBeNull(user, "user");

        this.user = user;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(user, getKey());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("user", user.getId())
                .add("key", getKey())
                .add("value", getValue());
    }
}
