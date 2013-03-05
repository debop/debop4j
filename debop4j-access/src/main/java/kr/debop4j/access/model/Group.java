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
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;

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
public class Group extends LongAnnotatedEntityBase implements ICodeBaseEntity, IUpdateTimestampedEntity {

    protected Group() {
    }

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column(name = "GroupCode", nullable = false, length = 32)
    private String code;

    @Column(name = "GroupName", nullable = false, length = 256)
    private String name;

    @Column(name = "GroupEName", length = 256)
    private String ename;

    @Column(name = "GroupDesc", length = 2000)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Timestamp(DateTime.now().getMillis());
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("code", code)
                    .add("name", name);

    }
}
