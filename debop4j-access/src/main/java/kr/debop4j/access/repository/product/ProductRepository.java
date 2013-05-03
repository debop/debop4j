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

package kr.debop4j.access.repository.product;

import kr.debop4j.access.model.product.Product;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.impl.HibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Product} 에 대한 Repository 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 12.
 */
@Repository
@Slf4j
public class ProductRepository extends HibernateRepository<Product> {

    public ProductRepository() {
        super(Product.class);
    }

    public DetachedCriteria buildCriteria(String code, String name, Boolean active) {
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);

        if (StringTool.isNotEmpty(code))
            CriteriaTool.addEq(dc, "code", code);

        if (StringTool.isNotEmpty(name))
            CriteriaTool.addEq(dc, "name", name);

        if (active != null)
            CriteriaTool.addEq(dc, "active", active);

        return dc;
    }

    public Product findByCode(String code) {
        DetachedCriteria dc = buildCriteria(code, null, null);
        return findOne(dc);
    }

    public List<Product> findByName(String name) {
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        CriteriaTool.addILike(dc, "name", name);
        return find(dc);
    }

    public List<Product> findAllByActive(boolean active) {
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        return find(buildCriteria(null, null, active));
    }
}
