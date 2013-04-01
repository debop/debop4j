package kr.debop4j.data.ogm.test;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 엔티티의 Identifier의 수형이 UUID 인 엔티티입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 31. 오전 2:19
 */
@Entity
@Getter
@Setter
public class UuidEntityBase extends AnnotatedEntityBase {
    private static final long serialVersionUID = 7507710661157374715L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    @Override
    public int hashCode() {
        return HashTool.compute(id);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("id", id);
    }
}
