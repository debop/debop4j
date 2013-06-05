package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTreeEntityBase is a Querydsl query type for TreeEntityBase
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QTreeEntityBase extends EntityPathBase<TreeEntityBase<? extends ITreeEntity<?>, ? extends java.io.Serializable>> {

    private static final long serialVersionUID = -2036112514;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTreeEntityBase treeEntityBase = new QTreeEntityBase("treeEntityBase");

    public final QEntityBase _super = new QEntityBase(this);

    public final SetPath<ITreeEntity<?>, SimplePath<ITreeEntity<?>>> children = this.<ITreeEntity<?>, SimplePath<ITreeEntity<?>>>createSet("children", ITreeEntity.class, SimplePath.class, PathInits.DIRECT2);

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    public final QTreeNodePosition nodePosition;

    public final SimplePath<ITreeEntity<?>> parent = createSimple("parent", ITreeEntity.class);

    //inherited
    public final BooleanPath persisted = _super.persisted;

    @SuppressWarnings("all")
    public QTreeEntityBase(String variable) {
        this((Class)TreeEntityBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QTreeEntityBase(Path<? extends TreeEntityBase> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTreeEntityBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    @SuppressWarnings("all")
    public QTreeEntityBase(PathMetadata<?> metadata, PathInits inits) {
        this((Class)TreeEntityBase.class, metadata, inits);
    }

    public QTreeEntityBase(Class<? extends TreeEntityBase<? extends ITreeEntity<?>, ? extends java.io.Serializable>> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nodePosition = inits.isInitialized("nodePosition") ? new QTreeNodePosition(forProperty("nodePosition")) : null;
    }

}

