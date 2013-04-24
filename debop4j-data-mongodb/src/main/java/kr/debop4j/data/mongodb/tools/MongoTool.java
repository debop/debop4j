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

package kr.debop4j.data.mongodb.tools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.datastore.spi.Tuple;
import org.hibernate.ogm.datastore.spi.TupleContext;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.dialect.mongodb.MongoDBDialect;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.EntityKeyMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MongoDB 를 hibernate-ogm의 저장소로 사용할 때의 저장소
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
@Component
@Slf4j
@Getter
@Setter
public class MongoTool {

    @Autowired
    GridDialect gridDialect;
    @Autowired
    DatastoreProvider datastoreProvider;

    public MongoTool() {}

    @Autowired
    public MongoTool(GridDialect gridDialect, DatastoreProvider datastoreProvider) {
        this.gridDialect = gridDialect;
        this.datastoreProvider = datastoreProvider;
    }

    public Tuple getTuple(String collectionName, String id, List<String> selectedColumns) {
        EntityKey key = new EntityKey(new EntityKeyMetadata(collectionName,
                                                            new String[]{ MongoDBDialect.ID_FIELDNAME }),
                                      new Object[]{ id });
        TupleContext tupleContext = new TupleContext(selectedColumns);
        return gridDialect.getTuple(key, tupleContext);
    }

    public MongoDBDatastoreProvider getProvider() {
        return (MongoDBDatastoreProvider) datastoreProvider;
    }
}
