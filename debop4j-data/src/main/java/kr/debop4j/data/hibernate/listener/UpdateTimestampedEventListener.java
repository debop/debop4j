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

package kr.debop4j.data.hibernate.listener;

import kr.debop4j.data.model.IUpdateTimestampedEntity;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

/**
 * {@link IUpdateTimestampedEntity} 를 구현한 엔티티의 updateTimestamp 값을 엔티티 저장 시에 갱신해 주는 Event Listener 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 22.
 */
public class UpdateTimestampedEventListener implements PreInsertEventListener, PreUpdateEventListener {

    /** Instantiates a new Update timestamped event listener. */
    public UpdateTimestampedEventListener() { }

    /**
     * 엔티티를 새로 추가하기 전에 최종 갱신 시간을 추가합니다.
     *
     * @param event 이벤트 정보
     * @return event bubbling을 그만 둘 것인가?
     */
    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IUpdateTimestampedEntity) {
            ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();
        }
        return false;
    }

    /**
     * 엔티티를 갱신하기전에 최종 갱신 시간을 갱신합니다.
     *
     * @param event 이벤트 정보
     * @return event bubbling을 그만 둘 것인가?
     */
    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IUpdateTimestampedEntity) {
            ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();
        }
        return false;
    }

    private static final long serialVersionUID = -7472589588444503777L;
}
