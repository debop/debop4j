package kr.debop4j.data.mybatis.test.mappers;

import kr.debop4j.data.mybatis.test.domain.model.User;

import java.util.List;

/**
 * kr.debop4j.data.mybatis.test.mappers.UserMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 7:25
 */
public interface UserMapper {

    public void insertUser(User user);

    public User getUserById(Integer userId);

    public List<User> getAllUsers();

    public void updateUser(User user);

    public void deleteUser(Integer userId);
}
