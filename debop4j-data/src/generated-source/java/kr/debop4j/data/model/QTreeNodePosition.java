package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTreeNodePosition is a Querydsl query type for TreeNodePosition
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QTreeNodePosition extends BeanPath<TreeNodePosition> {

    private static final long serialVersionUID = 1463375093;

    public static final QTreeNodePosition treeNodePosition = new QTreeNodePosition("treeNodePosition");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final NumberPath<Integer> order = createNumber("order", Integer.class);

    public QTreeNodePosition(String variable) {
        super(TreeNodePosition.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QTreeNodePosition(Path<? extends TreeNodePosition> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QTreeNodePosition(PathMetadata<?> metadata) {
        super(TreeNodePosition.class, metadata);
    }

}

