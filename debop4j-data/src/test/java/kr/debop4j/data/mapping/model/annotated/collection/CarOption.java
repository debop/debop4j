package kr.debop4j.data.mapping.model.annotated.collection;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * org.annotated.mapping.domain.model.collection.CarOption
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 15.
 */
@Embeddable
@Getter
@Setter
public class CarOption extends ValueObjectBase {

    private static final long serialVersionUID = -4205766412047788934L;

    protected CarOption() {
    }

    public CarOption(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Column(name = "OptionName")
    private String name;

    @Column(name = "OptionValue")
    private Integer value;

    @Override
    public int hashCode() {
        return HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("value", value);
    }
}
