package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.IActor;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.organization.Department;
import kr.debop4j.access.model.organization.Employee;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 사용자 정보
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 8 오후 5:12
 */
@Entity
@Table(name = "`User`")
@org.hibernate.annotations.Table(appliesTo = "`User`",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_user",
                                                                            columnNames = {
                                                                                    "ProductId",
                                                                                    "CompanyId",
                                                                                    "UserName",
                                                                                    "Password" }))
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class User extends AccessEntityBase implements IActor {

    private static final long serialVersionUID = 7743783792450069754L;

    private User() {}

    public User(Product product, String username, String password) {
        this(product, null, null, null, username, password);
    }

    public User(Product product, Company company, String username, String password) {
        this(product, company, null, null, username, password);
    }

    public User(Product product, Company company, Department department, Employee employee, String username, String password) {
        Guard.shouldNotBeNull(product, "product");
        Guard.shouldNotBeEmpty(username, "username");
        Guard.shouldNotBeEmpty(password, "password");

        this.product = product;
        this.company = company;
        this.department = department;
        this.employee = employee;
        this.username = username;
        this.password = password;
        this.active = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "UserId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

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
    private String username;

    @Column(nullable = false, length = 128)
    private String password;

    @Column(length = 64)
    private String nickname;

    @Column(name = "active")
    private Boolean active;

    @Column(length = 255)
    private String confirmQuestion;

    @Column(length = 255)
    private String confirmAnswer;

    @Transient
    public String getCode() {
        return username;
    }

    @Transient
    public String getName() {
        return nickname;
    }

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
                .add("product", product)
                .add("company", company)
                .add("employee", employee)
                .add("department", department);
    }
}
