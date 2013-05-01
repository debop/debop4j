package kr.debop4j.data.ogm.test.associations.manytoone;

import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.test.associations.manytoone.Brewery
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오전 11:58
 */
@Entity
@Getter
@Setter
public class Brewery extends UuidEntityBase {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "brewery_id")
    Set<Beer> beers = Sets.newHashSet();
}
