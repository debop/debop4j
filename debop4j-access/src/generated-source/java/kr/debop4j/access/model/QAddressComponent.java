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

package kr.debop4j.access.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QAddressComponent is a Querydsl query type for AddressComponent */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QAddressComponent extends BeanPath<AddressComponent> {

    private static final long serialVersionUID = 1366610075;

    public static final QAddressComponent addressComponent = new QAddressComponent("addressComponent");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    public final StringPath state = createString("state");

    public final StringPath street1 = createString("street1");

    public final StringPath street2 = createString("street2");

    public final StringPath zipcode = createString("zipcode");

    public QAddressComponent(String variable) {
        super(AddressComponent.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QAddressComponent(Path<? extends AddressComponent> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QAddressComponent(PathMetadata<?> metadata) {
        super(AddressComponent.class, metadata);
    }

}

