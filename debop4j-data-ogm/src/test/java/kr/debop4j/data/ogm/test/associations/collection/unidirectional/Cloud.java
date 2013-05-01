package kr.debop4j.data.ogm.test.associations.collection.unidirectional;

import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.test.associations.collection.unidirectional.Cloud
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 12:50
 */
@Entity
@Getter
@Setter
public class Cloud extends UuidEntityBase {

    String type;

    double length;

    @OneToMany
    @JoinTable
    Set<SnowFlake> producedSnowFlakes = Sets.newHashSet();
}
