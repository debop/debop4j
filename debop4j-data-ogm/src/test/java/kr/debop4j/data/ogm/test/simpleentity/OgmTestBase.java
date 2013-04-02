package kr.debop4j.data.ogm.test.simpleentity;

import kr.debop4j.data.ogm.test.utils.GridDialectType;
import kr.debop4j.data.ogm.test.utils.SkipByGridDialect;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.engine.spi.SearchFactoryImplementor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * OgmTestBase
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 29. 오후 4:22
 */
@Slf4j
public abstract class OgmTestBase {

//    static {
//        TestHelper.initializeHelpers();
//    }

    @BeforeClass
    public static void beforeClass() {
        TestHelper.initializeHelpers();
    }

    @Before
    public void before() throws Exception {
        doBefore();
    }

    @After
    public void after() throws Exception {
        doAfter();
    }

    public void doBefore() throws Exception {
        if (cfg == null || lastTestClass != getClass()) {
            buildConfiguration();
            lastTestClass = getClass();
        }
    }

    public void doAfter() throws Exception {
        handleUnclosedResources();
        closeResources();
    }

    protected SessionFactory sessions;
    private Session session;

    protected static Configuration cfg;
    private static Class<?> lastTestClass;

    protected String[] getXmlFiles() {
        return new String[]{ };
    }

    protected static void setCfg(Configuration cfg) {
        OgmTestBase.cfg = cfg;
    }

    protected static Configuration getCfg() {
        return cfg;
    }

    protected void configure(Configuration cfg) {
    }


    protected abstract Class<?>[] getAnnotatedClasses();

    protected boolean recreateSchema() {
        return true;
    }

    protected String[] getAnnotatedPackages() {
        return new String[]{ };
    }

    protected SearchFactoryImplementor getSearchFactoryImpl() {
        if (log.isDebugEnabled())
            log.debug("SearchFactoryImplementor 를 생성합니다.");

        FullTextSession fts = Search.getFullTextSession(openSession());
        fts.close();
        SearchFactory searchFactory = fts.getSearchFactory();
        return (SearchFactoryImplementor) searchFactory;
    }

    private void reportSkip(Skip skip) {
        reportSkip(skip.reason, skip.testDescription);
    }

    protected void reportSkip(String reason, String testDescription) {
        StringBuilder builder = new StringBuilder();
        builder.append("*** skipping test [");
        builder.append(fullTestName());
        builder.append("] - ");
        builder.append(testDescription);
        builder.append(" : ");
        builder.append(reason);
        OgmTestBase.log.warn(builder.toString());
    }

    protected Skip buildSkip(GridDialectType dialect, String comment) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("skipping database-specific test [");
        buffer.append(fullTestName());
        buffer.append("] for dialect [");
        buffer.append(dialect.name());
        buffer.append(']');

        if (StringHelper.isNotEmpty(comment)) {
            buffer.append("; ").append(comment);
        }

        return new Skip(buffer.toString(), null);
    }

    protected <T extends Annotation> T locateAnnotation(Class<T> annotationClass, Method runMethod) {
        T annotation = runMethod.getAnnotation(annotationClass);
        if (annotation == null) {
            annotation = getClass().getAnnotation(annotationClass);
        }
        if (annotation == null) {
            annotation = runMethod.getDeclaringClass().getAnnotation(annotationClass);
        }
        return annotation;
    }

    protected final Skip determineSkipByGridDialect(Method runMethod) throws Exception {
        SkipByGridDialect skipForDialectAnn = locateAnnotation(SkipByGridDialect.class, runMethod);
        if (skipForDialectAnn != null) {
            for (GridDialectType gridDialectType : skipForDialectAnn.value()) {
                if (gridDialectType.equals(TestHelper.getCurrentDialectType())) {
                    return buildSkip(gridDialectType, skipForDialectAnn.comment());
                }
            }
        }
        return null;
    }

    protected static class Skip {
        private final String reason;
        private final String testDescription;

        public Skip(String reason, String testDescription) {
            this.reason = reason;
            this.testDescription = testDescription;
        }
    }

//    @Override
//    protected void runTest() throws Throwable {
//        Method runMethod = findTestMethod();
//        FailureExpected failureExpected = locateAnnotation(FailureExpected.class, runMethod);
//        try {
//            super.runTest();
//            if (failureExpected != null) {
//                throw new FailureExpectedTestPassedException();
//            }
//        } catch (FailureExpectedTestPassedException t) {
//            closeResources();
//            throw t;
//        } catch (Throwable t) {
//            if (t instanceof InvocationTargetException) {
//                t = ((InvocationTargetException) t).getTargetException();
//            }
//            if (t instanceof IllegalAccessException) {
//                t.fillInStackTrace();
//            }
//            closeResources();
//            if (failureExpected != null) {
//                StringBuilder builder = new StringBuilder();
//                if (StringHelper.isNotEmpty(failureExpected.message())) {
//                    builder.append(failureExpected.message());
//                } else {
//                    builder.append("ignoring @FailureExpected test");
//                }
//                builder.append(" (")
//                       .append(failureExpected.jiraKey())
//                       .append(")");
//                OgmTestBase.log.warn(builder.toString(), t);
//            } else {
//                throw t;
//            }
//        }
//    }
//
//    @Override
//    public void runBare() throws Throwable {
//        Method runMethod = findTestMethod();
//
//        final Skip skip = determineSkipByGridDialect(runMethod);
//        if (skip != null) {
//            reportSkip(skip);
//            return;
//        }
//
//        setUp();
//        try {
//            runTest();
//        } finally {
//            tearDown();
//        }
//    }

    public String fullTestName() {
        return this.getClass().getName(); // + "#" + this.getName();
    }
//
//    private Method findTestMethod() {
//        String fName = getName();
//        assertNotNull(fName);
//        Method runMethod = null;
//        try {
//            runMethod = getClass().getMethod(fName);
//        } catch (NoSuchMethodException e) {
//            fail("Method \"" + fName + "\" not found");
//        }
//        if (!Modifier.isPublic(runMethod.getModifiers())) {
//            fail("Method \"" + fName + "\" should be public");
//        }
//        return runMethod;
//    }

    private static class FailureExpectedTestPassedException extends Exception {
        public FailureExpectedTestPassedException() {
            super("Test marked as @FailureExpected, but did not fail!");
        }
    }


    public Session openSession() throws HibernateException {
        rebuildSessionFactory();
        session = getSessions().openSession();
        return session;
    }

    private void rebuildSessionFactory() {
        if (sessions == null) {
            try {
                buildConfiguration();
            } catch (Exception e) {
                throw new HibernateException(e);
            }
        }
    }

    protected void setSessions(SessionFactory sessions) {
        this.sessions = sessions;
    }

    protected SessionFactory getSessions() {
        return sessions;
    }

    protected SessionFactoryImplementor sfi() {
        return (SessionFactoryImplementor) getSessions();
    }

    //FIXME clear cache when this happens
    protected void runSchemaGeneration() {

    }

    protected void runSchemaDrop() {
        TestHelper.dropSchemaAndDatabase(session);
    }

    protected void buildConfiguration() throws Exception {
        closeSessionFactory();
        try {
            setCfg(new OgmConfiguration());

            //Other configurations
            // by default use the new id generator scheme...
            getCfg().setProperty(Configuration.USE_NEW_ID_GENERATOR_MAPPINGS, "true");

            for (Map.Entry<String, String> entry : TestHelper.getEnvironmentProperties().entrySet()) {
                getCfg().setProperty(entry.getKey(), entry.getValue());
            }

            configure(cfg);

            if (recreateSchema()) {
                getCfg().setProperty(Environment.HBM2DDL_AUTO, "none");
            }
            for (String aPackage : getAnnotatedPackages()) {
                getCfg().addPackage(aPackage);
            }
            for (Class<?> aClass : getAnnotatedClasses()) {
                getCfg().addAnnotatedClass(aClass);
            }
            for (String xmlFile : getXmlFiles()) {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFile);
                getCfg().addInputStream(is);
            }

            // TODO: hibernate-search-orm 4.3.0 SNAPSHOT 에 있습니다.
//            getCfg().setProperty(MassIndexerFactoryIntegrator.MASS_INDEXER_FACTORY_CLASSNAME,
//                                 OgmMassIndexerFactory.class.getName());

            setSessions(getCfg().buildSessionFactory( /* new TestInterceptor() */));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void handleUnclosedResources() {
        if (session != null && session.isOpen()) {
            if (session.isConnected()) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
            session.close();
            session = null;
            // fail("unclosed session");
            // Assert.fail("unclosed session");
        } else {
            session = null;
        }
        closeSessionFactory();
    }

    private void closeSessionFactory() {
        if (sessions != null) {
            if (!sessions.isClosed()) {
                TestHelper.dropSchemaAndDatabase(sessions);
                sessions.close();
                sessions = null;
            } else {
                sessions = null;
            }
        }
    }

    protected void closeResources() {
        try {
            if (session != null && session.isOpen()) {
                if (session.isConnected()) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                }
                session.close();
            }
        } catch (Exception ignore) {
        }
        try {
            closeSessionFactory();
        } catch (Exception ignore) {
        }
    }

    public void checkCleanCache() {
        assertThat(TestHelper.assertNumberOfEntities(0, sessions))
                .as("Entity cache should be empty")
                .isTrue();

        assertThat(TestHelper.assertNumberOfAssociations(0, sessions))
                .as("Association cache should be empty")
                .isTrue();
    }
}
