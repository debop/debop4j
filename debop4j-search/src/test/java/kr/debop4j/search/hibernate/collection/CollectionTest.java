package kr.debop4j.search.hibernate.collection;

import kr.debop4j.search.hibernate.SearchTestBase;
import kr.debop4j.search.hibernate.model.Voc;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.fest.assertions.Assertions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * kr.debop4j.search.hibernate.collection.CollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 29. 오후 4:16
 */
@Slf4j
public class CollectionTest extends SearchTestBase {

    private static final String MEMO =
            "ASP.NET 웹 어플리케이션은 어플리케이션 Lifecycle, Page의 Lifecycle 에 상세한 event 를 정의하고 있어, event handler를 정의하면, 여러가지 선처리나 후처리를 수행할 수 있습니다.";

    @Test
    public void saveVocMemo() throws Exception {
        try {
            Voc voc = new Voc();

            voc.setCreateAt(new Date());
            voc.setCreator("배성혁");
            voc.setMemo(MEMO);
            voc.setSrtype("문의");

            voc.addAttr("부서", "플랫폼");
            voc.addAttr("담당", "송길주");

            fts.saveOrUpdate(voc);
            fts.flush();
            fts.flushToIndexes();
            fts.clear();

            Voc loaded = (Voc) fts.get(Voc.class, voc.getId());
            Assertions.assertThat(loaded).isNotNull();
            Assertions.assertThat(loaded.getAttrs().size()).isEqualTo(2);
            fts.clear();

            BooleanQuery luceneQuery = new BooleanQuery();
            luceneQuery.add(new TermQuery(new Term("attrs.name", "담당")), BooleanClause.Occur.MUST);
            luceneQuery.add(new TermQuery(new Term("attrs.value", "송길주")), BooleanClause.Occur.MUST);
            luceneQuery.add(new WildcardQuery(new Term("memo", "정의*")), BooleanClause.Occur.MUST);

            FullTextQuery ftq = fts.createFullTextQuery(luceneQuery, Voc.class);

            System.out.println("Query=" + luceneQuery.toString());

            List<Voc> vocs = (List<Voc>) ftq.list();
            Assertions.assertThat(vocs.size()).isEqualTo(1);

            for (Voc v : vocs) {
                System.out.println(v.getId() + ":" + v.getMemo());
            }

            QueryBuilder builder = getSearchDao().getQueryBuilder(Voc.class);
            Query query =
                    builder.bool()
                            .must(builder.keyword().onField("attrs.name").matching("담당").createQuery())
                            .must(builder.keyword().onField("attrs.value").matching("송길주").createQuery())
                            .must(builder.keyword().wildcard().onField("memo").matching("정의*").createQuery())
                            .createQuery();
            vocs = (List<Voc>) fts.createFullTextQuery(query, Voc.class).list();
            Assertions.assertThat(vocs.size()).isEqualTo(1);
            for (Voc v : vocs) {
                System.out.println(v.getId() + ":" + v.getMemo());
            }

        } finally {
            getSearchDao().deleteAll(Voc.class);
            getSearchDao().clearIndex(Voc.class);
        }
    }
}
