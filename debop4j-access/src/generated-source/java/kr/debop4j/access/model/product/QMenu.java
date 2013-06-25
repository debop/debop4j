package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QMenu is a Querydsl query type for Menu */
@Generated( "com.mysema.query.codegen.EntitySerializer" )
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = 821609776;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenu menu = new QMenu("menu");

    public final kr.debop4j.data.model.QAnnotatedEntityBase _super = new kr.debop4j.data.model.QAnnotatedEntityBase(this);

    public final BooleanPath active = createBoolean("active");

    public final SetPath<Menu, QMenu> children = this.<Menu, QMenu>createSet("children", Menu.class, QMenu.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final kr.debop4j.access.model.organization.QCompany company;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.debop4j.data.model.QTreeNodePosition nodePosition;

    public final QMenu parent;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QProduct product;

    public final StringPath title = createString("title");

    public final DateTimePath<org.joda.time.DateTime> updateTimestamp = createDateTime("updateTimestamp", org.joda.time.DateTime.class);

    public final StringPath url = createString("url");

    public QMenu(String variable) {
        this(Menu.class, forVariable(variable), INITS);
    }

    @SuppressWarnings( "all" )
    public QMenu(Path<? extends Menu> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMenu(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMenu(PathMetadata<?> metadata, PathInits inits) {
        this(Menu.class, metadata, inits);
    }

    public QMenu(Class<? extends Menu> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new kr.debop4j.access.model.organization.QCompany(forProperty("company")) : null;
        this.nodePosition = inits.isInitialized("nodePosition") ? new kr.debop4j.data.model.QTreeNodePosition(forProperty("nodePosition")) : null;
        this.parent = inits.isInitialized("parent") ? new QMenu(forProperty("parent"), inits.get("parent")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

