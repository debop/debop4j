package kr.debop4j.data.hibernate.interceptor;

import kr.debop4j.data.model.IUpdateTimestampedEntity;
import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * {@link IUpdateTimestampedEntity} 를 구현한 엔티티의 updateTimestamp 값을 엔티티 저장 시에 갱신해 주는 Interceptor 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 21.
 */
public class UpdateTimestampedInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UpdateTimestampedInterceptor.class);
    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final long serialVersionUID = 7231248402093351095L;

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
