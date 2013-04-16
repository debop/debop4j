package kr.debop4j.data.mongodb.dao;

import com.google.common.collect.Sets;
import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * kr.debop4j.data.mongodb.dao.Tournament
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 5:52
 */
@Entity
@Indexed
@Getter
@Setter
public class Tournament extends UuidEntityBase {
    private static final long serialVersionUID = 3431486937664639007L;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String tournament;

    @ManyToMany(mappedBy = "tournaments", fetch = FetchType.EAGER)
    @ContainedIn
    Set<Player> players = Sets.newHashSet();
}
