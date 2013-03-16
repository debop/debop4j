package kr.debop4j.ogm.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 환경설정 파일
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 16.
 */
@Configuration
@Import({MongoDBConfiguration.class})
public class AppConfig {
}
