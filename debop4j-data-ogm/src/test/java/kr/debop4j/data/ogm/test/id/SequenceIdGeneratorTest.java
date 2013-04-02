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
 * kr.debop4j.data.ogm.test.id.SequenceIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 4:43
 */
@Slf4j
@Ignore("JTA 에서만 제대로 작동한다.")
public class SequenceIdGeneratorTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{ Song.class, Actor.class };
    }

    @Test
    public void testSequenceIdGenerationInJTA() throws Exception {
        Song firstSong = new Song();
        Song secondSong = new Song();
        Actor firstActor = new Actor();
        Actor secondActor = new Actor();

        final Session session = openSession();
        Transaction tx = session.beginTransaction();

        boolean operationSuccessfull = false;
        try {
            firstSong.setSinger("Charlotte Church");
            firstSong.setTitle("Ave Maria");
            session.persist(firstSong);

            secondSong.setSinger("Charlotte Church");
            secondSong.setTitle("Flower Duet");
            session.persist(secondSong);

            firstActor.setName("Russell Crowe");
            firstActor.setBestMovieTitle("Gladiator");
            session.persist(firstActor);

            secondActor.setName("Johnny Depp");
            secondActor.setBestMovieTitle("Pirates of the Caribbean");
            session.persist(secondActor);
            operationSuccessfull = true;

            tx.commit();
        } catch (Exception e) {
            log.error("예외 발생", e);
            tx.rollback();
            Assert.fail(e.getMessage());
        }
        session.clear();

        tx = session.beginTransaction();
        try {
            firstSong = (Song) session.get(Song.class, firstSong.getId());
            assertThat(firstSong).isNotNull();
            assertThat(firstSong.getId()).isEqualTo(Song.INITIAL_VALUE);
            assertThat(firstSong.getTitle()).isEqualTo("Ave Maria");
            session.delete(firstSong);

            secondSong = (Song) session.get(Song.class, secondSong.getId());
            assertThat(secondSong).isNotNull();
            assertThat(secondSong.getId()).isEqualTo(Song.INITIAL_VALUE + 1);
            assertThat(secondSong.getTitle()).isEqualTo("Flower Duet");
            session.delete(secondSong);

            firstActor = (Actor) session.get(Actor.class, firstActor.getId());
            assertThat(firstActor).isNotNull();
            assertThat(firstActor.getId()).isEqualTo(Actor.INITIAL_VALUE);
            assertThat(firstActor.getName()).isEqualTo("Russell Crowe");
            session.delete(firstActor);

            secondActor = (Actor) session.get(Actor.class, secondActor.getId());
            assertThat(secondActor).isNotNull();
            assertThat(secondActor.getId()).isEqualTo(Actor.INITIAL_VALUE + 1);
            assertThat(secondActor.getName()).isEqualTo("Johnny Depp");
            session.delete(secondActor);

            tx.commit();
        } catch (Exception e) {
            log.error("예외 발생", e);
            tx.rollback();
            Assert.fail(e.getMessage());
        }
        session.close();
    }
}
