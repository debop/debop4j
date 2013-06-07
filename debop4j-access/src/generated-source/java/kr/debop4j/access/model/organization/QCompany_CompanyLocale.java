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

package kr.debop4j.access.model.organization;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/** QCompany_CompanyLocale is a Querydsl query type for CompanyLocale */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QCompany_CompanyLocale extends BeanPath<Company.CompanyLocale> {

    private static final long serialVersionUID = -1273916579;

    public static final QCompany_CompanyLocale companyLocale = new QCompany_CompanyLocale("companyLocale");

    public final kr.debop4j.core.QValueObjectBase _super = new kr.debop4j.core.QValueObjectBase(this);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public QCompany_CompanyLocale(String variable) {
        super(Company.CompanyLocale.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QCompany_CompanyLocale(Path<? extends Company.CompanyLocale> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    public QCompany_CompanyLocale(PathMetadata<?> metadata) {
        super(Company.CompanyLocale.class, metadata);
    }

}

