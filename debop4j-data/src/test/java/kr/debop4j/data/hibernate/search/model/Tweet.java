package kr.debop4j.data.hibernate.search.model;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

/**
 * org.hibernate.search.model.Tweet
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 5
 */
@Entity
@Table(name = "Tweet")
@Indexed
public class Tweet extends AnnotatedEntityBase {

    @Getter
    @Id
    @GeneratedValue
    @Column(name = "tweet_id")
    @DocumentId
    private Integer id;

    @Getter
    @Setter
    @Column(name = "user_name")
    @Field
    private String username;

    @Getter
    @Setter
    @Lob
    @Column(name = "tweet_text")
    @Field(name = "tweet_text", index = Index.YES, store = Store.YES)
    @Boost(3)
    private String text;
}
