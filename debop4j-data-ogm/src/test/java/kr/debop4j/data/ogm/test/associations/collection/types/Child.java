package kr.debop4j.data.ogm.test.associations.collection.types;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.associations.collection.type.Child
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오전 11:12
 */
@Entity
@Getter
@Setter
public class Child extends UuidEntityBase {

    protected Child() {}

    public Child(String name) {
        this.name = name;
    }

    private String name;

}
