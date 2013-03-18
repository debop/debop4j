package com.kt.vital.domain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * com.kt.vital.domain.VitalConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 3:52
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.kt.vital.domain.repository", "com.kt.vital.domain..service"})
@Import({UsingPostgreSqlConfiguration.class})
//@Import({UsingMySqlConfiguration.class})
// @Import({UsingHSqlConfiguration.class})
public class VitalConfiguration {
}
