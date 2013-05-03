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

package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.access.model.ICodeBaseEntity;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * 프로그램에서 생산한 자원 정보. Actor가 접근 가능한 대상을 말합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 10.
 */
@Entity
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Resource",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_resource",
                                                                            columnNames = {
                                                                                    "ProductId",
                                                                                    "ResourceCode" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Resource extends AccessEntityBase implements ICodeBaseEntity {

    private static final long serialVersionUID = 3675784179606293494L;

    protected Resource() {}

    public Resource(Product product, String code) {
        this(product, code, code);
    }

    public Resource(Product product, String code, String name) {
        Guard.shouldNotBeNull(product, "product");
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");

        this.product = product;
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "ResourceId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    @NaturalId
    private Product product;

    @Column(name = "ResourceCode", nullable = false, length = 128)
    @NaturalId
    private String code;

    @Column(name = "ResourceName", nullable = false, length = 128)
    private String name;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "ResourceDesc", length = 4000)
    private String description;

    @Column(name = "ResourceExAttr", length = 4000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("isActive", isActive);
    }
}
