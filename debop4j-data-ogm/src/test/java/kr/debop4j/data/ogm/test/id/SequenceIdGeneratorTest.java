package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.SequenceIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 4:43
 */
@Slf4j
public class SequenceIdGeneratorTest extends JpaTestBase {

    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[]{ Song.class, Actor.class };
    }

    @Test
    public void testSequenceIdGenerationInJTA() throws Exception {
        Song firstSong = new Song();
        Song secondSong = new Song();
        Actor firstActor = new Actor();
        Actor secondActor = new Actor();

        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();
        boolean operationSuccessfull = false;
        try {
            firstSong.setSinger("Charlotte Church");
            firstSong.setTitle("Ave Maria");
            em.persist(firstSong);

            secondSong.setSinger("Charlotte Church");
            secondSong.setTitle("Flower Duet");
            em.persist(secondSong);

            firstActor.setName("Russell Crowe");
            firstActor.setBestMovieTitle("Gladiator");
            em.persist(firstActor);

            secondActor.setName("Johnny Depp");
            secondActor.setBestMovieTitle("Pirates of the Caribbean");
            em.persist(secondActor);
            operationSuccessfull = true;
        } finally {
            commitOrRollback(operationSuccessfull);
        }
        em.clear();

        getTransactionManager().begin();
        operationSuccessfull = false;
        try {
            firstSong = em.find(Song.class, firstSong.getId());
            assertThat(firstSong).isNotNull();
            assertThat(firstSong.getId()).isEqualTo(Song.INITIAL_VALUE);
            assertThat(firstSong.getTitle()).isEqualTo("Ave Maria");
            em.remove(firstSong);

            secondSong = em.find(Song.class, secondSong.getId());
            assertThat(secondSong).isNotNull();
            assertThat(secondSong.getId()).isEqualTo(Song.INITIAL_VALUE + 1);
            assertThat(secondSong.getTitle()).isEqualTo("Flower Duet");
            em.remove(secondSong);

            firstActor = em.find(Actor.class, firstActor.getId());
            assertThat(firstActor).isNotNull();
            assertThat(firstActor.getId()).isEqualTo(Actor.INITIAL_VALUE);
            assertThat(firstActor.getName()).isEqualTo("Russell Crowe");
            em.remove(firstActor);

            secondActor = em.find(Actor.class, secondActor.getId());
            assertThat(secondActor).isNotNull();
            assertThat(secondActor.getId()).isEqualTo(Actor.INITIAL_VALUE + 1);
            assertThat(secondActor.getName()).isEqualTo("Johnny Depp");
            em.remove(secondActor);
            operationSuccessfull = true;
        } finally {
            commitOrRollback(operationSuccessfull);
        }

        em.close();
    }
}
