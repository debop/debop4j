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
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 직위 정보  (예:사원,대리,과장,차장,부장 등)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 8 오후 5:05
 */
@Entity
@Table(name = "EmpPosition")
@org.hibernate.annotations.Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "EmpPosition",
                                 indexes = { @Index(name = "ix_emp_position",
                                                    columnNames = { "CompanyId", "CodeValue", "CodeName" }) })
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeePosition extends EmployeeCodeBased {

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
