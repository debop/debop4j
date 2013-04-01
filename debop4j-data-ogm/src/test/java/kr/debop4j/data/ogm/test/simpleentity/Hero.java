package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.simpleentity.Hero
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Hero extends AnnotatedEntityBase {

    @Id
    private String name;
}


