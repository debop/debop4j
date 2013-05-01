package kr.debop4j.search.hibernate.model;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.search.hibernate.model.Animal
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 25. 오후 2:12
 */
@Entity
@Indexed
// @Analyzer(impl=StandardAnalyzer.class)
// @Analyzer(impl = CJKAnalyzer.class)
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Animal extends AnnotatedEntityBase {

    private static final long serialVersionUID = -857730019219332508L;
    @Id
    @DocumentId
    private Integer id;

    @Field(store = Store.YES, index = Index.YES)
    private String name;

    @Override
    public int hashCode() {
        return HashTool.compute(id);
    }
}
