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

package kr.debop4j.data.hibernate.interceptor;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 엔티티의 변화에 대해 로그를 기록한 후 이를 DB에 저장하는
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 16.
 */
public class LoggingInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final long serialVersionUID = -5900714659767205225L;

    private List<Log> logs = Lists.newArrayList();

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!(entity instanceof Log)) {
            if (isTraceEnabled)
                logs.add(new Log("insert", (String) id, entity.getClass().getName()));
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (!(entity instanceof Log)) {
            if (isTraceEnabled)
                logs.add(new Log("update", (String) id, entity.getClass().getName()));
        }
        return false;
    }

    @Override
    public void postFlush(Iterator entities) {
        // 로그 정보를 기타 DB에 따로 저장할 수 있습니다^^
        //
        if (isTraceEnabled) {
            for (Log x : logs) {
                log.debug("[{}]", x);
            }
        }
        logs.clear();
    }

    @Getter
    @Setter
    public static class Log extends ValueObjectBase {

        private String entityName;
        private String entityId;
        private String action;
        private Calendar time;

        public Log() {
        }

        public Log(String action, String entityId, String entityName) {
            super();
            this.action = action;
            this.entityId = entityId;
            this.entityName = entityName;
            this.time = Calendar.getInstance();
        }

        @Override
        protected Objects.ToStringHelper buildStringHelper() {
            return super.buildStringHelper()
                    .add("entityId", entityId)
                    .add("entityName", entityName)
                    .add("action", action)
                    .add("timePart", time);
        }
    }
}
