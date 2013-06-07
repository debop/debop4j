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

import kr.debop4j.data.ehcache.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Sprint Cache를 테스트하기 위한 UserRepository
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 *         13. 3. 24. 오후 10:19
 */
@Repository
@Slf4j
public class UserRepository {

    @Cacheable(value = "user", key = "#id")
    public User getUser(String id) {
        return getUser(id, 1000);
    }

    @Cacheable(value = "user", key = "#id")
    public User getUser(String id, int favoriteMovieSize) {
        User user = new User();

        user.setUsername(id);
        user.setPassword(id);
        user.setEmail("sunghyouk.bae@gmail.com");

        user.getHomeAddress().setPhone("999-9999");
        user.getOfficeAddress().setPhone("555-5555");

        for (int i = 0; i < favoriteMovieSize; i++)
            user.getFavoriteMovies().add("Favorite Movie Number-" + i);

        if (log.isDebugEnabled())
            log.debug("Create User...");

        return user;
    }
}
