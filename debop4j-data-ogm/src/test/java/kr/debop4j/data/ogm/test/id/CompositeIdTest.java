package kr.debop4j.data.ogm.test.id;

import kr.debop4j.data.ogm.test.utils.jpa.JpaTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.id.CompositeIdTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 2. 오후 3:40
 */
@Slf4j
public class CompositeIdTest extends JpaTestBase {

    @Override
    public Class<?>[] getEntities() {
        return new Class<?>[]{
                News.class,
                NewsID.class,
                Label.class
        };
    }

    @Test
    public void compisteEmbeddedId() throws Exception {

        final String titleOGM = "How to use Hibernate OGM ?";
        final String titleAboutJUG = "What is a JUG ?";
        final String titleCountJUG = "There are more than 20 JUGs in France";

        final String author = "Guillaume";

        final String contentOGM = "Simple, just like ORM but with a NoSQL database";
        final String contentAboutJUG = "JUG means Java User Group";
        final String contentCountJUG = "Great! Congratulations folks";

        Label questionLabel = new Label("question");
        Label jugLabel = new Label("jug");
        Label hibernateLabel = new Label("hibernate");
        Label ogmLabel = new Label("OGM");
        Label statJugLabel = new Label("statJUG");

        NewsID newsOgmID = new NewsID(titleOGM, author);
        NewsID newsAboutJugID = new NewsID(titleAboutJUG, author);
        NewsID newsCountJugID = new NewsID(titleCountJUG, author);

        final List<Label> newsOgmLabels = new ArrayList<Label>();
        newsOgmLabels.add(ogmLabel);
        newsOgmLabels.add(hibernateLabel);

        final List<Label> newsAboutJugLabels = new ArrayList<Label>();
        newsAboutJugLabels.add(jugLabel);
        newsAboutJugLabels.add(questionLabel);

        final List<Label> newsCountJugLabels = new ArrayList<Label>();
        newsCountJugLabels.add(statJugLabel);


        News newsAboutJUG = new News(newsAboutJugID, contentAboutJUG, newsAboutJugLabels);
        News newsOGM = new News(newsOgmID, contentOGM, newsOgmLabels);
        News newsCountJUG = new News(newsCountJugID, contentCountJUG, newsCountJugLabels);

        boolean operationSuccessful = false;
        getTransactionManager().begin();
        final EntityManager em = getFactory().createEntityManager();

        try {
            em.persist(newsOGM);
            em.persist(newsAboutJUG);
            em.persist(newsCountJUG);
            operationSuccessful = true;
        } finally {
            commitOrRollback(operationSuccessful);
        }
        em.clear();
        getTransactionManager().begin();
        operationSuccessful = false;
        try {
            News news = em.find(News.class, newsOgmID);
            assertThat(news).isNotNull();
            assertThat(news.getContent()).isEqualTo(contentOGM);
            assertThat(news.getNewsId().getAuthor()).isEqualTo(author);
            assertThat(news.getNewsId().getTitle()).isEqualTo(titleOGM);
            assertThat(news.getLabels().size()).isEqualTo(newsOgmLabels.size());
            em.remove(news);
            assertThat(em.find(News.class, newsOgmID)).isNull();

            em.clear();
            news = em.find(News.class, newsAboutJugID);
            assertThat(news).isNotNull();
            assertThat(news.getContent()).isEqualTo(contentAboutJUG);
            assertThat(news.getNewsId().getAuthor()).isEqualTo(author);
            assertThat(news.getNewsId().getTitle()).isEqualTo(titleAboutJUG);
            assertThat(news.getLabels().size()).isEqualTo(newsAboutJugLabels.size());
            em.remove(news);
            assertThat(em.find(News.class, newsAboutJugID)).isNull();

            em.clear();
            news = em.find(News.class, newsCountJugID);
            assertThat(news).isNotNull();
            assertThat(news.getContent()).isEqualTo(contentCountJUG);
            assertThat(news.getNewsId().getAuthor()).isEqualTo(author);
            assertThat(news.getNewsId().getTitle()).isEqualTo(titleCountJUG);
            assertThat(news.getLabels().size()).isEqualTo(newsCountJugLabels.size());
            em.remove(news);
            assertThat(em.find(News.class, newsCountJugID)).isNull();
        } finally {
            commitOrRollback(operationSuccessful);
        }
        em.close();
    }
}
