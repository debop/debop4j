package kr.debop4j.access.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.access.test.AppConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 2.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"kr.debop4j.access.repository", "kr.debop4j.access.service"})
@Import({UsingPostgreSqlConfiguration.class})
public class AppConfig {

    // @ComponentScan 으로 @Repository, @Service 는 정의할 필요 없다.
}
