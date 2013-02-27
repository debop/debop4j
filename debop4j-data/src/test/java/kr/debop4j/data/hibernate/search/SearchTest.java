package kr.debop4j.data.hibernate.search;

import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.AppConfig;
import kr.debop4j.data.hibernate.search.model.SearchItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.impl.FullTextSessionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Hibernate Search 를 테스트합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 5
 */
@Slf4j
public class SearchTest {

    @BeforeClass
    public static void beforeClass() {
        if (Springs.isNotInitialized())
            Springs.initByAnnotatedClasses(AppConfig.class);
    }

    @Getter(lazy = true)
    private final SessionFactory sessionFactory = Springs.getFirstBeanByType(SessionFactory.class);

    private Session session;
    private FullTextSession fullTextSession;

    @Before
    public void before() {
        session = getSessionFactory().openSession();
        fullTextSession = new FullTextSessionImpl(session);
    }

    @Test
    public void firstSearch() {

        SearchItem item = new SearchItem();
        item.setTitle("아이템 제목");
        item.setDescription("아이템 설명");
        item.setEan("USD");
        item.setImageURL("http://debop.blogspot.com");

        fullTextSession.save(item);
        fullTextSession.flush();
        fullTextSession.clear();

        List<SearchItem> list = (List<SearchItem>)
                fullTextSession
                        .createCriteria(SearchItem.class)
                        .add(Restrictions.ilike("title", "제목"))
                        .list();

        Assert.assertNotNull(list);
        for (SearchItem loaded : list)
            System.out.println(loaded.getTitle());
    }
}
