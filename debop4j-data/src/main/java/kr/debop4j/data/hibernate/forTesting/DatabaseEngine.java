package kr.debop4j.data.hibernate.forTesting;

/**
 * kr.debop4j.data.hibernate.forTesting.DatabaseEngine
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
public enum DatabaseEngine {

    HSql("HSql"),
    HSqlWithFile("HSqlWithFile"),
    SQLite("SQLite"),
    SQLiteWithFile("SQLitewithFile"),
    MySQL("MySQL"),
    PostgreSQL("PostgresQL"),
    SQLServer("SQLServer"),
    Oracle("Oracle");

    private final String databaseName;

    DatabaseEngine(String databaseName) {
        this.databaseName = databaseName;
    }
}
