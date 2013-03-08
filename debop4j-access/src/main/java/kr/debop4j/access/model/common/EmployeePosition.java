package kr.debop4j.access.model.common;

import kr.debop4j.access.model.Company;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 직위 정보  (예:사원,대리,과장,차장,부장 등)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:05
 */
@Entity
@Table(name = "EmployeePosition")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeePosition extends EmployeeCodeBase {

    protected EmployeePosition() {}

    public EmployeePosition(Company company, String code) {
        this(company, code, code);
    }

    public EmployeePosition(Company company, String code, String name) {
        super(company, code, name);
    }
}
