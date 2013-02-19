package kr.debop4j.data.hibernate.forTesting;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 여러 Database에 대해, Hibernate 작업 Test를 위한 기본 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 19.
 */
@Slf4j
public abstract class DatabaseTestFixtureBase {

    public static List<UnitOfWorkTestContextBase> contexts = Lists.newArrayList();
}
