package kr.debop4j.data.mapping.annotated;

import com.google.common.base.Objects;
import kr.debop4j.data.model.EntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * org.annotated.mapping.CustomerRepositoryTest
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/repository-config.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class CustomerRepositoryTest {

    @Configuration
    static class ContextConfiguration {

        @Bean
        public CustomerRepository customerRepository() {
            return new CustomerRepositoryImpl();
        }
    }

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CustomerRepository customerRepository;

    //@Before
    @Transactional
    public void onSetupInTransaction() {
        Customer c = new Customer();
        c.setName("Test");
        c.setCustomerSince(new Date());
        em.persist(c);
    }
}

interface CustomerRepository {

    List<Customer> findAll();
}

class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<Customer> findAll() {
        return (List<Customer>) sessionFactory.getCurrentSession().createQuery("from Join_Customer").list();
    }
}

@Getter
@Setter
@ToString
class Customer extends EntityBase<Long> {

    private static final long serialVersionUID = -2287305093400486879L;

    private String name;
    private Date customerSince;

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();

        return Objects.hashCode(name, customerSince);
    }
}
