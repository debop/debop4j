package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/**
 * 사용자가 즐겨찾기한 컨텐츠 (메뉴)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 11.
 */
@Entity
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserFavorite extends AccessEntityBase {

    private static final long serialVersionUID = 5857584976380701117L;

    protected UserFavorite() {}

    public UserFavorite(User user, String content) {
        Guard.shouldNotBeNull(user, "user");

        this.user = user;
        this.content = content;
    }

    @Id
    @GeneratedValue
    @Column(name = "UserFavoriteId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    @Index(name = "ix_userfavorite")
    @NaturalId
    private User user;

    @Column(name = "Content", nullable = false, length = 2000)
    @NaturalId
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registDate;

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    /**
     * 우선순위
     */
    @Basic
    private Integer preference;

    @Column(length = 2000)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(user, content);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("user", user)
                .add("content", content)
                .add("registDate", registDate)
                .add("preference", preference)
                .add("active", active)
                .add("description", description)
                .add("exAttr", exAttr);
    }
}
