package kr.debop4j.data.mongodb.dao;

import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;

/**
 * MongoDB를 저장소로 사용하는 Ogm 용 Dao인 MongoOgmDao 에 대한 테스트입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 3:14
 */
@Slf4j
public class MongoOgmDaoImplTest extends MongoGridDatastoreTestBase {

    private static final Random rnd = new Random();
    private static final Date BIRTH = new DateTime().withDate(1968, 10, 14).toDate();
    private static final int PLAYER_COUNT = 10000;
    private static final int TOURNAMENT_COUNT = 16;

    @Test
    public void setupTest() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        assertThat(dao).isNotNull();
    }

    private Player createPlayer() {
        Player player = new Player();

        player.setName("성혁");
        player.setSurname("배");
        player.setAge(45);
        player.setBirth(BIRTH);
        return player;
    }

    private Tournament createTournament(String name) {
        Tournament tournament = new Tournament();
        tournament.setTournament(name);
        return tournament;
    }

    private List<Player> createTestPlayers(int count) {

        List<Player> players = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {
            Player player = createPlayer();
            player.setName("이름-" + i);
            player.setAge(i);
            players.add(player);
        }

        List<Tournament> tournaments = Lists.newArrayListWithCapacity(TOURNAMENT_COUNT);
        for (int i = 0; i < TOURNAMENT_COUNT; i++) {
            Tournament tournament = createTournament("토너먼트-" + i);

            for (int j = 0; j < 22; j++) {
                Player player = players.get(rnd.nextInt(count));
                tournament.getPlayers().add(player);
                player.getTournaments().add(tournament);
            }
            tournaments.add(tournament);
        }
        return players;
    }

    @Test
    public void crud() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        Player player = createPlayer();

        dao.saveOrUpdate(player);
        UnitOfWorks.getCurrent().flushSession();
        UnitOfWorks.getCurrent().clearSession();

        Player loaded = dao.get(Player.class, player.getId());

        assertThat(loaded).isNotNull();
        log.debug("loaded=[{}]", loaded);
        assertThat(loaded.getName()).isEqualTo(player.getName());

        loaded.setAge(46);
        dao.saveOrUpdate(loaded);
        UnitOfWorks.getCurrent().flushSession();
        UnitOfWorks.getCurrent().clearSession();

        loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getName()).isEqualTo(player.getName());

        dao.delete(loaded);
        UnitOfWorks.getCurrent().flushSession();
        UnitOfWorks.getCurrent().clearSession();

        loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNull();
    }

    @Test
    public void deleteByIdTest() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        Player player = createPlayer();

        dao.saveOrUpdate(player);
        UnitOfWorks.getCurrent().flushSession();
        UnitOfWorks.getCurrent().clearSession();

        dao.deleteById(Player.class, player.getId());
        UnitOfWorks.getCurrent().flushSession();
        UnitOfWorks.getCurrent().clearSession();

        Player loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNull();
    }


    @Test
    public void findAllTest() throws Exception {
        daoTest(new Action1<HibernateOgmDaoImpl>() {
            @Override
            public void perform(HibernateOgmDaoImpl dao) {
                List<Player> loadedPlayers = dao.findAll(Player.class);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);
            }
        });
    }

    @Test
    public void findByFields() throws Exception {
        daoTest(new Action1<HibernateOgmDaoImpl>() {
            @Override
            public void perform(HibernateOgmDaoImpl dao) {

                // 일자로 검색 (equal)
                Query luceneQuery = dao.getQueryBuilder(Player.class)
                        .keyword().onField("birth").matching(BIRTH)
                        .createQuery();

                List<Player> loadedPlayers = dao.find(Player.class, luceneQuery);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);


                QueryBuilder queryBuilder = dao.getQueryBuilder(Player.class);

                // AND 검색 ( name like xxx% and surname = xxx)
                Query nameQuery = queryBuilder.keyword().onField("name").matching("이름*").createQuery();
                Query surnameQuery = queryBuilder.keyword().onField("surname").matching("배").createQuery();

                luceneQuery = queryBuilder
                        .bool()
                        .must(nameQuery)
                        .must(surnameQuery)
                        .createQuery();

                loadedPlayers = dao.find(Player.class, luceneQuery);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);

                // RANGE 25 < x <= 28 (excludeLimit)
                luceneQuery = queryBuilder.range().onField("age").from(25).to(28).excludeLimit().createQuery();
                loadedPlayers = dao.find(Player.class, luceneQuery);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(1);
                for (Player player : loadedPlayers)
                    log.debug("Player=[{}]", player);
            }
        });
    }


    public void daoTest(Action1<HibernateOgmDaoImpl> action) throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        List<Player> players = createTestPlayers(PLAYER_COUNT);

        for (Player player : players) {
            dao.saveOrUpdate(player);
        }
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        assertThat(dao.count(Player.class)).isEqualTo(PLAYER_COUNT);

        action.perform(dao);

        dao.deleteAll(Player.class);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        assertThat(dao.count(Player.class)).isEqualTo(0);
    }
}
