package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 그룹의 구성원
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 5 오후 4:26
 */
@Entity
@Table(name = "GroupMember")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class GroupMember extends AccessEntityBase {

    private static final long serialVersionUID = 538564009832398711L;

    protected GroupMember() {}

    public GroupMember(Group group, OrganizationKind memberKind, Long memberId) {
        Guard.shouldNotBeNull(group, "group");
        this.group = group;
        this.memberKind = memberKind;
        this.memberId = memberId;

        this.group.getMembers().add(this);
    }

    @Id
    @GeneratedValue
    @Column(name = "GroupMemberId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GroupId", nullable = false)
    @Index(name = "ix_groupmember")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "MemberKind", nullable = false, length = 128)
    @Index(name = "ix_groupmember")
    private OrganizationKind memberKind = OrganizationKind.Employee;

    @Column(name = "MemberId", nullable = false)
    @Index(name = "ix_groupmember")
    private Long memberId;

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(group, memberKind, memberId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("memberKind", memberKind)
                .add("memberId", memberId)
                .add("groupId", group.getId());
    }
}
