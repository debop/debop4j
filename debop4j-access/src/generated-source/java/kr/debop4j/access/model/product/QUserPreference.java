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


/** QUserPreference is a Querydsl query type for UserPreference */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUserPreference extends EntityPathBase<UserPreference> {

    private static final long serialVersionUID = 2036490647;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPreference userPreference = new QUserPreference("userPreference");

    public final kr.debop4j.access.model.QPreferenceBase _super = new kr.debop4j.access.model.QPreferenceBase(this);

    //inherited
    public final StringPath defaultValue = _super.defaultValue;

    //inherited
    public final StringPath description = _super.description;

    //inherited
    public final StringPath exAttr = _super.exAttr;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath key = _super.key;

    //inherited
    public final BooleanPath persisted = _super.persisted;

    public final QUser user;

    //inherited
    public final StringPath value = _super.value;

    public QUserPreference(String variable) {
        this(UserPreference.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QUserPreference(Path<? extends UserPreference> path) {
        this((Class) path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserPreference(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUserPreference(PathMetadata<?> metadata, PathInits inits) {
        this(UserPreference.class, metadata, inits);
    }

    public QUserPreference(Class<? extends UserPreference> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

