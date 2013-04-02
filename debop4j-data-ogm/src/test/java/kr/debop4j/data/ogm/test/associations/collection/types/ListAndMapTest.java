package kr.debop4j.data.ogm.test.associations.collection.types;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.associations.collection.types.ListAndMapTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오전 11:18
 */
@Slf4j
public class ListAndMapTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                User.class,
                Address.class,
                Father.class,
                Child.class
        };
    }

    @Getter
    private Session session;

    @Override
    public void doBefore() throws Exception {
        super.doBefore();
        session = openSession();
    }

    @Override
    public void doAfter() throws Exception {
        if (session != null)
            session.close();
        super.doAfter();
    }

    @Test
    public void orderedListTest() throws Exception {
        Transaction tx = session.beginTransaction();

        Child luke = new Child("luke");
        Child leia = new Child("leia");
        session.persist(luke);
        session.persist(leia);

        Father father = new Father();
        father.getOrderedChildren().add(luke);
        father.getOrderedChildren().add(null);
        father.getOrderedChildren().add(leia);

        session.persist(father);
        tx.commit();

        session.clear();

        tx = session.beginTransaction();
        father = (Father) session.get(Father.class, father.getId());
        assertThat(father.getOrderedChildren())
                .as("List should have 3 elements")
                .hasSize(3);
        assertThat(father.getOrderedChildren().get(0).getName())
                .as("Luke should be first")
                .isEqualTo(luke.getName());
        assertThat(father.getOrderedChildren().get(1))
                .as("Second born should be null")
                .isNull();
        assertThat(father.getOrderedChildren().get(2).getName())
                .as("Leia should be third")
                .isEqualTo(leia.getName());

        session.delete(father);
        // cascade delete가 되었습니다.
        // session.delete(session.load(Child.class, luke.getId()));
        // session.delete(session.load(Child.class, leia.getId()));
        tx.commit();

        checkCleanCache();
    }

    @Test
    public void mapAndElementCollection() throws Exception {

        Address home = new Address();
        home.setCity("Paris");
        Address work = new Address();
        work.setCity("San Francisco");
        User user = new User();
        user.getAddresses().put("home", home);
        user.getAddresses().put("work", work);
        user.getNicknames().add("idrA");
        user.getNicknames().add("day[9]");
        session.persist(home);
        session.persist(work);
        session.persist(user);
        User user2 = new User();
        user2.getNicknames().add("idrA");
        user2.getNicknames().add("day[9]");
        session.persist(user2);

        session.flush();
        session.clear();

        user = (User) session.get(User.class, user.getId());
        assertThat(user.getNicknames())
                .as("Should have 2 nick1")
                .hasSize(2);
        assertThat(user.getNicknames())
                .as("Should contain nicks")
                .contains("idrA", "day[9]");
        user.getNicknames().remove("idrA");

        session.flush();
        session.clear();

        user = (User) session.get(User.class, user.getId());
        // TODO do null value
        assertThat(user.getAddresses())
                .as("List should have 2 elements")
                .hasSize(2);
        assertThat(user.getAddresses().get("home").getCity())
                .as("home address should be under home")
                .isEqualTo(home.getCity());
        assertThat(user.getNicknames())
                .as("Should have 1 nick1")
                .hasSize(1);
        assertThat(user.getNicknames())
                .as("Should contain nick")
                .contains("day[9]");

        session.delete(user);
        // CascadeType.ALL 로 user 삭제 시 address 삭제 됨
        // session.delete(session.load(Address.class, home.getId()));
        // session.delete(session.load(Address.class, work.getId()));

        user2 = (User) session.get(User.class, user2.getId());
        assertThat(user2.getNicknames())
                .as("Should have 2 nicks")
                .hasSize(2);
        assertThat(user2.getNicknames())
                .as("Should contain nick")
                .contains("idrA", "day[9]");
        session.delete(user2);

        session.flush();
        checkCleanCache();
    }
}
