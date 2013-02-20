package kr.debop4j.data.hibernate.forTesting;

import com.google.common.collect.Maps;
import kr.debop4j.data.hibernate.forTesting.datasource.HSqlUnitOfWorkTestContextDbStrategy;
import kr.debop4j.data.hibernate.forTesting.datasource.PostgreSqlUnitOfWorktestContextDbStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.util.Map;

/**
 * 각 DB에 맞게 테스트용 Database 룰 준비합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public abstract class UnitOfWorkTestContextDbStrategy {

    public static final String HSqlDbName = ":mem:";
    public static final String SQLiteDbName = ":memory:";

    public static UnitOfWorkTestContextDbStrategy forDb(DatabaseEngine databaseEngine,
                                                        String databaseName,
                                                        Map<String, String> properties) {
        UnitOfWorkTestContextDbStrategy strategy;

        if (databaseEngine == DatabaseEngine.HSql)
            strategy = new HSqlUnitOfWorkTestContextDbStrategy();
        else if (databaseEngine == DatabaseEngine.PostgreSQL)
            strategy = new PostgreSqlUnitOfWorktestContextDbStrategy();
        else
            strategy = new HSqlUnitOfWorkTestContextDbStrategy();

        if (properties != null) {
            for (String key : properties.keySet()) {
                strategy.setHibernateProperties(key, properties.get(key));
            }
        }
        return strategy;
    }

    public static UnitOfWorkTestContextDbStrategy forDb(DatabaseEngine databaseEngine, String databaseName) {
        return forDb(databaseEngine, databaseName);
    }

    @Getter
    private final String databaseName;
    @Getter
    private final Map<String, String> properties = Maps.newHashMap();

    @Getter
    @Setter
    private UnitOfWorkTestContext testContext;

    protected UnitOfWorkTestContextDbStrategy(String databaseName) {
        this.databaseName = databaseName;

        properties.put(Environment.FORMAT_SQL, "true");
        properties.put(Environment.HBM2DDL_AUTO, "create"); // create | spawn | spawn-drop | update | validate
    }

    public abstract DatabaseEngine getDatabaseEngine();


    public Session createSession() {
        return getTestContext().getSessionFactory().openSession();
    }

    public void setupDatabase(Session currentSession) {
        Session session = (currentSession != null) ? currentSession : createSession();
        createDatabaseSchema(session);
    }

    protected void createDatabaseMedia() {
        // 필요 시 Database 를 실제로 생성합니다. ==> java에서는 그렇게 할 필요가 없다.
    }

    protected void createDatabaseSchema(Session session) {
        new SchemaExport(getTestContext().getConfiguration()).execute(false, true, false, true);
    }
}
