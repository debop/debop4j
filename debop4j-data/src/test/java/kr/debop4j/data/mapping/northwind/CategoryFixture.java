package kr.debop4j.data.mapping.northwind;

import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.mapping.northwind.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.data.mapping.northwind.CategoryFixture
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 23.
 */
@Slf4j
public class CategoryFixture extends NorthwindDbTestFixtureBase {

    public IHibernateRepository<Category> getCategoryDao() {
        return getDao(Category.class);
    }

    @Test
    public void findAll() {
        List<Category> categories = getCategoryDao().findAll();
        print(categories);
    }

    @Test
    public void getSroll() {
        ScrollableResults scroll = getCategoryDao().getScroll(DetachedCriteria.forClass(Category.class));
        while (scroll.next()) {
            log.debug("Category=" + scroll.get(0));
        }
        scroll.close();
    }
}
