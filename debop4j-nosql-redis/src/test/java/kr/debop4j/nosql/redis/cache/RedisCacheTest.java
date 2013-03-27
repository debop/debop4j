package kr.debop4j.nosql.redis.cache;

import kr.debop4j.nosql.redis.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.debop4j.core.cache.redis.RedisCacheTest
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 26. 오전 11:35
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisCacheConfiguration.class})
public class RedisCacheTest {

    @Autowired
    RedisCacheManager redisCacheManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void clearTest() {
        Assert.assertNotNull(redisCacheManager);
        Cache cache = redisCacheManager.getCache("user");
        Assert.assertNotNull(cache);
        cache.clear();
        Assert.assertNotNull(cache);
    }

    @Test
    public void getUserFromCache() {

        User user1 = userRepository.getUser("debop", 100);
        User user2 = userRepository.getUser("debop", 100);

        Assert.assertEquals(user1.getUsername(), user2.getUsername());
    }

    @Test
    public void componentConfigurationTest() {
        Assert.assertNotNull(redisCacheManager);
        Cache cache = redisCacheManager.getCache("user");
        Assert.assertNotNull(cache);

        cache.evict("debop");

        Assert.assertNotNull(userRepository);
    }
}
