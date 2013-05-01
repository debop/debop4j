package kr.debop4j.search.hibernate.score;

import kr.debop4j.search.hibernate.SearchTestBase;
import kr.debop4j.search.hibernate.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.junit.Test;

import java.util.List;

/**
 * kr.debop4j.search.hibernate.score.TestSalesmanSearch
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오후 3:53
 */
@Slf4j
public class TestSalesmanSearch extends SearchTestBase {

    @Test
    public void searchProduct() throws Exception {
        Transaction tx = fts.beginTransaction();

        Query query = new TermQuery(new Term("description", "salesman"));
        log.debug("query=" + query.toString());

        FullTextQuery ftq = fts.createFullTextQuery(query, Product.class);
        ftq.setProjection(FullTextQuery.DOCUMENT,
                          FullTextQuery.SCORE,
                          FullTextQuery.DOCUMENT_ID,
                          FullTextQuery.EXPLANATION);

        List<Object[]> results = ftq.list();


        tx.commit();
    }
}
