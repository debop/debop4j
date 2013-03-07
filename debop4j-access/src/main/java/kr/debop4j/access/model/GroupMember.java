package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import kr.debop4j.data.model.LongAnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

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
public class GroupMember extends LongAnnotatedEntityBase implements IUpdateTimestampedEntity {

    protected GroupMember() {}

    public GroupMember(Department department, OrganizationKind memberKind, Long memberId) {
        Guard.shouldNotBeNull(department, "department");
        this.department = department;
        this.memberKind = memberKind;
        this.memberId = memberId;
    }

    @ManyToOne
    @JoinColumn(name = "departmentId", nullable = false)
    @Index(name = "ix_groupmember")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "MemberKind", nullable = false, length = 128)
    @Index(name = "ix_groupmember")
    private OrganizationKind memberKind = OrganizationKind.Employee;

    @Column(name = "MemberId", nullable = false)
    @Index(name = "ix_groupmember")
    private Long memberId;

    @Column(name = "IsActive")
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(department, memberKind, memberId);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("departmentId", department.getId())
                .add("memberKind", memberKind)
                .add("memberId", memberId);
    }
}
