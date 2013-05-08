package kr.debop4j.data.mongodb.dao;

import com.google.common.collect.Lists;
import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import kr.debop4j.data.hibernate.unitofwork.IUnitOfWork;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorkNestingOptions;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.mongodb.MongoGridDatastoreTestBase;
import kr.debop4j.data.mongodb.model.Player;
import kr.debop4j.data.mongodb.model.Tournament;
import kr.debop4j.data.ogm.dao.IHibernateOgmDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;

/**
 * MongoDB를 저장소로 사용하는 Ogm 용 Dao인 MongoOgmDao 에 대한 테스트입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 16. 오후 3:14
 */
@Slf4j
public class MongoOgmDaoTest extends MongoGridDatastoreTestBase {

    private static final Random rnd = new Random();
    private static final Date BIRTH = DateTime.now().withTimeAtStartOfDay().withDate(1968, 10, 14).toDate();
    private static final int REPEAT_COUNT = 10;
    private static final int PLAYER_COUNT = 1000;
    private static final int TOURNAMENT_COUNT = 16;

    @Autowired
    MongoOgmDao dao;

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
    public void searchQueryTest() throws Exception {
        daoInParallel(new Action1<IHibernateOgmDao>() {
            @Override
            public void perform(IHibernateOgmDao dao) {

                // findAll
                List<Player> loadedPlayers = dao.findAll(Player.class);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);
                log.debug("findAll seach result = [{}]", loadedPlayers.size());

                // 일자로 검색 (equal)
                Query luceneQuery = dao.getQueryBuilder(Player.class)
                        .keyword().onField("birth").matching(BIRTH)
                        .createQuery();

                loadedPlayers = dao.find(Player.class, luceneQuery);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);
                log.debug("birth seach result = [{}]", loadedPlayers.size());

                QueryBuilder queryBuilder = dao.getQueryBuilder(Player.class);

                // AND 검색 ( name like xxx% and surname = xxx)
                Query nameQuery = queryBuilder.keyword().wildcard().onField("name").matching("이름*").createQuery();
                Query surnameQuery = queryBuilder.keyword().onField("surname").matching("배").createQuery();

                luceneQuery = queryBuilder
                        .bool()
                        .must(nameQuery)
                        .must(surnameQuery)
                        .createQuery();

                loadedPlayers = dao.find(Player.class, luceneQuery);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(0);
                log.debug("AND Seach result = [{}]", loadedPlayers.size());

                // RANGE 25 < x <= 28 (excludeLimit)
                luceneQuery = queryBuilder.range().onField("age").from(25).to(28).excludeLimit().createQuery();
                loadedPlayers = dao.find(Player.class, luceneQuery, null);
                assertThat(loadedPlayers).isNotNull();
                assertThat(loadedPlayers.size()).isGreaterThan(1);
                log.debug("range seach result = [{}]", loadedPlayers.size());
                for (Player player : loadedPlayers)
                    log.debug("Player=[{}]", player);
            }
        });
    }

    public void daoInSerial(Action1<IHibernateOgmDao> action) throws Exception {

        for (int i = 0; i < REPEAT_COUNT; i++) {
            List<Player> players = createTestPlayers(PLAYER_COUNT);
            for (Player player : players) {
                dao.persist(player);
            }
            dao.flushToIndexes();
            dao.getSession().flush();
            dao.getSession().clear();

            log.debug("Player [{}]명을 추가했습니다.", players.size());
        }

        try {
            action.perform(dao);
        } finally {
            log.debug("Player 엔티티를 삭제합니다...");
            List<Player> players = dao.findAll(Player.class);
            assertThat(players.size()).isGreaterThan(0);
            dao.deleteAll(players);
            UnitOfWorks.getCurrent().flushSession();
            UnitOfWorks.getCurrent().clearSession();
        }
        assertThat(dao.count(Player.class)).isEqualTo(0);
    }

    public void daoInParallel(Action1<IHibernateOgmDao> action) throws Exception {

        Parallels.run(REPEAT_COUNT, new Action1<Integer>() {
            @Override
            public void perform(Integer arg) {
                try (IUnitOfWork unitOfWork = UnitOfWorks.start(UnitOfWorkNestingOptions.CreateNewOrNestUnitOfWork)) {
                    List<Player> players = createTestPlayers(PLAYER_COUNT);

                    for (Player player : players) {
                        dao.saveOrUpdate(player);
                    }
                    dao.getSession().flush();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {}
                    /**
                     * 병렬 작업 시에는 flushToIndexes() 메소드를 호출하여,
                     * session이 닫히거나 스레드가 중단되기 전에 인덱싱을 마무리하도록 한다.
                     */
                    dao.flushToIndexes();
                    dao.getSession().close();

                    log.debug("Player [{}]명을 추가했습니다.", players.size());
                } catch (Exception e) {
                    log.error("예외 발생", e);
                }
            }
        });

        try {
            action.perform(dao);
        } finally {
            log.debug("Player 엔티티를 삭제합니다...");
            List<Player> players = dao.findAll(Player.class);
            assertThat(players.size()).isGreaterThan(0);
            dao.deleteAll(players);
            dao.getFullTextSession().flush();
            dao.flushToIndexes();
            dao.getFullTextSession().close();
        }
        assertThat(dao.count(Player.class)).isEqualTo(0);
    }

}
