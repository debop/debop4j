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
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QTreeNodePosition is a Querydsl query type for TreeNodePosition */
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
        super((Class) path.getType(), path.getMetadata());
    }

    public QTreeNodePosition(PathMetadata<?> metadata) {
        super(TreeNodePosition.class, metadata);
    }

}

