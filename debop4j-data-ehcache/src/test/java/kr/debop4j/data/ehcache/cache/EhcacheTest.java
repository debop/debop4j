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

package kr.debop4j.data.ehcache.cache;

import kr.debop4j.core.AutoStopwatch;
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

        User user1, user2;
        try (AutoStopwatch stopwatch = new AutoStopwatch("initial User")) {
            user1 = userRepository.getUser("debop", 100);
        }

        try (AutoStopwatch stopwatch = new AutoStopwatch("from Cache")) {
            user2 = userRepository.getUser("debop", 100);
        }
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
