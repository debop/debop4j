package kr.debop4j.access.repository.product;

import kr.debop4j.access.model.product.Product;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.hibernate.repository.HibernateRepository;
import kr.debop4j.data.hibernate.tools.CriteriaTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Product} 에 대한 Repository 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 12.
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
