package kr.debop4j.data.hibernate.listener;

import kr.debop4j.data.model.IUpdateTimestampedEntity;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

/**
 * {@link IUpdateTimestampedEntity} 를 구현한 엔티티의 updateTimestamp 값을 엔티티 저장 시에 갱신해 주는 Event Listener 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 22.
 */
public class UpdateTimestampedEventListener implements PreInsertEventListener, PreUpdateEventListener {

    private static final long serialVersionUID = -7472589588444503777L;

    public UpdateTimestampedEventListener() { }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IUpdateTimestampedEntity) {
            ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();
        }
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        Object entity = event.getEntity();
        if (entity instanceof IUpdateTimestampedEntity) {
            ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();
        }
        return false;
    }
}
