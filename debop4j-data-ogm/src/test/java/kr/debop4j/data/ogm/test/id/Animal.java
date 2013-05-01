package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.id.Animal
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 2:16
 */
@Entity
@Getter
@Setter
public class Animal extends AnnotatedEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String name;

    private String species;
}
