package kr.debop4j.data.hibernate.forTesting;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.UUID;

/**
 * kr.debop4j.data.hibernate.forTesting.UnitOfWorkTestContextBase
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public abstract class UnitOfWorkTestContextBase implements AutoCloseable {

    public final String CurrentHibernateSessionKey = UUID.randomUUID().toString();

    protected GenericApplicationContext springContext;
    @Getter
    private Class dbConfigurationClass;
    @Getter
    private int unitOfWorkNestingLevel = -1;

    protected UnitOfWorkTestContextBase(Class dbConfigurationClass) {
        this.dbConfigurationClass = dbConfigurationClass;

        springContext = new AnnotationConfigApplicationContext(dbConfigurationClass);
        Springs.init(springContext);
    }

    public GenericApplicationContext getSpringContext() {
        synchronized (this) {
            if (springContext == null) {
                springContext = new AnnotationConfigApplicationContext(dbConfigurationClass);
            }
        }
        return springContext;
    }

    public void createUnitOfWork() {
        Guard.shouldBe(unitOfWorkNestingLevel != 0, "중첩된 UnitOfWork를 만들려면 createNestedUnitOfWork() 메소드를 사용하세요.");

        if (log.isInfoEnabled())
            log.info("현재 Session을 사용하여, UnitOfWork를 시작하고, Database를 설정합니다.");

        try {
            if (UnitOfWorks.isStarted()) {
                UnitOfWorks.stop();
                UnitOfWorks.closeUnitOfWorkFactory();
            }

            if (Springs.isNotInitialized())
                Springs.init(getSpringContext());

            UnitOfWorks.start();

            setupDatabase(UnitOfWorks.getCurrentSession());
        } finally {
            unitOfWorkNestingLevel = 0;
        }
    }

    public IUnitOfWork createNestedUnitOfWork() {
        Guard.shouldBe(this.unitOfWorkNestingLevel != -1, "부모 UnitOfWork가 존재하지 않습니다. 먼저 createUnitOfWork() 를 호출하세요");

        try {
            IUnitOfWork uow = UnitOfWorks.start(UnitOfWorks.getCurrentSessionFactory(),
                                                UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork);
            return uow;
        } finally {
            unitOfWorkNestingLevel++;
        }
    }

    public SessionFactory getSessionFactory() {
        return Springs.getBean(SessionFactory.class);
    }

    public Session createSession() {
        Session session = getSessionFactory().openSession();
        return session;
    }

    public void disposeSession(Session sessionToClose) {
        if (sessionToClose == null)
            return;

        if (log.isDebugEnabled())
            log.debug("hibernate session을 close 합니다...");

        sessionToClose.close();

        if (log.isDebugEnabled())
            log.debug("hibernate session을 close 했습니다.");
    }

    public void disposeUnitOfWork() {
        try {
            UnitOfWorks.stop();
        } finally {
            unitOfWorkNestingLevel--;
        }
    }

    abstract public void initializeContainerAndUnitOfWorkFactory();

    public void setupDatabase(Session session) {
        // Nothing to do.
    }

    public String toString() {
        return Objects.toStringHelper(this)
                .toString();
    }

    @Override
    public void close() throws Exception {
        this.dbConfigurationClass = null;

        UnitOfWorks.stop();

        if (springContext != null) {
            springContext.close();
            springContext = null;
        }
    }


    // region << static methods >>

    public static UnitOfWorkTestContextBase create(Class dbConfigurationClass) {
        return new UnitOfWorkTestContext(dbConfigurationClass);
    }

    // endregion
}
