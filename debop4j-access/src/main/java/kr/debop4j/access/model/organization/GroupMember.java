package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 그룹의 구성원
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 5 오후 4:26
 */
@Entity
@Table(name = "GroupMember")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class GroupMember extends AccessEntityBase {

    private static final long serialVersionUID = 538564009832398711L;

    protected GroupMember() {}

    public GroupMember(Department department, OrganizationKind memberKind, Long memberId) {
        Guard.shouldNotBeNull(department, "department");
        this.department = department;
        this.memberKind = memberKind;
        this.memberId = memberId;
    }

    @Id
    @GeneratedValue
    @Column(name = "GroupMemberId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DepartmentId", nullable = false)
    @Index(name = "ix_groupmember")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "MemberKind", nullable = false, length = 128)
    @Index(name = "ix_groupmember")
    private OrganizationKind memberKind = OrganizationKind.Employee;

    @Column(name = "MemberId", nullable = false)
    @Index(name = "ix_groupmember")
    private Long memberId;

    @Column(name = "Active")
    private Boolean active;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(department, memberKind, memberId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("departmentId", department.getId())
                .add("memberKind", memberKind)
                .add("memberId", memberId);
    }
}
