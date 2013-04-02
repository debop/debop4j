package kr.debop4j.data.ogm.test.massindex;

import kr.debop4j.data.ogm.test.hsearch.Insurance;
import kr.debop4j.data.ogm.test.id.NewsID;
import kr.debop4j.data.ogm.test.massindex.model.IndexedLabel;
import kr.debop4j.data.ogm.test.massindex.model.IndexedNews;
import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.test.TestConstants;
import org.hibernate.search.util.impl.FileHelper;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * hibernate-search-engine 4.3.0-SNAPSHOT 이상에서 테스트가 가능합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 1:17
 */
@Ignore("hibernate-search-engine 4.3.0 이상에서 지원합니다.")
public class SimpleEntityMassIndexingTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ Insurance.class, IndexedNews.class, IndexedLabel.class };
    }

    @After
    public void after() throws Exception {
        super.after();
        FileHelper.delete(getBaseIndexDir());
    }

    @Test
    public void simpleEntityMassIndexing() throws Exception {
        {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            Insurance insurance = new Insurance();
            insurance.setName("Insurance Corporation");
            session.persist(insurance);
            transaction.commit();
            session.clear();
            session.close();
        }
        {
            purgeAll(Insurance.class);
            startAndWaitMassIndexing(Insurance.class);
        }
        {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<Insurance> list = session.createQuery("FROM Insurance ").list();
            assertThat(list).hasSize(1);
            assertThat(list.get(0).getName()).isEqualTo("Insurance Corporation");
            transaction.commit();
            session.clear();
            session.close();
        }
    }

    @Test
    public void entityWithCompositeIdMassIndexing() throws Exception {
        {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            IndexedNews news = new IndexedNews(new NewsID("title", "author"), "content");
            session.persist(news);
            transaction.commit();
            session.clear();
            session.close();
        }
        {
            purgeAll(IndexedNews.class);
            startAndWaitMassIndexing(IndexedNews.class);
        }
        {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<IndexedNews> list = session.createQuery("FROM IndexedNews ").list();
            assertThat(list).hasSize(1);
            assertThat(list.get(0).getContent()).isEqualTo("content");
            assertThat(list.get(0).getNewsId().getTitle()).isEqualTo("title");
            assertThat(list.get(0).getNewsId().getAuthor()).isEqualTo("author");
            transaction.commit();
            session.clear();
            session.close();
        }
    }


    private void startAndWaitMassIndexing(Class<?> entityType) throws InterruptedException {
        FullTextSession session = Search.getFullTextSession(openSession());
        session.createIndexer(entityType).purgeAllOnStart(true).startAndWait();
    }

    private void purgeAll(Class<?> entityType) {
        FullTextSession session = Search.getFullTextSession(openSession());
        session.purgeAll(entityType);
        session.flushToIndexes();
        @SuppressWarnings("unchecked")
        List<Insurance> list = session.createQuery("FROM " + entityType.getSimpleName()).list();
        assertThat(list).hasSize(0);
    }


    protected File getBaseIndexDir() {
        // Make sure no directory is ever reused across the testsuite as Windows might not be able
        // to delete the files after usage. See also
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4715154
        String shortTestName = this.getClass().getSimpleName();

        // the constructor File(File, String) is broken too, see :
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5066567
        // So make sure to use File(String, String) in this case as TestConstants works with absolute paths!
        File indexPath = new File(TestConstants.getIndexDirectory(), shortTestName);
        return indexPath;
    }

    protected void configure(org.hibernate.cfg.Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty("hibernate.search.default.indexBase", getBaseIndexDir().getAbsolutePath());
        cfg.setProperty("hibernate.search.default.directory_provider", "filesystem");
        // Infinispan requires to be set to distribution mode for this test to pass
        // cfg.setProperty("hibernate.ogm.infinispan.configuration_resourcename", "infinispan-dist.xml");
        cfg.setProperty("hibernate.ogm.infinispan.configuration_resourcename", "infinispan-local.xml");
    }
}
