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
 * 사용자 정보
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:12
 */
@Entity
@Table(name = "`User`")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User extends AccessEntityBase {

    private User() {}

    protected User(Company company, String username, String password) {
        this(company, null, null, username, password);
    }

    protected User(Company company, Department department, Employee employee, String username, String password) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(password, "password");

        this.department = department;
        this.employee = employee;
        this.username = username;
        this.password = password;
    }


    @Id
    @GeneratedValue
    @Column(name = "UserId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DepartmentId")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeId")
    private Employee employee;

    @Column(nullable = false, length = 32)
    @Index(name = "ix_user_username")
    private String username;

    @Column(nullable = false, length = 128)
    @Index(name = "ix_user_username")
    private String password;

    @Column(length = 64)
    private String nickname;

    @Column(name = "active")
    private Boolean active;

    @Column(length = 255)
    private String confirmQuestion;

    @Column(length = 255)
    private String confirmAnswer;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(department, employee, username, password);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("username", username)
                .add("password", password)
                .add("nickname", nickname)
                .add("active", active)
                .add("company", company)
                .add("employee", employee)
                .add("department", department);
    }
}
