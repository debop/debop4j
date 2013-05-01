package kr.debop4j.data.mapping.model.annotated.onetomany;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

/**
 * org.annotated.mapping.domain.model.onetomany.OneToMany_Item
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 4.
 */
@Entity
@Table(name = "JPA_ONE_TO_MANY_ITEM")
public class OneToMany_Item extends JpaEntityBase {

    private static final long serialVersionUID = -6749071403586052068L;

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "item", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    private Set<OneToMany_Bid> bids = Sets.newHashSet();

    @Override
    public int hashCode() {
        return isPersisted() ? HashTool.compute(id) : HashTool.compute(name);

    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("description", description);
    }
}
