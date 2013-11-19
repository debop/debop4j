package kr.debop4j.data.mapping.northwind;

import com.google.common.collect.Iterables;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.HibernateParameter;
import kr.debop4j.data.hibernate.forTesting.DatabaseTestFixtureBase;
import kr.debop4j.data.hibernate.repository.IHibernateRepository;
import kr.debop4j.data.hibernate.repository.IHibernateRepositoryFactory;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mapping.northwind.config.PostgreSqlConfig;
import kr.debop4j.data.mapping.northwind.model.Customer;
import kr.debop4j.data.model.IStatefulEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.StringType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Northwind Database 를 사용하는 테스트 코드
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 23.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PostgreSqlConfig.class })
public class NorthwindDbTestFixtureBase extends DatabaseTestFixtureBase {

    @Autowired
    ApplicationContext applicationContext;

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

    public final Customer testCustomer = getCustomer();

    private Customer getCustomer() {
        Customer customer = new Customer("CACTU");
        customer.setCompanyName("Cactus Comidas para llevar");
        customer.setContactName("Patricio Simpson");

        return customer;
    }

    public final HibernateParameter CustomerParameter = new HibernateParameter("customerId", "ANATR", StringType.INSTANCE);

    public <E extends IStatefulEntity> IHibernateRepository<E> getDao(Class<E> entityCalss) {
        return applicationContext.getBean(IHibernateRepositoryFactory.class).getOrCreateHibernateRepository(entityCalss);
        // return Springs.getBean(IHibernateRepositoryFactory.class).getOrCreateHibernateRepository(entityCalss);
    }

    public void print(Iterable<?> collection) {
        print(collection, 5);
    }

    public void print(Iterable<?> collection, Integer maxCount) {
        if (log.isDebugEnabled()) {
            log.debug("최대 [{}]개만 print 합니다...", maxCount);
            log.debug("-----------------------------------");
            for (Object x : Iterables.limit(collection, maxCount))
                log.debug(x.toString());
            log.debug("-----------------------------------");
        }
    }
}
