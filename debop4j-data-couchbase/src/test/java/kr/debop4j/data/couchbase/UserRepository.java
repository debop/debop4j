package kr.debop4j.data.couchbase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * com.couchbase.UserRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 3. 오후 6:14
 */
@Repository
@Slf4j
public class UserRepository {

    public final User getUser(String id) {
        return getUser(id, 1000);
    }

    @Cacheable( value = "user", key = "#id" )
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
