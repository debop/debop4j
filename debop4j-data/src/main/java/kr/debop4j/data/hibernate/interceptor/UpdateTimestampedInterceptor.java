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

import kr.debop4j.data.model.IUpdateTimestampedEntity;
import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * {@link IUpdateTimestampedEntity} 를 구현한 엔티티의 updateTimestamp 값을 엔티티 저장 시에 갱신해 주는 Interceptor 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 21.
 */
public class UpdateTimestampedInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UpdateTimestampedInterceptor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final long serialVersionUID = 7231248402093351095L;

    /** Instantiates a new Update timestamped interceptor. */
    public UpdateTimestampedInterceptor() {
        super();
        if (isTraceEnabled)
            log.trace("UpdateTimestampedInterceptor 생성");
    }

    public void preFlush(Iterator entities) {
        while (entities.hasNext()) {
            Object entity = entities.next();
            if (entity instanceof IUpdateTimestampedEntity) {
                ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();

                if (isTraceEnabled)
                    log.trace("updateTimestamp 값을 현재 시각으로 갱신했습니다. entity=[{}]", entity);
            }
        }
    }
}
