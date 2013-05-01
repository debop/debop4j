package kr.debop4j.data.ogm.test.embeddable;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.embeddable.EmbeddableTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
public class EmbeddableTest extends OgmTestBase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[] { Account.class };
    }

    @Test
    public void embeddableEntity() throws Exception {

        final Session session = openSession();
        Transaction transaction = session.beginTransaction();

        Account account = new Account();
        account.setLogin("emmanuel");
        account.setPassword("li");
        account.setPassword("like I would tell ya");
        account.setHomeAddress(new Address());
        final Address address = account.getHomeAddress();
        address.setCity("Paris");
        address.setCountry("France");
        address.setStreet1("1 avenue des Champs Elysees");
        address.setZipCode("75007");
        session.persist(account);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        final Account loadedAccount = (Account) session.get(Account.class, account.getLogin());
        assertThat(loadedAccount).as("Cannot load persisted object").isNotNull();
        final Address loadedAddress = loadedAccount.getHomeAddress();
        assertThat(loadedAddress).as("Embeddable should not be null").isNotNull();
        assertThat(loadedAddress.getCity()).as("persist and load fails for embeddable").isEqualTo(address.getCity());
        assertThat(loadedAddress.getZipCode()).as("@Column support for embeddable does not work").isEqualTo(address.getZipCode());
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        loadedAddress.setCountry("USA");
        session.merge(loadedAccount);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        Account secondLoadedAccount = (Account) session.get(Account.class, account.getLogin());
        assertThat(loadedAccount.getHomeAddress().getCity())
                .as("Merge fails for embeddable")
                .isEqualTo(secondLoadedAccount.getHomeAddress().getCity());
        session.delete(secondLoadedAccount);
        transaction.commit();

        session.clear();

        transaction = session.beginTransaction();
        assertThat(session.get(Account.class, account.getLogin())).isNull();
        transaction.commit();

        session.close();
    }
}
