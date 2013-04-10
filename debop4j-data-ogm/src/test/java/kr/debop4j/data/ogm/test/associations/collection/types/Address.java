package kr.debop4j.data.ogm.test.associations.collection.types;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.associations.collection.type.Address
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 31. 오전 2:18
 */
@Entity
@Getter
@Setter
public class Address extends UuidEntityBase {

    private String city;

}
