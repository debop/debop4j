package kr.debop4j.data.ogm.test.associations.collection.manytomany;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import kr.debop4j.data.ogm.test.utils.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * ManyToManyTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 29. 오후 9:10
 */
@Slf4j
public class ManyToManyTest extends OgmTestBase {

    @Test
    public void mappingTest() {
        Session session = openSession();
        Assert.assertNotNull(session);
        session.close();
    }

    @Test
    @Ignore("hibernate-core 4.2.2.Final 이상에서는 예외가 발생합니다.")
    public void manyToManyTest() {
        Session session = openSession();
        Transaction tx = session.beginTransaction();

        AccountOwner owner = new AccountOwner();
        owner.setSSN("0123456");
        BankAccount soge = new BankAccount();
        soge.setAccountNumber("X2345000");
        owner.getBankAccounts().add(soge);
        soge.getOwners().add(owner);

        // mappedBy 가 AccountOwner로 설정되었다는 것은 AccountOwner 를 기준으로 cascading 이 된다는 뜻이다.
        session.persist(owner);
        tx.commit();

        assertThat(TestHelper.assertNumberOfEntities(2, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(2, sessions)).isTrue();

        session.clear();

        // read from inverse side
        tx = session.beginTransaction();
        soge = TestHelper.get(session, BankAccount.class, soge.getId());
        Assert.assertEquals(1, soge.getOwners().size());
        assertThat(soge.getOwners()).hasSize(1);
        assertThat(soge.getOwners()).onProperty("id").contains(owner.getId());
        tx.commit();

        session.clear();

        // read from non-inverse side and update data
        tx = session.beginTransaction();
        owner = TestHelper.get(session, AccountOwner.class, owner.getId());
        assertThat(owner.getBankAccounts()).hasSize(1);
        assertThat(owner.getBankAccounts()).onProperty("id").contains(soge.getId());

        soge = owner.getBankAccounts().iterator().next();
        soge.getOwners().remove(owner);
        owner.getBankAccounts().remove(soge);

        BankAccount barclays = new BankAccount();
        barclays.setAccountNumber("ZZZ-009");
        barclays.getOwners().add(owner);
        owner.getBankAccounts().add(barclays);

        session.delete(soge);
        //session.saveOrUpdate(owner);
        tx.commit();

        assertThat(TestHelper.assertNumberOfEntities(2, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(2, sessions)).isTrue();
        session.clear();

        //delete data
        tx = session.beginTransaction();
        owner = TestHelper.get(session, AccountOwner.class, owner.getId());
        assertThat(owner.getBankAccounts()).hasSize(1);
        assertThat(owner.getBankAccounts()).onProperty("id").contains(barclays.getId());
        barclays = owner.getBankAccounts().iterator().next();
        barclays.getOwners().clear();
        owner.getBankAccounts().clear();
        session.delete(barclays);
        session.delete(owner);
        tx.commit();

        assertThat(TestHelper.assertNumberOfEntities(0, sessions)).isTrue();
        assertThat(TestHelper.assertNumberOfAssociations(0, sessions)).isTrue();

        session.close();
        checkCleanCache();

    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] {
                AccountOwner.class,
                BankAccount.class
        };
    }
}
