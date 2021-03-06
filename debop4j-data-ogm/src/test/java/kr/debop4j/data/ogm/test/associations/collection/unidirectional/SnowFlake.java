package kr.debop4j.data.ogm.test.associations.collection.unidirectional;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.associations.collection.unidirectional.SnowFlake
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 12:50
 */
@Entity
@Getter
@Setter
public class SnowFlake extends UuidEntityBase {

    private String description;
}
