package kr.debop4j.data.ogm.test.associations.manytoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.Member
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 12:01
 */
@Entity
@Getter
@Setter
public class Member extends UuidEntityBase {

    protected Member() {}

    public Member(String name) {
        this.name = name;
    }

    private String name;

    @ManyToOne
    private Jug memberOf;
}
