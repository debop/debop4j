package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.simpleentity.Hypothesis
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Hypothesis extends AnnotatedEntityBase {

    @Id
    private String id;

    private String description;

    @Column(name = "pos")
    private int position;
}
