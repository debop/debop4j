package kr.debop4j.data.couchbase.cache;

import kr.debop4j.core.Stopwatch;
import kr.debop4j.data.couchbase.User;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Couchbase Cache 테스트
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25 오후 5:36
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CouchbaseCacheConfiguration.class })
public class CouchbaseCacheTest {

    @Autowired
    CouchbaseCacheManager cacheManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void clearTest() {
        Assert.assertNotNull(cacheManager);
        Cache cache = cacheManager.getCache("user");
        Assert.assertNotNull(cache);

        //cache.clear();
    }

    @Test
    public void getUserFromCache() {

        Stopwatch sw = new Stopwatch("initial User");
        sw.start();
        User user1 = userRepository.getUser("debop", 100);
        sw.stop();

        sw = new Stopwatch("from Cache");
        sw.start();
        User user2 = userRepository.getUser("debop", 100);
        sw.stop();

        assertThat(user2.getUsername()).isEqualTo(user1.getUsername());
        assertThat(user2.getPassword()).isEqualTo(user1.getPassword());
    }

    @Test
    public void componentConfigurationTest() {
        Assert.assertNotNull(cacheManager);
        Cache cache = cacheManager.getCache("user");

        Assertions.assertThat(cache).isNotNull();
        Assertions.assertThat(userRepository).isNotNull();
    }
}
