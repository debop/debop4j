package kr.debop4j.data.hibernate.interceptor;

import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;

import java.util.Iterator;

/**
 * {@link IUpdateTimestampedEntity} 를 구현한 엔티티의 updateTimestamp 값을 엔티티 저장 시에 갱신해 주는 Interceptor 입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 11. 21.
 */
@Slf4j
public class UpdateTimestampedInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 7231248402093351095L;

    public UpdateTimestampedInterceptor() {
        super();
        if (log.isDebugEnabled())
            log.debug("UpdateTimestampedInterceptor 생성");
    }

    public void preFlush(Iterator entities) {
        while (entities.hasNext()) {
            Object entity = entities.next();
            if (entity instanceof IUpdateTimestampedEntity) {
                ((IUpdateTimestampedEntity) entity).updateUpdateTimestamp();

                if (log.isDebugEnabled())
                    log.debug("updateTimestamp 값을 현재 시각으로 갱신했습니다. entity=[{}]", entity);
            }
        }
    }
}
