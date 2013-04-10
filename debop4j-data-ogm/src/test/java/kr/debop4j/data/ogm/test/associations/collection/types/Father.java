package kr.debop4j.data.ogm.test.associations.collection.types;

import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * kr.debop4j.data.ogm.test.associations.collection.type.Father
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오전 11:13
 */
@Entity
@Getter
@Setter
public class Father extends UuidEntityBase {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "Father_Child")
    @OrderColumn(name = "birthorder")
    List<Child> orderedChildren = new ArrayList<Child>();
}
