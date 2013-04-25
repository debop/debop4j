package kr.debop4j.search.hibernate.score;

import kr.debop4j.search.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * kr.debop4j.search.hibernate.score.ScoreTestBase
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 3:52
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public abstract class ScoreTestBase {
    @Inject
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
}
