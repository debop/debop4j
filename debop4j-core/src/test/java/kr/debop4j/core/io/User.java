package kr.debop4j.core.io;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;


/**
 * pudding.pudding.commons.core.io.User
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 10. 4.
 */
@Getter
@Setter
public class User extends ValueObjectBase {

    private static final long serialVersionUID = -1375942267796202939L;

    private String name;
    private String employeeNumber;
    private String address;

    @Override
    public int hashCode() {
        return HashTool.compute(name, employeeNumber);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("name", name)
                .add("employeeNumber", employeeNumber)
                .add("address", address);
    }
}
