package kr.debop4j.core.cache.ehcache;

import com.google.common.collect.Lists;
import kr.debop4j.core.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * kr.debop4j.core.cache.ehcache.UserRepository
 *
 * @author sunghyouk.bae@gmail.com
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

        user.setFirstName("성혁");
        user.setLastName("배");
        user.setAddressStr("정릉1동 현대홈타운 107동 301호");
        user.setCity("서울");
        user.setState("서울");
        user.setEmail("sunghyouk.bae@gmail.com");


        user.getHomeAddress().setPhone("999-9999");
        user.getHomeAddress().setStreet("정릉1동 현대홈타운 107동 301호");
        user.getHomeAddress().getProperties().addAll(Lists.newArrayList("home", "addr"));

        user.getOfficeAddress().setPhone("555-5555");
        user.getOfficeAddress().setStreet("동작동 삼성옴니타워 4층");
        user.getOfficeAddress().getProperties().addAll(Lists.newArrayList("office", "addr"));

        for (int i = 0; i < favoriteMovieSize; i++)
            user.getFavoriteMovies().add("Favorite Movie Number-" + i);

        if (UserRepository.log.isDebugEnabled())
            UserRepository.log.debug("Create User...");

        return user;
    }
}
