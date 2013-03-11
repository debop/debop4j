package kr.debop4j.access.model.common;

import com.google.common.base.Objects;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 직책 (보직) 을 나타낸다. (개발본부장, TFT 팀장, 경영지원실장 등)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:10
 */
@Entity
@Table(name = "EmpTitle")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeeTitle extends EmployeeCodeBase {

    private static final long serialVersionUID = -3548865945774656200L;

    protected EmployeeTitle() {}

    public EmployeeTitle(Company company, String code) {
        this(company, code, code);
    }

    public EmployeeTitle(Company company, String code, String name) {
        super(company, code, name);
    }

    @Id
    @GeneratedValue  // PostgreSQL, Oracle 처럼 Database 전역 Sequence 가 있는 경우에만 Table per class 상속이 가능하다.
    @Column(name = "EmpTitleId")
    private Long id;


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
