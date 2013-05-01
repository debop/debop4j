package kr.debop4j.search.hibernate.query;

import kr.debop4j.search.hibernate.SearchTestBase;
import kr.debop4j.search.hibernate.model.Dvd;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.fest.assertions.Assertions;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * kr.debop4j.search.hibernate.query.QueryParserTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 23. 오후 5:06
 */
@Slf4j
public class QueryParserTest extends SearchTestBase {

    @Test
    public void manualAnalyzer() throws Exception {
        String search = "The Little Pony";
        Reader reader = new StringReader(search);
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

        TokenStream stream = analyzer.tokenStream("title", reader);
        BooleanQuery query = new BooleanQuery();

        while (stream.incrementToken()) {
            CharTermAttribute termAttribute = stream.getAttribute(CharTermAttribute.class);
            if (termAttribute.length() != 0) {
                String term = new String(termAttribute.buffer(), 0, termAttribute.length());
                query.add(new TermQuery(new Term("title", term)), BooleanClause.Occur.SHOULD);
            }
        }
        Assertions.assertThat(query.toString()).isEqualTo("title:little title:pony");
        System.out.println(query.toString());
    }


    String[] descs = new String[] {
            "he hits the road as a traveling salesman",
            "he's not a computer salesman",
            "a traveling salesman touting the wave of the future",
            "transforms into an aggressive, high-risk salesman",
            "a once-successful salesman"
    };


    private void buildIndex(FullTextSession fts) {
        Transaction tx = fts.beginTransaction();
        for (int i = 0; i < descs.length; i++) {
            Dvd dvd = new Dvd();
            dvd.setDescription(descs[i]);
            dvd.setId(i);
            fts.saveOrUpdate(dvd);
        }
        tx.commit();
        fts.clear();
    }

    @Test
    public void testTermQuery() throws Exception {
        buildIndex(fts);

        Transaction tx = fts.beginTransaction();
        try {
            String userInput = "salesman";
            Term term = new Term("description", userInput);
            TermQuery query = new TermQuery(term);

            log.debug("query: " + query.toString());

            FullTextQuery ftq = fts.createFullTextQuery(query, Dvd.class);
            @SuppressWarnings("unchecked")
            List<Dvd> results = ftq.list();

            Assertions.assertThat(results.size()).isEqualTo(descs.length);
            Assertions.assertThat(results.get(0).getDescription()).isEqualTo(descs[1]);

            for (Dvd dvd : results) {
                log.debug("Dev: " + dvd.getDescription());
            }

            for (Object element : fts.createQuery("from " + Dvd.class.getName()).list()) {
                fts.delete(element);
            }
        } finally {
            tx.commit();
        }
    }
}
