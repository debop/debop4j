package kr.debop4j.data.ogm.test.queries;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

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

    private static final long serialVersionUID = -6968612655671062151L;

    @Id
    private String id;

    @Field( analyze = Analyze.NO )
    private String description;

    @Column( name = "pos" )
    @Field
    @NumericField
    private int position;
}
