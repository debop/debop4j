package kr.debop4j.data.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDateTimeRange is a Querydsl query type for DateTimeRange
 */
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
        super((Class)path.getType(), path.getMetadata());
    }

    public QDateTimeRange(PathMetadata<?> metadata) {
        super(DateTimeRange.class, metadata);
    }

}

