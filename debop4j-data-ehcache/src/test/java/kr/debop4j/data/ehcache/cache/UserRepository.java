package kr.debop4j.data.ehcache.cache;

import kr.debop4j.data.ehcache.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * kr.debop4j.core.cache.ehcache.UserRepository
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
