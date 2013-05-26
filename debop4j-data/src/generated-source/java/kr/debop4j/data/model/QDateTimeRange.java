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
import com.mysema.query.types.path.DateTimePath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QDateTimeRange is a Querydsl query type for DateTimeRange */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QDateTimeRange extends BeanPath<DateTimeRange> {

    private static final long serialVersionUID = 1044752630;

    public static final QDateTimeRange dateTimeRange = new QDateTimeRange("dateTimeRange");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final DateTimePath<org.joda.time.DateTime> end = createDateTime("end", org.joda.time.DateTime.class);

    public final DateTimePath<org.joda.time.DateTime> start = createDateTime("start", org.joda.time.DateTime.class);

    public QDateTimeRange(String variable) {
        super(DateTimeRange.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QDateTimeRange(Path<? extends DateTimeRange> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QDateTimeRange(PathMetadata<?> metadata) {
        super(DateTimeRange.class, metadata);
    }

}

