package kr.debop4j.data.ogm.test.associations.collection.types;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.test.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.test.associations.collection.types.User
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오전 11:16
 */
@Entity
@Getter
@Setter
public class User extends UuidEntityBase {

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "User_Address")
    @MapKeyColumn(name = "nick")
    @Fetch(FetchMode.SUBSELECT)
    private Map<String, Address> addresses = Maps.newHashMap();

    @ElementCollection
    @JoinTable(name = "Nicks", joinColumns = @JoinColumn(name = "user_id"))
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    Set<String> nicknames = Sets.newHashSet();
}
