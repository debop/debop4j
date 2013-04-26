package kr.debop4j.search.hibernate.field;

import kr.debop4j.search.hibernate.model.Animal;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.junit.Test;

/**
 * NonShardsTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 25. 오후 2:26
 */
@Slf4j

public class NonShardsTest extends LuceneTestBase {

    @Test
    public void testNoShards() throws Exception {
        buildIndex(fts);

    }

    private void buildIndex(FullTextSession fts) {
        Transaction tx = fts.beginTransaction();
        Animal a = new Animal();
        a.setId(1);
        a.setName("Elephant");
        fts.persist(a);

        a = new Animal();
        a.setId(2);
        a.setName("Bear");
        fts.persist(a);

        tx.commit();
        fts.clear();
    }
}
