package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 트리 형식의 자료 구조의 하나의 노드의 위치를 나타냅니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 13.
 */
@Embeddable
@Getter
@Setter
public class TreeNodePosition extends ValueObjectBase {

    private static final long serialVersionUID = 7724440843329055902L;

    @Column(name = "TreeLevel")
    private Integer level;

    @Column(name = "TreeOrder")
    private Integer order;

    public TreeNodePosition() {
        this(0, 0);
    }

    public TreeNodePosition(Integer level, Integer order) {
        this.level = level;
        this.order = order;
    }

    public TreeNodePosition(TreeNodePosition nodePosition) {
        Guard.shouldNotBeNull(nodePosition, "nodePosition");
        this.level = nodePosition.level;
        this.order = nodePosition.order;
    }

    public void setPosition(int level, int order) {
        this.level = level;
        this.order = order;
    }

    public void setPosition(TreeNodePosition nodePosition) {
        Guard.shouldNotBeNull(nodePosition, "nodePosition");
        this.level = nodePosition.level;
        this.order = nodePosition.order;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(level, order);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("level", level)
                .add("order", order);
    }
}
