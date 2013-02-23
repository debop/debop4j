package kr.debop4j.data.hibernate.forTesting;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.LongAnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * kr.debop4j.data.hibernate.forTesting.LongEntityForTesting
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 21.
 */
@Entity
@Table(name = "LongEntity_ForTesting")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class LongEntityForTesting extends LongAnnotatedEntityBase {

    protected LongEntityForTesting() {}

    public LongEntityForTesting(String code) {
        this.code = code;
    }


    @Column(nullable = false)
    public String code;
    public String name;
    public Integer age;

    @Version
    public int version;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(code, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("code", code)
                .add("name", name)
                .add("age", age);
    }
}
