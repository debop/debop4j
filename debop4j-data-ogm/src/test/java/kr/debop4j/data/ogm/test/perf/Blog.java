package kr.debop4j.data.ogm.test.perf;

import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * kr.debop4j.data.ogm.test.perf.Blog
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Blog extends AnnotatedEntityBase {

    @Id
    private Integer id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "blog")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<BlogEntry> entries = new HashSet<BlogEntry>();
}
