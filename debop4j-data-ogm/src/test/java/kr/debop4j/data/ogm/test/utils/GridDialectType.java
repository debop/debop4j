package kr.debop4j.data.ogm.test.utils;

/**
 * GridDialectType
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 4:39
 */
public enum GridDialectType {
    HASHMAP("org.hibernate.ogm.HashMapTestHelper") {
        @Override
        public Class<?> loadTestableGridDialectClass() {
            return null; //this one is special, we want it only as fallback when all others fail
        }
    },

    INFINISPAN("org.hibernate.ogm.test.utils.InfinispanTestHelper"),

    EHCACHE("org.hibernate.ogm.test.utils.EhcacheTestHelper"),

    MONGODB("org.hibernate.ogm.test.utils.MongoDBTestHelper");

    private final String testHelperClassName;

    GridDialectType(String testHelperClassName) {
        this.testHelperClassName = testHelperClassName;
    }

    public Class<?> loadTestableGridDialectClass() {
        Class<?> classForName = null;
        try {
            classForName = Class.forName(testHelperClassName);
        } catch (ClassNotFoundException e) {
            //ignore this: might not be available
        }
        return classForName;
    }

    public static GridDialectType valueFromHelperClass(Class<? extends TestableGridDialect> class1) {
        for (GridDialectType type : values()) {
            if (type.testHelperClassName.equals(class1.getName())) {
                return type;
            }
        }
        throw new IllegalArgumentException(class1 +
                                                   " is not one of the TestableGridDialect implementation known to " + GridDialectType.class);
    }
}
