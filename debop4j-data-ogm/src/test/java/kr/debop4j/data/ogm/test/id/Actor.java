package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.ogm.test.id.Actor
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 2:13
 */
@Entity
@Getter
@Setter
public class Actor extends AnnotatedEntityBase {

    static final transient int INITIAL_VALUE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actorSequenceGenerator")
    @SequenceGenerator(name = "actorSequenceGenerator",
                       sequenceName = "actor_sequence_name",
                       initialValue = INITIAL_VALUE,
                       allocationSize = 10)
    private Long id;

    private String name;

    private String bestMovieTitle;


}
