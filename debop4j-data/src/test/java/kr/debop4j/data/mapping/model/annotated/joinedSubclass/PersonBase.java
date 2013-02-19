package kr.debop4j.data.mapping.model.annotated.joinedSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.joinedSubclass.PersonBase
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 8.
 */
@Entity
@Table(name = "JS_PERSON")
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicInsert
@DynamicUpdate
public abstract class PersonBase extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1470594196996731366L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERSON_ID")
    private Long id;

    @Column(name = "PERSON_NAME", nullable = false, length = 256)
    private String name;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(name, company);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("company", company);
    }
}
