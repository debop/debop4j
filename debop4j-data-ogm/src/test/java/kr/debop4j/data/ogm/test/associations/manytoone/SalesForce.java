package kr.debop4j.data.ogm.test.associations.manytoone;

import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.SalesForce
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 12:02
 */
@Entity
@Getter
@Setter
public class SalesForce extends UuidEntityBase {

    public SalesForce() {}

    public SalesForce(String corporation) {
        this.corporation = corporation;
    }

    private String corporation;

    @OneToMany(mappedBy = "salesForce", cascade = { CascadeType.ALL })
    Set<SalesGuy> salesGuys = Sets.newHashSet();
}
