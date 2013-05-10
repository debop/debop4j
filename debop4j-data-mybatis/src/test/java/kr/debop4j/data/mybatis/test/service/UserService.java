package kr.debop4j.data.mybatis.test.service;

import kr.debop4j.data.mybatis.test.domain.model.User;
import kr.debop4j.data.mybatis.test.mappers.UserMapper;
import org.apache.ibatis.session.SqlSession;

/**
 * kr.debop4j.data.mybatis.test.service.UserService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 10. 오후 7:33
 */
public class UserService {

    public void insertUser(User user) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            userMapper.insertUser(user);
            session.commit();
        }
    }

    public User getUserById(Integer userId) {
        try (SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            return userMapper.getUserById(userId);
        }
    }
}
