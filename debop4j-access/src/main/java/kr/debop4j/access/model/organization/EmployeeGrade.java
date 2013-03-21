package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 직원 직급 (Grade) 정보 (예: 1급, 2급 등 호봉체계에 사용된다.)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:03
 */
@Entity
@Table(name = "EmpGrade")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeeGrade extends EmployeeCodeBase {

    private static final long serialVersionUID = -4295473386640384874L;

    protected EmployeeGrade() {}

    public EmployeeGrade(Company company, String code) {
        this(company, code, code);
    }

    public EmployeeGrade(Company company, String code, String name) {
        super(company, code, name);
    }

    @Id
    @GeneratedValue  // PostgreSQL, Oracle 처럼 Database 전역 Sequence 가 있는 경우에만 Table per class 상속이 가능하다.
    @Column(name = "EmpGradeId")
    private Long id;

    @Column(name = "ParentCode", length = 128)
    public String parentCode;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(getCompany(), getCode());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id);
    }
}