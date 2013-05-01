package kr.debop4j.core.io;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * pudding.pudding.commons.core.io.Company
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 10. 4.
 */
@Getter
@Setter
public class Company extends ValueObjectBase {

    private static final long serialVersionUID = 4442244029750273886L;

    private String code;
    private String name;
    private String description;
    private long amount;

    private final List<User> users = Lists.newLinkedList();


    @Override
    public int hashCode() {
        return HashTool.compute(code, name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("code", code)
                .add("name", name)
                .add("amount", amount);
    }
}
