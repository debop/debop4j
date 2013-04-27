package kr.debop4j.search.hibernate.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.kr.KoreanAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

/**
 * kr.debop4j.search.hibernate.model.Tweet
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 28.
 */
@Entity
@Table(name = "Tweet")
@Indexed
@Analyzer(impl = KoreanAnalyzer.class)
@Getter
@Setter
public class Tweet extends AnnotatedEntityBase {

    private static final long serialVersionUID = 3617783931787945059L;

    @Id
    @GeneratedValue
    @Column(name = "tweetId")
    @DocumentId
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @Column(name = "username")
    @Field
    private String username;

    @Lob
    @Column(name = "tweetText", length = 4000)
    @Field(index = Index.YES, store = Store.YES)
    @Boost(3)
    private String text;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(username, text);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("username", username)
                .add("text", text);
    }
}

