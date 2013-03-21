package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.IActor;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 가상의 조직을 나타냅니다.
 * User: Administrator
 * Date: 13. 3. 5
 */
@Entity
@Table(name = "`Group`")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "`Group`",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_group_code",
                                                                            columnNames = {
                                                                                    "CompanyId",
                                                                                    "GroupCode",
                                                                                    "GroupName"}))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Group extends AccessEntityBase implements IActor {

    private static final long serialVersionUID = 514703125940494102L;

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
        this.active = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "GROUP_ID")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", nullable = false)
    private Company company;

    @Column(name = "GroupCode", nullable = false, length = 32)
    private String code;

    @Column(name = "GroupName", nullable = false, length = 256)
    private String name;

    @Column(name = "GroupEName", length = 256)
    private String ename;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "GroupDesc", length = 2000)
    private String description;

    @Column(name = "GroupExAttr", length = 2000)
    private String exAttr;


    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL})
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<GroupMember> members = Sets.newHashSet();

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
