package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.simpleentity.Hypothesis
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Indexed
@Getter
@Setter
public class Hypothesis extends AnnotatedEntityBase {

    @Id
    private String id;

    private String description;

    @Column(name = "pos")
    private int position;
}
