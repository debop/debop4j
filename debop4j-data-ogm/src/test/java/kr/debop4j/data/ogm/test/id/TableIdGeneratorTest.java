package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.TableIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 5:05
 */
@Slf4j
public class TableIdGeneratorTest extends JpaTestBase {

    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[]{
                Music.class,
                Video.class
        };
    }

    @Test
    public void tableIdGenerator() throws Exception {
        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();
        Music music = new Music();
        music.setName("Variations Sur Marilou");
        music.setComposer("Gainsbourg");
        em.persist(music);
        Video video = new Video();
        video.setDirector("Wes Craven");
        video.setName("Scream");
        em.persist(video);
        getTransactionManager().commit();

        em.clear();

        getTransactionManager().begin();
        music = em.find(Music.class, music.getId());
        assertThat(music).isNotNull();
        assertThat(music.getName()).isEqualTo("Variations Sur Marilou");
        em.remove(music);
        video = em.find(Video.class, video.getId());
        assertThat(video).isNotNull();
        assertThat(video.getName()).isEqualTo("Scream");
        em.remove(video);
        getTransactionManager().commit();

        em.close();
    }
}
