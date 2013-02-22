package kr.debop4j.data.hibernate.forTesting;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 여러 Database에 대해, Hibernate 작업 Test를 위한 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public abstract class DatabaseTestFixtureBase {

    @Getter
    private static final List<UnitOfWorkTestContextBase> contexts = Lists.newArrayList();

    @Getter
    private static UnitOfWorkTestContextBase currentContext;

    private static final String TestModeKey = DatabaseTestFixtureBase.class.getName() + ".isRunningInTestMode";

    public static boolean isRunningInTestMode() {
        Boolean isTestMode = (Boolean) Local.get(TestModeKey);
        return (isTestMode != null) ? isTestMode : false;
    }

    public static void setRunningInTestMode(boolean testMode) {
        Local.put(TestModeKey, testMode);
    }

    public static void initHibernateAndSpring(Class dbConfigurationClass) {

        UnitOfWorkTestContextBase context = getUnitOfWorkTestContext(dbConfigurationClass);
        setRunningInTestMode(true);

        if (currentContext != context) {
            context.initializeContainerAndUnitOfWorkFactory();
        }

        currentContext = context;
    }

    protected static UnitOfWorkTestContextBase getUnitOfWorkTestContext(final Class dbConfigurationClass) {

        UnitOfWorkTestContextBase context = null;
        if (contexts.size() > 0) {
            Predicate<UnitOfWorkTestContextBase> criteria =
                    new Predicate<UnitOfWorkTestContextBase>() {
                        @Override
                        public boolean apply(@Nullable UnitOfWorkTestContextBase input) {
                            return (input != null) && dbConfigurationClass == input.getDbConfigurationClass();
                        }
                    };
            try {
                context = Iterables.find(contexts, criteria);
            } catch (NoSuchElementException ignored) {
                context = null;
            }
        }

        if (context == null) {
            context = UnitOfWorkTestContextBase.create(dbConfigurationClass);
            contexts.add(context);

            if (log.isDebugEnabled())
                log.debug("create UnitOfWorkTestContext by [{}]", dbConfigurationClass);
        }

        return context;
    }

    public static void closeUnitOfWorkTestContexts() {
        if (log.isDebugEnabled())
            log.debug("모든 테스트용 UnitOfWorkTestContext 를 종료합니다...");

        for (UnitOfWorkTestContextBase context : contexts) {
            try {
                if (context != null)
                    context.close();

            } catch (Exception ignored) {
                log.warn("Context 종료 시 예외가 발생했습니다. 무시합니다.", ignored);
            }
        }
        try {
            currentContext.close();
            currentContext = null;
        } catch (Exception ignored) { }

        setRunningInTestMode(false);
        contexts.clear();

        UnitOfWorks.closeUnitOfWorkFactory();

        if (log.isDebugEnabled())
            log.debug("모든 UnitOfWorkTestContext를 종료했습니다.");
    }
}
