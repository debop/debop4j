/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.data.ogm.dao;

import com.google.common.collect.Lists;
import kr.debop4j.core.spring.Springs;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import kr.debop4j.data.ogm.GridDatastoreTestBase;
import kr.debop4j.data.ogm.dao.impl.HibernateOgmDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * {@link HibernateOgmDao} 테스트 코드
 * 검색 관련 테스트는 debop4j-data-mongodb 를 참고하세요. ( MongoOgmDaoImplTest )
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 11:43
 */
@Slf4j
public class HibernateOgmDaoTest extends GridDatastoreTestBase {

    @Test
    public void createTest() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        assertThat(dao).isNotNull();
    }

    private Player createPlayer() {
        Player player = new Player();

        player.setName("성혁");
        player.setSurname("배");
        player.setAge(45);
        player.setBirth(new Date(1968, 10, 14));
        return player;
    }

    private List<Player> createTestPlayers(int count) {
        List<Player> players = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {
            Player player = createPlayer();
            player.setName("이름-" + i);
            player.setAge(i);
            players.add(player);
        }
        return players;
    }

    @Test
    public void crud() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        Player player = createPlayer();

        dao.saveOrUpdate(player);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        Player loaded = dao.get(Player.class, player.getId());

        assertThat(loaded).isNotNull();
        log.debug("loaded=[{}]", loaded);
        assertThat(loaded.getName()).isEqualTo(player.getName());

        loaded.setAge(46);
        dao.saveOrUpdate(loaded);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNotNull();
        assertThat(loaded.getName()).isEqualTo(player.getName());

        dao.delete(loaded);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNull();
    }

    @Test
    public void deleteByIdTest() throws Exception {
        HibernateOgmDaoImpl dao = Springs.getBean(HibernateOgmDaoImpl.class);
        Player player = createPlayer();

        dao.saveOrUpdate(player);
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        dao.deleteById(Player.class, player.getId());
        UnitOfWorks.getCurrent().transactionalFlush();
        UnitOfWorks.getCurrent().clearSession();

        Player loaded = dao.get(Player.class, player.getId());
        assertThat(loaded).isNull();
    }
}
