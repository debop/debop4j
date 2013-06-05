package kr.debop4j.access.model.organization;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QCompany_CompanyLocale is a Querydsl query type for CompanyLocale
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QCompany_CompanyLocale extends BeanPath<Company.CompanyLocale> {

    private static final long serialVersionUID = -1273916579;

    public static final QCompany_CompanyLocale companyLocale = new QCompany_CompanyLocale("companyLocale");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public QCompany_CompanyLocale(String variable) {
        super(Company.CompanyLocale.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QCompany_CompanyLocale(Path<? extends Company.CompanyLocale> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QCompany_CompanyLocale(PathMetadata<?> metadata) {
        super(Company.CompanyLocale.class, metadata);
    }

}

