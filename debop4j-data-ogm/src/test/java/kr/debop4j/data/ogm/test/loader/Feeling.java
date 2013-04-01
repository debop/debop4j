package kr.debop4j.data.ogm.test.loader;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.loader.Feeling
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
public class Feeling extends UuidEntityBase {

    private static final long serialVersionUID = -5342578095971806887L;

    @Getter
    @Setter
    private String name;
}
