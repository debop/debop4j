package kr.debop4j.data.hibernate.search;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.impl.FullTextSessionImpl;
import org.hibernate.search.model.SearchItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * org.hibernate.search.SearchTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 5
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class SearchTest {

    @Autowired
    SessionFactory sessionFactory;

    private Session session;
    private FullTextSession fullTextSession;

    @Before
    public void before() {
        session = sessionFactory.openSession();
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

        List<SearchItem> list = (List<SearchItem>) fullTextSession.createCriteria(SearchItem.class).add(Restrictions.ilike("title", "제목")).list();

        Assert.assertNotNull(list);
        for (SearchItem loaded : list)
            System.out.println(loaded.getTitle());
    }
}
