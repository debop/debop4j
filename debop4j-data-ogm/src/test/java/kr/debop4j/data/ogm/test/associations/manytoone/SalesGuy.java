package kr.debop4j.data.ogm.test.associations.manytoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.SalesGuy
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 12:03
 */
@Entity
@Getter
@Setter
public class SalesGuy extends UuidEntityBase {

    protected SalesGuy() {}

    public SalesGuy(String name) {
        this.name = name;
    }

    private String name;

    @ManyToOne
    private SalesForce salesForce;
}
