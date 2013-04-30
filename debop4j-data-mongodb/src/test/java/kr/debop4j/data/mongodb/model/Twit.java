package kr.debop4j.data.mongodb.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

/**
 * hibernate-ogm 은 Hibernate의 criteria를 지원하지 않습니다. 이를 보조하기 위해 hibernate-search를 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 3:00
 */
@Entity
@Indexed
@Getter
@Setter
public class Twit extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1831686112282898189L;

    @Id
    @DocumentId
    private Long id;

    @Column(name = "UserName")
    @Field(index = Index.YES)
    @Boost(1.5f)
    private String username;

    @Column(name = "Text", length = 300)
    @Field
    @Analyzer(impl = KoreanAnalyzer.class)
    private String text;

    @DateBridge(resolution = Resolution.SECOND)
    @Temporal(TemporalType.TIMESTAMP)
    @Field(analyze = Analyze.NO)
    private Date createdAt;
}
