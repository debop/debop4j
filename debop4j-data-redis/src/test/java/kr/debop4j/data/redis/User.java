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

package kr.debop4j.data.redis;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
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
public class User implements Serializable, Comparable<User> {

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

    private byte[] byteArray = new byte[1024];

    private String Description;

    private Address homeAddress = new Address();
    private Address officeAddress = new Address();

    List<String> favoriteMovies = Lists.newArrayList();

    @Override
    public int hashCode() {
        return Objects.hashCode(username, password, firstName, lastName, city, state, age);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("username", username)
                .add("password", password)
                .toString();
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
    public static class Address implements Serializable, Comparable<Address> {

        private static final long serialVersionUID = 5004748205792679032L;

        private String street;
        private String phone;

        private List<String> properties = Lists.newArrayList();

        @Override
        public int hashCode() {
            return Objects.hashCode(street, phone);
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
