package kr.debop4j.access.model.common;

import kr.debop4j.access.model.Company;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 직원 직급 (Grade) 정보 (예: 1급, 2급 등 호봉체계에 사용된다.)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:03
 */
@Entity
@Table(name = "EmployeeGrade")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeeGrade extends EmployeeCodeBase {

    protected EmployeeGrade() {}

    public EmployeeGrade(Company company, String code) {
        this(company, code, code);
    }

    public EmployeeGrade(Company company, String code, String name) {
        super(company, code, name);
    }

    public String parentCode;

}
