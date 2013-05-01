package kr.debop4j.search.hibernate.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * kr.debop4j.search.hibernate.model.Product
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오후 3:48
 */
@Entity
@Indexed
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Product extends AnnotatedEntityBase {
    private static final long serialVersionUID = -4584574576851886802L;

    @Id
    @GeneratedValue
    @DocumentId
    private long id;

    @Field(index = Index.YES, store = Store.YES)
    private String title;

    @Field(index = Index.YES, store = Store.YES)
    private String description;


}
