package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;

import javax.persistence.Id;

/**
 * Annotated Entity 중에 Id의 수형이 Integer인 엔티티에 대한 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
public class IntAnnotatedEntityBase extends AnnotatedEntityBase {

    private Integer id;

    @Id
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return (id != null) ? HashTool.compute(id) : super.hashCode();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id);
    }
}
