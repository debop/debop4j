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
import com.google.common.collect.Sets;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 제품에서 사용하는 마스터 코드 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Entity
@Table(name = "ProductCode")
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "ProductCode",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_product_code",
                                                                            columnNames = { "ProductId", "CodeValue" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProductCode extends AccessEntityBase {

    private static final long serialVersionUID = 305315517508305093L;

    protected ProductCode() {}

    public ProductCode(Product product, String code, String name) {
        this.product = product;
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "CodeId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    @NaturalId
    private Product product;

    @Column(name = "CodeValue", nullable = false, length = 128)
    @NaturalId
    private String code;

    @Column(name = "CodeName", nullable = false, length = 255)
    private String name;

    @Column(name = "Descripton", length = 2000)
    private String description;

    //@Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    @OneToMany(mappedBy = "code", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<ProductCodeItem> items = Sets.newHashSet();


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(product, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("product", product);
    }
}