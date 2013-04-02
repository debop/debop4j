package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.IdentityIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 4:06
 */
@Slf4j
@Ignore("JTA 에서만 제대로 작동한다.")
public class IdentityIdGeneratorTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ Animal.class };
    }

    @Test
    public void testIdentityGenerator() throws Exception {
        final Session session = openSession();
        Transaction tx = session.beginTransaction();

        Animal jungleKing = new Animal();
        Animal fish = new Animal();

        try {
            jungleKing.setName("Lion");
            jungleKing.setSpecies("Mammal");
            session.persist(jungleKing);

            fish.setName("Shark");
            fish.setSpecies("Tiger Shark");
            session.persist(fish);

            tx.commit();
        } catch (Exception e) {
            log.error("예외 발생!!!", e);
            try {
                tx.rollback();
            } catch (Exception inner) {
                log.error("rollback 에러", inner);
            }
            Assert.fail("예외가 발생하여 rollback 했습니다.");
        }
        session.clear();

        tx = session.beginTransaction();
        try {
            Animal animal = (Animal) session.get(Animal.class, jungleKing.getId());
            assertThat(animal).isNotNull();
            assertThat(animal.getId()).isEqualTo(1);
            assertThat(animal.getName()).isEqualTo("Lion");
            session.delete(animal);

            animal = (Animal) session.get(Animal.class, fish.getId());
            assertThat(animal).isNotNull();
            assertThat(animal.getId()).isEqualTo(2);
            assertThat(animal.getName()).isEqualTo("Shark");
            session.delete(animal);

            tx.commit();
        } catch (Exception e) {
            log.error("예외 발생!!!", e);
            try {
                tx.rollback();
            } catch (Exception inner) {
                log.error("rollback 에러", inner);
            }
            Assert.fail("예외가 발생하여 rollback 했습니다.");
        }
        session.close();
    }
}
