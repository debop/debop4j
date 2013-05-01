package kr.debop4j.data.mapping.model.annotated.joinedSubclass;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;

import javax.persistence.*;

/**
 * org.annotated.mapping.domain.model.joinedSubclass.Company
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 8.
 */
@Entity
@Table(name = "JS_COMPANY")
public class Company extends AnnotatedEntityBase {

    private static final long serialVersionUID = -7165454377601935859L;
    @Id
    @GeneratedValue
    @Column(name = "COMPANY_ID")
    private Long id;

    @Column(name = "COMPANY_NAME")
    private String name;


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name);
    }
}
