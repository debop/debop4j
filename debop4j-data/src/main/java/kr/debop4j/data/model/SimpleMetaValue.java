package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;


/**
 * 메타 정보를 표현하는 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 19
 */
@Getter
@Setter
public class SimpleMetaValue extends ValueObjectBase implements IMetaValue {

    private static final long serialVersionUID = -6675942606392780717L;

    public static final SimpleMetaValue Empty = new SimpleMetaValue("");

    private String value;
    private String label;
    private String description;
    private String exAttr;

    public SimpleMetaValue() {
        this("");
    }

    public SimpleMetaValue(Object value) {
        this.value = Guard.firstNotNull(value, "").toString();
    }

    public SimpleMetaValue(SimpleMetaValue metaValue) {
        if (metaValue != null) {
            this.value = metaValue.value;
            this.label = metaValue.label;
            this.description = metaValue.description;
            this.exAttr = metaValue.exAttr;
        }
    }


    @Override
    public int hashCode() {
        return HashTool.compute(value);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("value", value)
                .add("label", label)
                .add("description", description)
                .add("exAttr", exAttr);
    }
}
