package kr.debop4j.data.mongodb.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * org.hibernate.ogm.test.mongodb.model.Module
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 23. 오후 2:21
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Module extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1425319711549181974L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Setter(AccessLevel.PROTECTED)
    private String id;

    @Index(name = "ix_module_name")
    private String name;

    @Override
    public int hashCode() {
        return (isPersisted()) ? HashTool.compute(id) : HashTool.compute(name);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("name", name);
    }
}
