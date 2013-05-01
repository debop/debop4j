package kr.debop4j.data.mongodb;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 테스트용 엔티티입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 12. 5.
 */
@Slf4j
@Getter
@Setter
public class User extends ValueObjectBase implements Comparable<User> {

    private static final long serialVersionUID = 4195391816802258792L;

    private String firstName;
    private String lastName;
    private String addressStr;
    private String city;
    private String state;
    private String zipcode;
    private String email;
    private String username;
    private String password;

    private Double age;
    private Date updateTime;

    private byte[] byteArray = ArrayTool.getRandomBytes(1024);

    private String Description;

    private Address homeAddress = new Address();
    private Address officeAddress = new Address();

    List<String> favoriteMovies = Lists.newArrayList();

    @Override
    public int hashCode() {
        return HashTool.compute(username, password, firstName, lastName, city, state, age);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("username", username)
                .add("password", password);
    }

    @Override
    public int compareTo(User that) {
        // 여러항목에 걸쳐 정렬을 수행할 때 Guava의 ComparisonChain 을 이용하면 좋다.
        return ComparisonChain.start()
                .compare(this.firstName, that.firstName)
                .compare(this.lastName, that.lastName)
                .compare(this.zipcode, that.zipcode)
                .compare(this.age, that.age, Ordering.natural().nullsLast())
                .result();
    }

    @Getter
    @Setter
    public static class Address extends ValueObjectBase implements Comparable<Address> {

        private static final long serialVersionUID = 5004748205792679032L;

        private String street;
        private String phone;

        private List<String> properties = Lists.newArrayList();

        @Override
        public int hashCode() {
            return HashTool.compute(street, phone);
        }

        @Override
        public int compareTo(Address that) {
            return ComparisonChain.start()
                    .compare(this.street, that.street, Ordering.natural().nullsFirst())
                    .compare(this.phone, that.phone, Ordering.natural().nullsFirst())
                    .result();
        }
    }

    public static User getUser(int favoriteMovieSize) {

        User user = new User();
        user.setFirstName("성혁");
        user.setLastName("배");
        user.setAddressStr("정릉1동 현대홈타운 107동 301호");
        user.setCity("서울");
        user.setState("서울");
        user.setEmail("sunghyouk.bae@gmail.com");
        user.setUsername("debop");
        user.setPassword("debop");

        user.getHomeAddress().setPhone("999-9999");
        user.getHomeAddress().setStreet("정릉1동 현대홈타운 107동 301호");
        user.getHomeAddress().getProperties().addAll(Lists.newArrayList("home", "addr"));

        user.getOfficeAddress().setPhone("555-5555");
        user.getOfficeAddress().setStreet("동작동 삼성옴니타워 4층");
        user.getOfficeAddress().getProperties().addAll(Lists.newArrayList("office", "addr"));

        for (int i = 0; i < favoriteMovieSize; i++)
            user.getFavoriteMovies().add("Favorite Movie Number-" + i);

        if (log.isDebugEnabled())
            log.debug("Create User...");

        return user;
    }
}
