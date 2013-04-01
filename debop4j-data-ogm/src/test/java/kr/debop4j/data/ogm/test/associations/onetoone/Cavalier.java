package kr.debop4j.data.ogm.test.associations.onetoone;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * kr.debop4j.data.ogm.test.associations.onetoone.Cavalier
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Cavalier extends UuidEntityBase {

    private String name;

    @OneToOne
    @JoinColumn(name = "horse_id")
    private Horse horse;
}
