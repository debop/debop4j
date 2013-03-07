package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * 가상의 조직을 나타냅니다.
 * User: Administrator
 * Date: 13. 3. 5
 */
@Entity
@Table(name = "Groups")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Group extends AnnotatedEntityBase implements ICodeBaseEntity, IUpdateTimestampedEntity {

    protected Group() { }

    public Group(Company company, String groupCode) {
        this(company, groupCode, groupCode);
    }

    public Group(Company company, String groupCode, String groupName) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(groupCode, "groupCode");
        Guard.shouldNotBeEmpty(groupName, "groupName");

        this.company = company;
        this.code = groupCode;
        this.name = groupName;
    }

    @Id
    @GeneratedValue
    @Column(name = "GroupId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", nullable = false)
    @Index(name = "ix_group_code")
    private Company company;

    @Column(name = "GroupCode", nullable = false, length = 32)
    @Index(name = "ix_group_code")
    private String code;

    @Column(name = "GroupName", nullable = false, length = 256)
    @Index(name = "ix_group_code")
    private String name;

    @Column(name = "GroupEName", length = 256)
    private String ename;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "GroupDesc", length = 2000)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("comapnyId", company.getId())
                .add("code", code)
                .add("name", name);
    }
}
