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

    protected User(String username, String password) {
        this(null, null, username, password);
    }

    protected User(Department department, Employee employee, String username, String password) {
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

    private String confirmQuestion;

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
                .add("employee", employee)
                .add("department", department);
    }
}
