package kr.debop4j.data.ehcache.cache;

import kr.debop4j.core.Stopwatch;
import kr.debop4j.data.ehcache.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.debop4j.core.cache.ehcache.EhcacheTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 24. 오후 8:58
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EhcacheConfiguration.class })
public class EhcacheTest {

    @Autowired
    EhCacheCacheManager cacheManager;
    @Autowired
    UserRepository userRepository;

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

        Assert.assertEquals(user1, user2);
    }

    @Test
    public void componentConfigurationTest() {
        Assert.assertNotNull(cacheManager);
        Cache cache = cacheManager.getCache("user");
        Assert.assertNotNull(cache);

        Assert.assertNotNull(userRepository);
    }
}
