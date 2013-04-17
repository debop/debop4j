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

import java.util.List;

/**
 * MongoDB 를 hibernate-ogm의 저장소로 사용할 때의 저장소
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 3. 28
 */
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
