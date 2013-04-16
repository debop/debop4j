package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.simpleentity.Helicopter
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Helicopter extends UuidEntityBase {

    private static final long serialVersionUID = -858241709367877857L;

    private String name;
}
