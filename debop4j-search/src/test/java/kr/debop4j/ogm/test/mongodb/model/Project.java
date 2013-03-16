package kr.debop4j.ogm.test.mongodb.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.core.tools.StringTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * kr.debop4j.ogm.test.mongodb.model.Project
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 16.
 */
@Entity
@Getter
@Setter
public class Project extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = 4540128317807586040L;

    @Id
    private String id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @OrderColumn(name = "ordering")
    private List<Module> modules = Lists.newArrayList();

    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
    }

    @Override
    public int hashCode() {
        if (StringTool.isNotEmpty(id))
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
