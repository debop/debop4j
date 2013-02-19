package kr.debop4j.data.mapping.model.annotated.collection;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parent;

import javax.persistence.Embeddable;

/**
 * org.annotated.mapping.domain.model.collection.JpaImage
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
@Getter
@Setter
@Embeddable
public class JpaImage extends ValueObjectBase {

    private static final long serialVersionUID = -40661152641816441L;

    @Parent
    private JpaItem item;

    private String name;

    private String filename;

    private Integer sizeX;

    private Integer sizeY;

    @Override
    public int hashCode() {
        return HashTool.compute(name, filename, sizeX, sizeY);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("filename", filename)
                .add("sizeX", sizeX)
                .add("sizeY", sizeY);
    }
}
