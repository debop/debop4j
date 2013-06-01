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

package kr.debop4j.data.ogm.test.datastore;

import org.hibernate.LockMode;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.ogm.datastore.StartStoppable;
import org.hibernate.ogm.datastore.spi.*;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.grid.AssociationKey;
import org.hibernate.ogm.grid.EntityKey;
import org.hibernate.ogm.grid.RowKey;
import org.hibernate.ogm.type.GridType;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.type.Type;

import java.util.Iterator;

/**
 * kr.debop4j.data.ogm.test.datastore.DatastoreProviderGeneratingSchema
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class DatastoreProviderGeneratingSchema implements DatastoreProvider, StartStoppable {

    @Override
    public Class<? extends GridDialect> getDefaultDialect() {
        return Dialect.class;
    }

    @Override
    public void start(Configuration configuration, SessionFactoryImplementor sessionFactoryImplementor) {
        Iterator<Table> tables = configuration.getTableMappings();
        while (tables.hasNext()) {
            Table table = tables.next();
            if (table.isPhysicalTable()) {
                String tableName = table.getQuotedName();
                // do something with table
                Iterator<Column> columns = (Iterator<Column>) table.getColumnIterator();
                while (columns.hasNext()) {
                    Column column = columns.next();
                    String columnName = column.getCanonicalName();
                    // do something with column
                }
                //TODO handle unique constraints?
            }
        }
        throw new RuntimeException("STARTED!");
    }

    @Override
    public void stop() {
        // not tested
        throw new RuntimeException("STOPPED!");
    }

    public static class Dialect implements GridDialect {

        public Dialect(DatastoreProviderGeneratingSchema provider) {

        }

        @Override
        public LockingStrategy getLockingStrategy(Lockable lockable, LockMode lockMode) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Tuple getTuple(EntityKey key, TupleContext tupleContext) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Tuple createTuple(EntityKey key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void updateTuple(Tuple tuple, EntityKey key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void removeTuple(EntityKey key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Association getAssociation(AssociationKey key, AssociationContext associationContext) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Association createAssociation(AssociationKey key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void updateAssociation(Association association, AssociationKey key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void removeAssociation(AssociationKey key) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Tuple createTupleAssociation(AssociationKey associationKey, RowKey rowKey) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void nextValue(RowKey key, IntegralDataTypeHolder value, int increment, int initialValue) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public GridType overrideType(Type type) {
            // No type to override
            return null;
        }

//        @Override
//        public void forEachTuple(Consumer consumer, EntityKeyMetadata... entityKeyMetadatas) {
//        }
    }
}
