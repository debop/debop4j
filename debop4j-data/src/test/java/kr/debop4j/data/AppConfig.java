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

package kr.debop4j.data;

import kr.debop4j.data.hibernate.repository.IHibernateDao;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.data.AppConfig
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 26
 */
@Configuration
@EnableTransactionManagement
@EnableAsync
@ComponentScan(basePackageClasses = { UnitOfWorks.class, IHibernateDao.class })
@Import({ kr.debop4j.data.DatabaseConfig.class })
public class AppConfig {

    // 추가해야 할 것
}
