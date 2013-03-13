package kr.debop4j.access;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.access.AppConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 2.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"kr.debop4j.access.repository", "kr.debop4j.access.service"})
@Import({UsingPostgreSqlConfiguration.class})
public class AppConfig {
}
