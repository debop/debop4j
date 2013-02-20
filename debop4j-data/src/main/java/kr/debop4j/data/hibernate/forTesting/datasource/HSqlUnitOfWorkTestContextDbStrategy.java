package kr.debop4j.data.hibernate.forTesting.datasource;

import kr.debop4j.data.hibernate.forTesting.DatabaseEngine;
import kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextDbStrategy;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.sql.Connection;

/**
 * kr.debop4j.data.hibernate.forTesting.datasource.HSqlUnitOfWorkTestContextDbStrategy
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 20.
 */
@Slf4j
public class HSqlUnitOfWorkTestContextDbStrategy extends UnitOfWorkTestContextDbStrategy {


    public HSqlUnitOfWorkTestContextDbStrategy() {
        super(HSqlDbName);

        getProperties().put(Environment.DRIVER, "org.hsqldb.jdbcDriver");
        getProperties().put(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");
        getProperties().put(Environment.URL, "jdbc:hsqldb" + getDatabaseName() + "testdb");
        getProperties().put(Environment.USER, "sa");
        getProperties().put(Environment.PASS, "");

        getProperties().put(Environment.SHOW_SQL, "true");
        getProperties().put(Environment.RELEASE_CONNECTIONS, "on_close");
        getProperties().put(Environment.STATEMENT_BATCH_SIZE, "30");
    }

    @Override
    public DatabaseEngine getDatabaseEngine() {
        return DatabaseEngine.HSql;
    }

    @Override
    protected void createDatabaseSchema(Session session) {
        new SchemaExport(getTestContext().getConfiguration()).execute(false, true, false, true);
    }

    @Override
    public Session createSession() {

        Connection conn = ((SessionFactoryImpl) getTestContext().getSessionFactory()).getConnectionProvider().getConnection();
        return getTestContext().getSessionFactory().openSession(conn);
    }
}
