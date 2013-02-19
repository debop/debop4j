package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;

import javax.persistence.Id;

/**
 * Annotated Entity 중에 Id의 수형이 Long인 엔티티에 대한 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
public abstract class LongAnnotatedEntityBase extends AnnotatedEntityBase {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
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
