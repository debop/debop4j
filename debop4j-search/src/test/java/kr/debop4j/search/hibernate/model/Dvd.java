package kr.debop4j.search.hibernate.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.search.hibernate.model.Dvd
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 23. 오후 5:44
 */
@Entity
@Indexed
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Dvd extends AnnotatedEntityBase {

    private static final long serialVersionUID = 3996311188018341320L;

    @Id
    @DocumentId
    public Integer id;

    @Field
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES)
    private String description;
}
