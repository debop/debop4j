package kr.debop4j.search.hibernate.model;

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * kr.debop4j.search.hibernate.model.VocAttr
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 4. 29. 오후 4:05
 */
@Entity
@Indexed
@Getter
@Setter
public class VocAttr extends AnnotatedEntityBase {

    private static final long serialVersionUID = 5459929566638977310L;

    protected VocAttr() {}

    public VocAttr(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Id
    @GeneratedValue
    @DocumentId
    private Long id;

    @Field
    private String name;

    @Field
    private String value;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(name);
    }
}
