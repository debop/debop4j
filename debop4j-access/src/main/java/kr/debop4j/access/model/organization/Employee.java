/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.IActor;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 직원 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 1.
 */
@Entity
@Table(name = "Employee")
@org.hibernate.annotations.Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Employee",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_department_code",
                                                                            columnNames = {
                                                                                    "CompanyId",
                                                                                    "EmployeeCode",
                                                                                    "EmployeeName" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Employee extends AccessEntityBase implements IActor {

    private static final long serialVersionUID = 2919375838394714017L;

    protected Employee() {}

    public Employee(Company company, String employeeCode) {
        this(company, employeeCode, employeeCode);
    }

    public Employee(Company company, String employeeCode, String employeeName) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(employeeCode, "employeeCode");

        this.company = company;
        this.code = employeeCode;
        this.name = employeeName;
    }

    @Id
    @GeneratedValue
    @Column(name = "EmployeeId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false)
    private Company company;

    @Column(name = "EmployeeCode", nullable = false, length = 64)
    private String code;

    @Column(name = "EmployeeName", nullable = false, length = 128)
    private String name;

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "EmpGradeId")
    private EmployeeGrade empGrade;

    @ManyToOne
    @JoinColumn(name = "EmpPositionId")
    private EmployeePosition empPosition;

    @Column(name = "Description", length = 4000)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("active", active)
                .add("empGrade", empGrade)
                .add("empPosition", empPosition);
    }
}
