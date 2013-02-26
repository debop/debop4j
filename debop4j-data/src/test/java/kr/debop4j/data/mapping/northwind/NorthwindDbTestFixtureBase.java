package kr.debop4j.data.mapping.northwind;

import com.google.common.collect.Iterables;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import kr.debop4j.data.hibernate.repository.HibernateRepositoryFactory;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mapping.northwind.config.PostgreSqlConfig;
import kr.debop4j.data.mapping.northwind.model.Customer;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.StringType;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Northwind Database 를 사용하는 테스트 코드
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 23.
 */
@Slf4j
public class NorthwindDbTestFixtureBase extends DatabaseTestFixtureBase {

    @BeforeClass
    public static void beforeClass() {
        initHibernateAndSpring(PostgreSqlConfig.class);

        if (!UnitOfWorks.isStarted())
            UnitOfWorks.start();
    }

    @AfterClass
    public static void afterClass() {
        UnitOfWorks.stop();
        Springs.reset();
    }

    public static final Customer testCustomer = getCustomer();

    private static Customer getCustomer() {
        Customer customer = new Customer("CACTU");
        customer.setCompanyName("Cactus Comidas para llevar");
        customer.setContactName("Patricio Simpson");

        return customer;
    }

    public static final HibernateParameter CustomerParameter =
            new HibernateParameter("customerId", "ANATR", StringType.INSTANCE);

    public static <E extends IStatefulEntity> IHibernateRepository<E> getDao(Class<E> entityCalss) {
        return Springs.getBean(HibernateRepositoryFactory.class).getOrCreateHibernateRepository(entityCalss);
    }

    public static <E> void print(Iterable<E> collection) {
        print(collection, 5);
    }

    public static <E> void print(Iterable<E> collection, Integer maxCount) {
        if (log.isDebugEnabled()) {
            log.debug("최대 [{}]개만 print 합니다...", maxCount);
            log.debug("-----------------------------------");
            for (Object x : Iterables.limit(collection, maxCount))
                log.debug(x.toString());
            log.debug("-----------------------------------");
        }
    }
}
