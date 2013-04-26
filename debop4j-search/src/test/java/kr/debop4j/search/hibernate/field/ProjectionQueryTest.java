package kr.debop4j.search.hibernate.field;

import kr.debop4j.search.hibernate.model.Employee;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.ScrollableResults;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.search.hibernate.field.ProjectionQueryTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 2:51
 */
public class ProjectionQueryTest extends LuceneTestBase {

    @Test
    public void testLuceneObjectsProjectionWithScroll() throws Exception {
        buildIndex(fts);

        Transaction tx = fts.beginTransaction();
        QueryParser parser = new QueryParser(Version.LUCENE_36, "dept", new StandardAnalyzer(Version.LUCENE_36));
        Query query = parser.parse("dept:ITech");
        FullTextQuery ftq = fts.createFullTextQuery(query, Employee.class);
        ftq.setProjection("id", "lastname", "dept",
                          FullTextQuery.THIS,
                          FullTextQuery.SCORE,
                          FullTextQuery.DOCUMENT,
                          FullTextQuery.ID);

        ScrollableResults projections = ftq.scroll();
        projections.beforeFirst();
        projections.next();
        Object[] projection = projections.get();

        assertThat(projection[0]).isEqualTo(1000);
        assertThat(projection[1]).isEqualTo("Griffin");
        assertThat(projection[2]).isEqualTo("ITech");
        assertThat(projection[3].hashCode()).isEqualTo(fts.get(Employee.class, 1000).hashCode());
        assertThat(projection[4]).isEqualTo(1.0F);
        assertThat(projection[5]).isInstanceOf(Document.class);
        assertThat(projection[6]).isEqualTo(1000);

        assertThat(projections.isFirst()).isTrue();
        assertThat(((Employee) projection[3]).getId()).isEqualTo(1000);

        for (Object element : fts.createQuery("from " + Employee.class.getName()).list())
            fts.delete(element);

        tx.commit();
    }

    private void buildIndex(FullTextSession fts) {
        Transaction tx = fts.beginTransaction();
        Employee e1 = new Employee(1000, "Griffin", "ITech");
        fts.save(e1);

        Employee e2 = new Employee(1001, "Jackson", "Accounting");
        fts.save(e2);

        tx.commit();
        fts.clear();
    }
}
