package kr.debop4j.search.hibernate;

import kr.debop4j.data.hibernate.tools.HibernateTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.search.event.impl.FullTextIndexEventListener;

/**
 * kr.debop4j.search.hibernate.SearchTool
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 28.
 */
@Slf4j
public class SearchTool {

    private SearchTool() {}

    /**
     * Hibernate-Search의 FullTextIndexEventListener를 SessionFactory에 등록합니다.
     */
    public static void registerFullTextIndexEventListener(SessionFactory sessionFactory, FullTextIndexEventListener listener) {
        assert sessionFactory != null;
        log.info("sessionFactory에 FullTestIndexEventListener를 등록합니다...");

        try {
            HibernateTool.registerEventListener(sessionFactory, listener,
                                                EventType.POST_UPDATE,
                                                EventType.POST_INSERT,
                                                EventType.POST_DELETE,
                                                EventType.FLUSH);
        } catch (Throwable t) {
            log.warn("listener를 등록하는데 실패했습니다.", t);
            throw new RuntimeException(t);
        }
    }
}
