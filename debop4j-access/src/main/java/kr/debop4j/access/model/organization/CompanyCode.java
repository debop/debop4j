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
import com.google.common.collect.Sets;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 특정 회사에서 사용하는 코드
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 8 오후 1:07
 */
@Entity
@Table(name = "CompanyCode")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class CompanyCode extends AccessEntityBase {

    protected CompanyCode() {}

    public CompanyCode(Company company, String code) {
        this(company, code, code);
    }

    public CompanyCode(Company company, String code, String name) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(code, "code");

        this.company = company;
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", nullable = false)
    @Index(name = "ix_code")
    private Company company;

    @Column(name = "CodeValue", nullable = false, length = 128)
    @Index(name = "ix_code")
    private String code;

    @Column(name = "CodeName", nullable = false, length = 255)
    @Index(name = "ix_code")
    private String name;

    @Column(name = "Descripton", length = 2000)
    private String description;

    //@Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<CompanyCodeItem> items = Sets.newHashSet();

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
                .add("description", description)
                .add("company", company);
    }
}
