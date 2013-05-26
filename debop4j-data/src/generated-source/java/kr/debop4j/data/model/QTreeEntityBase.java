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

package kr.debop4j.data.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QTreeEntityBase is a Querydsl query type for TreeEntityBase */
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
        this((Class) TreeEntityBase.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QTreeEntityBase(Path<? extends TreeEntityBase> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTreeEntityBase(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    @SuppressWarnings("all")
    public QTreeEntityBase(PathMetadata<?> metadata, PathInits inits) {
        this((Class) TreeEntityBase.class, metadata, inits);
    }

    public QTreeEntityBase(Class<? extends TreeEntityBase<? extends ITreeEntity<?>, ? extends java.io.Serializable>> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nodePosition = inits.isInitialized("nodePosition") ? new QTreeNodePosition(forProperty("nodePosition")) : null;
    }

}

