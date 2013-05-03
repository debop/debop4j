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
import kr.debop4j.access.model.ICodeBaseEntity;
import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * 직급, 직위, 직책 등 직원과 관련된 코드 정보를 나타내는 추상클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 8 오후 4:45
 */
// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// @DynamicInsert
// @DynamicUpdate
@MappedSuperclass
@org.hibernate.annotations.Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
public abstract class EmployeeCodeBase extends AccessEntityBase implements ICodeBaseEntity {

    private static final long serialVersionUID = -3706853105005691162L;

    protected EmployeeCodeBase() {}

    protected EmployeeCodeBase(Company company, String code) {
        this(company, code, code);
    }

    protected EmployeeCodeBase(Company company, String code, String name) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");

        this.company = company;
        this.code = code;
        this.name = name;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId")
    private Company company;

    @Column(name = "CodeValue", nullable = false, length = 128)
    private String code;

    @Column(name = "CodeName", nullable = false, length = 256)
    private String name;

    @Column(name = "ViewOrder")
    private Integer viewOrder;

    @Column(name = "Description", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("code", code)
                .add("name", name)
                .add("company", company);
    }
}
