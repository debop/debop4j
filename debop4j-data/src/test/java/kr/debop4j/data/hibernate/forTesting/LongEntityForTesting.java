package kr.debop4j.data.hibernate.forTesting;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * kr.debop4j.data.hibernate.forTesting.LongEntityForTesting
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Entity
@Table(name = "LongEntity_ForTesting")
@Getter
@Setter
public class LongEntityForTesting extends AnnotatedEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    public String code;
    public String name;
    public Integer age;

    @Version
    public int version;

    public LongEntityForTesting(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(code, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("age", age);
    }
}
