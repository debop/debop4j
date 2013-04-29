package kr.debop4j.search.hibernate.field;

import kr.debop4j.search.dao.HibernateSearchDao;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * LuceneTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 2:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LuceneNativeConfig.class })
public abstract class LuceneTestBase {

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected SessionFactory sessionFactory;

    protected FullTextSession fts;

    @Before
    public void before() {
        fts = Search.getFullTextSession(sessionFactory.openSession());
    }

    @After
    public void after() {
        if (fts != null) {
            fts.flush();
            fts.flushToIndexes();

            fts.close();
            fts = null;
        }
    }

    public HibernateSearchDao getSearchDao() {
        return appContext.getBean(HibernateSearchDao.class);
    }
}
