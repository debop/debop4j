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

package kr.debop4j.data.hibernate.tools;

import kr.debop4j.core.Action1;
import kr.debop4j.core.Guard;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.sql.Connection;

/**
 * StatelessSession 을 이용하여 작업할 수 있도록 제공하는 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 19.
 */
@Slf4j
public class StatelessTool {

    private StatelessTool() {}

    public static void executeTransactional(Action1<StatelessSession> action) {
        executeTransactional(UnitOfWorks.getCurrentSession(), action);
    }

    /** 지정한 session으로부터 StatelessSession을 생성한 후 작업을 수행하고, 닫습니다. */
    public static void executeTransactional(Session session, Action1<StatelessSession> action) {
        if (log.isDebugEnabled())
            log.debug("StatelessSession을 이용하여 Transaction 하에서 특정 작업을 수행합니다.");

        StatelessSession stateless = openStatelessSession(session);
        Transaction tx = null;

        try {
            tx = stateless.beginTransaction();
            action.perform(stateless);
            tx.commit();
        } catch (Exception e) {
            log.error("StatelessSession에서 작업이 실패했습니다. rollback 합니다.", e);
            if (tx != null)
                tx.rollback();
        } finally {
            stateless.close();
        }
    }

    public static void executeTransactional(Session session, Iterable<Action1<StatelessSession>> actions) {
        if (log.isDebugEnabled())
            log.debug("StatelessSession을 이용하여 Transaction 하에서 특정 작업을 수행합니다.");

        StatelessSession stateless = openStatelessSession(session);
        Transaction tx = null;

        try {
            tx = stateless.beginTransaction();
            for (Action1<StatelessSession> action : actions) {
                action.perform(stateless);
            }
            tx.commit();
        } catch (Exception e) {
            log.error("StatelessSession에서 작업이 실패했습니다. rollback 합니다.", e);
            if (tx != null)
                tx.rollback();
        } finally {
            stateless.close();
        }
    }

    public static void execute(Action1<StatelessSession> action) {
        execute(UnitOfWorks.getCurrentSession(), action);
    }

    public static void execute(Session session, Action1<StatelessSession> action) {
        if (log.isDebugEnabled())
            log.debug("StatelessSession을 이용하여 특정 작업을 수행합니다.");

        StatelessSession stateless = openStatelessSession(session);
        try {
            action.perform(stateless);
        } catch (Exception e) {
            log.error("StatelessSession에서 작업이 실패했습니다. rollback 합니다.", e);
        } finally {
            stateless.close();
        }
    }

    public static void execute(Session session, Iterable<Action1<StatelessSession>> actions) {
        if (log.isDebugEnabled())
            log.debug("StatelessSession을 이용하여 특정 작업을 수행합니다.");

        StatelessSession stateless = openStatelessSession(session);
        try {
            for (Action1<StatelessSession> action : actions)
                action.perform(stateless);
        } catch (Exception e) {
            log.error("StatelessSession에서 작업이 실패했습니다. rollback 합니다.", e);
        } finally {
            stateless.close();
        }
    }

    public static StatelessSession openStatelessSession(Session session) {
        Guard.shouldNotBeNull(session, "session");
        return session.getSessionFactory().openStatelessSession();
    }

    public static StatelessSession openStatelessSession(Connection connection) {
        Guard.shouldNotBeNull(connection, "connection");
        return UnitOfWorks.getCurrentSessionFactory().openStatelessSession(connection);
    }
}
