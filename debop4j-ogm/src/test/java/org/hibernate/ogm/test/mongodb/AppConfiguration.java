package org.hibernate.ogm.test.mongodb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * org.hibernate.ogm.test.mongodb.AppConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 23. 오후 6:19
 */
@Configuration
@ComponentScan({"kr.debop4j.ogm.spring.cfg",
                       "kr.debop4j.ogm.tools.mongodb"})
@Import({MongoDBConfiguration.class})
public class AppConfiguration {

}
