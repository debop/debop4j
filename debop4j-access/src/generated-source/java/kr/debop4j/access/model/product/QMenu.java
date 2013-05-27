/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.access.model.product;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QMenu is a Querydsl query type for Menu */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = 821609776;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenu menu = new QMenu("menu");

    public final kr.debop4j.data.model.QAnnotatedTreeEntityBase _super;

    public final BooleanPath active = createBoolean("active");

    public final SetPath<Menu, QMenu> children = this.<Menu, QMenu>createSet("children", Menu.class, QMenu.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final kr.debop4j.access.model.organization.QCompany company;

    public final StringPath description = createString("description");

    public final StringPath exAttr = createString("exAttr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    // inherited
    public final kr.debop4j.data.model.QTreeNodePosition nodePosition;

    public final QMenu parent;

    //inherited
    public final BooleanPath persisted;

    public final QProduct product;

    public final StringPath title = createString("title");

    public final DateTimePath<java.util.Date> updateTimestamp = createDateTime("updateTimestamp", java.util.Date.class);

    public final StringPath url = createString("url");

    public QMenu(String variable) {
        this(Menu.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
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
        this._super = new kr.debop4j.data.model.QAnnotatedTreeEntityBase(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new kr.debop4j.access.model.organization.QCompany(forProperty("company")) : null;
        this.nodePosition = _super.nodePosition;
        this.parent = inits.isInitialized("parent") ? new QMenu(forProperty("parent"), inits.get("parent")) : null;
        this.persisted = _super.persisted;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}
