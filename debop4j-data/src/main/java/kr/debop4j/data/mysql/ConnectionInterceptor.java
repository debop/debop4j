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

package kr.debop4j.data.mysql;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * MySQL Replication 환경 (Master-Slave)에서 {@link ReadOnlyConnection} 이 정의된 Method에 대해서는
 * Slave 서버로 접속하기 위해, {@link java.sql.Connection#isReadOnly()}의 속성을 true로 변경하여 작업을 수행하도록 합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 5. 오후 11:07
 */
@Aspect
@Component
public class ConnectionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ConnectionInterceptor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();

    @Autowired
    private SessionFactory sessionFactory;

    @Around("@annotation(ReadOnlyConnection)")
    public Object proceed(ProceedingJoinPoint pjp) throws Throwable {

        log.debug("읽기전용 작업을 수행하기 위해 현 connection를 readonly로 설정합니다...");

        SessionImpl session = (SessionImpl) sessionFactory.getCurrentSession();
        Connection connection = session.connection();

        boolean autoCommit = connection.getAutoCommit();
        boolean readOnly = connection.isReadOnly();

        try {
            // MySQL SLAVE 서버에 접속하기 위해 Connection 속성을 설정합니다.
            connection.setAutoCommit(false);
            connection.setReadOnly(true);

            // @ReadOnlyConnection이 선언된 메소드를 실행합니다.
            return pjp.proceed();

        } finally {
            connection.setAutoCommit(autoCommit);
            connection.setReadOnly(readOnly);
            log.debug("읽기전용 작업을 수행하고, connection의 원래 설정으로 재설정했습니다.");
        }
    }
}
