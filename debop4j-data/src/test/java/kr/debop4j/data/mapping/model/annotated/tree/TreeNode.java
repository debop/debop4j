package kr.debop4j.data.mapping.model.annotated.tree;

import com.google.common.base.Objects;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.TreeEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * kr.debop4j.data.mapping.model.annotated.tree.TreeNode
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Entity
@Table(name = "A_TREENODE")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class TreeNode extends TreeEntityBase<TreeNode, Integer> {

    private String title;
    private String data;
    private String description;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return HashTool.compute(title, data);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("title", title)
                .add("data", data)
                .add("description", description);
    }
}
