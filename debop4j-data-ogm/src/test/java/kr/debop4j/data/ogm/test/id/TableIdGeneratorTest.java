package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.simpleentity.OgmTestBase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.TableIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:05
 */
@Slf4j
@Ignore("JTA 에서만 제대로 작동한다.")
public class TableIdGeneratorTest extends OgmTestBase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{
                Music.class,
                Video.class
        };
    }

    @Test
    public void tableIdGenerator() throws Exception {
        Session session = openSession();
        Transaction tx = session.beginTransaction();

        Music music = new Music();
        music.setName("Variations Sur Marilou");
        music.setComposer("Gainsbourg");
        session.persist(music);
        Video video = new Video();
        video.setDirector("Wes Craven");
        video.setName("Scream");
        session.persist(video);

        tx.commit();

        session.clear();

        tx = session.beginTransaction();
        music = (Music) session.get(Music.class, music.getId());
        assertThat(music).isNotNull();
        assertThat(music.getName()).isEqualTo("Variations Sur Marilou");
        session.delete(music);
        video = (Video) session.get(Video.class, video.getId());
        assertThat(video).isNotNull();
        assertThat(video.getName()).isEqualTo("Scream");
        session.delete(video);
        tx.commit();

        session.close();
    }
}
