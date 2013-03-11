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
 * 직위 정보  (예:사원,대리,과장,차장,부장 등)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:05
 */
@Entity
@Table(name = "EmpPosition")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeePosition extends EmployeeCodeBase {

    private static final long serialVersionUID = 4057406303429886156L;

    protected EmployeePosition() {}

    public EmployeePosition(Company company, String code) {
        this(company, code, code);
    }

    public EmployeePosition(Company company, String code, String name) {
        super(company, code, name);
    }

    @Id
    @GeneratedValue  // PostgreSQL, Oracle 처럼 Database 전역 Sequence 가 있는 경우에만 Table per class 상속이 가능하다.
    @Column(name = "EmpPositionId")
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
