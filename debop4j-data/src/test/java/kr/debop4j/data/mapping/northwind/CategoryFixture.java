package kr.debop4j.data.mapping.northwind;

import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.mapping.northwind.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.CategoryFixture
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Slf4j
public class CategoryFixture extends NorthwindDbTestFixtureBase {

    public IHibernateRepository<Category> getCategoryDao() {
        return getDao(Category.class);
    }

    @Test
    public void findAll() {
        List<Category> categories = getCategoryDao().getAll();
        print(categories);
    }
}
