package kr.debop4j.data.ogm.test.associations.onetoone;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * kr.debop4j.data.ogm.test.associations.onetoone.Wheel
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Wheel extends AnnotatedEntityBase {

    @Id
    private String id;

    private double diameter;

    @OneToOne(cascade = CascadeType.PERSIST)
    @PrimaryKeyJoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @MapsId
    private Vehicle vehicle;
}
