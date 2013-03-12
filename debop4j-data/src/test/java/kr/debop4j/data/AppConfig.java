package kr.debop4j.data;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.data.AppConfig
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 26
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"kr.debop4j.data.hibernate.repository"})
@Import({kr.debop4j.data.DatabaseConfig.class})
public class AppConfig {


}
