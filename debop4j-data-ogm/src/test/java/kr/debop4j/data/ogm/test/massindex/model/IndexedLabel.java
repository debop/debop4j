package kr.debop4j.data.ogm.test.massindex.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * kr.debop4j.data.ogm.test.massindex.model.IndexedLabel
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 1:16
 */
@Entity
@Indexed
@Getter
@Setter
public class IndexedLabel extends AnnotatedEntityBase {

    private static final long serialVersionUID = -3215016392141137416L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public IndexedLabel() {}

    public IndexedLabel(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public int hashCode() {
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name);
    }
}
