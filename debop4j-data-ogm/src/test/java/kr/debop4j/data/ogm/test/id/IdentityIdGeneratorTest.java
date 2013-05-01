package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.IdentityIdGeneratorTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 4:06
 */
@Slf4j
public class IdentityIdGeneratorTest extends JpaTestBase {
    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[] { Animal.class };
    }

    @Test
    public void testIdentityGenerator() throws Exception {
        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();
        Animal jungleKing = new Animal();
        Animal fish = new Animal();
        boolean ok = false;
        try {
            jungleKing.setName("Lion");
            jungleKing.setSpecies("Mammal");
            em.persist(jungleKing);

            fish.setName("Shark");
            fish.setSpecies("Tiger Shark");
            em.persist(fish);
            ok = true;
        } finally {
            commitOrRollback(ok);
        }
        em.clear();

        getTransactionManager().begin();
        ok = false;
        try {
            Animal animal = em.find(Animal.class, jungleKing.getId());
            assertThat(animal).isNotNull();
            assertThat(animal.getId()).isEqualTo(1);
            assertThat(animal.getName()).isEqualTo("Lion");
            em.remove(animal);

            animal = em.find(Animal.class, fish.getId());
            assertThat(animal).isNotNull();
            assertThat(animal.getId()).isEqualTo(2);
            assertThat(animal.getName()).isEqualTo("Shark");
            em.remove(animal);
            ok = true;
        } finally {
            commitOrRollback(ok);
        }
        em.close();
    }
}
