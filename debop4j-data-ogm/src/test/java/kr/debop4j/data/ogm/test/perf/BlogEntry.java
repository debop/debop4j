package kr.debop4j.data.ogm.test.perf;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * kr.debop4j.data.ogm.test.perf.BlogEntry
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class BlogEntry extends AnnotatedEntityBase {

    @Id
    private Long id;

    private String title;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Blog blog;

    private String content;
}
