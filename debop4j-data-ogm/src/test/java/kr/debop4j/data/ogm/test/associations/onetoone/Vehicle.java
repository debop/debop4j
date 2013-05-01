package kr.debop4j.data.ogm.test.associations.onetoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.associations.onetoone.Vehicle
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Vehicle extends UuidEntityBase {

    private String brand;
}
