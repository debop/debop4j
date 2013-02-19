package kr.debop4j.data.mapping.model.hbm.collection;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

/**
 * org.hibernate.mapping.domain.model.collection.Image
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
@Getter
@Setter
public class Image extends ValueObjectBase {

    private static final long serialVersionUID = 7513180521707042792L;

    private Item item;

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
