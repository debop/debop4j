package kr.debop4j.data.ogm.test.associations.manytoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.Jug
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 12:00
 */
@Entity
@Getter
@Setter
public class Jug extends UuidEntityBase {

    public Jug() {}

    public Jug(String name) {
        this.name = name;
    }

    public String name;
}
