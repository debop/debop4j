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
import kr.debop4j.access.model.PreferenceBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 제품의 설정 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Entity
@Table(name = "ProductPreference")
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "ProductPreference",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_productPreference",
                                                                            columnNames = {
                                                                                    "ProductId",
                                                                                    "PrefKey" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProductPreference extends PreferenceBase {

    private static final long serialVersionUID = -8362998094989974826L;

    protected ProductPreference() {}

    public ProductPreference(Product product, String key, String value) {
        super(key, value);
        this.product = product;
    }

    @Id
    @GeneratedValue
    @Column(name = "PreferenceId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(product, getKey());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("product", product);
    }
}
