package kr.debop4j.data.ogm.test.associations.onetoone;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * kr.debop4j.data.ogm.test.associations.onetoone.Wife
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Wife extends UuidEntityBase {

    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "wife")
    private Husband husband;
}
