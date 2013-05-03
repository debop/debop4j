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
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 한 부서의 구성원 정보 (직원의 겸직이 가능하므로, 부서-직원은 many-to-many 관계를 가집입니다)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 5 오후 4:33
 */
@Entity
@Table(name = "DepartmentMember")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "DepartmentMember",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_DepartmentMember",
                                                                            columnNames = { "DepartmentId", "EmployeeId" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class DepartmentMember extends AccessEntityBase {

    private static final long serialVersionUID = 6458394469111394831L;

    protected DepartmentMember() {}

    public DepartmentMember(Department department, Employee employee) {
        Guard.shouldNotBeNull(department, "department");
        Guard.shouldNotBeNull(employee, "employee");
        Guard.shouldBe(department.getCompany() == employee.getCompany(), "같은 회사여야 합니다.");

        this.department = department;
        this.employee = employee;

        this.department.getMembers().add(this);
    }

    @Id
    @GeneratedValue
    @Column(name = "DeptMemberId")
    private Long id;

    /** 소속 부서 */
    @ManyToOne
    @JoinColumn(name = "DepartmentId", nullable = false)
    private Department department;

    /** 소속 직원 */
    @ManyToOne
    @JoinColumn(name = "EmployeeId", nullable = false)
    private Employee employee;

    /** 소속 시작일 */
    @Column(name = "StartTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    /** 소속 종료일 */
    @Column(name = "EndTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "Active")
    private Boolean active;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    /** 직책 */
    @ManyToOne
    @JoinColumn(name = "EmpTitleId")
    private EmployeeTitle empTitle;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(department, employee);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("departmentId", department.getId())
                .add("employeeId", employee.getId())
                .add("employeeTitle", empTitle)
                .add("active", active)
                .add("startTime", startTime)
                .add("endTime", endTime);
    }
}
