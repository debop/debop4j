package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.simpleentity.Helicopter
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Indexed
@Getter
@Setter
public class Helicopter extends UuidEntityBase {

    private static final long serialVersionUID = -858241709367877857L;

    private String name;
}
