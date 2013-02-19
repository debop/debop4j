package kr.debop4j.data.hibernate.interceptor;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.ValueObjectBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 엔티티의 변화에 대해 로그를 기록한 후 이를 DB에 저장하는
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 16.
 */
@Slf4j
public class LoggingInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = -5900714659767205225L;

    private List<Log> logs = Lists.newArrayList();
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!(entity instanceof Log)) {
            if (isDebugEnabled)
                logs.add(new Log("insert", (String) id, entity.getClass().getName()));
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (!(entity instanceof Log)) {
            if (isDebugEnabled)
                logs.add(new Log("update", (String) id, entity.getClass().getName()));
        }
        return false;
    }

    @Override
    public void postFlush(Iterator entities) {
        // 로그 정보를 기타 DB에 따로 저장할 수 있습니다^^
        //
        if (isDebugEnabled) {
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
