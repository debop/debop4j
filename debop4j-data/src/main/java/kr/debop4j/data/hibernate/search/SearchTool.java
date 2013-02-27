package kr.debop4j.data.hibernate.search;

import kr.debop4j.core.Guard;
import kr.debop4j.data.hibernate.tools.HibernateTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.event.spi.EventType;
import org.hibernate.search.event.impl.FullTextIndexEventListener;

/**
 * kr.debop4j.data.hibernate.search.SearchTool
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 2. 27.
 */
@Slf4j
public class SearchTool {

    private SearchTool() {}

    /**
     * Hibernate-Search의 FullTextIndexEventListener를 SessionFactory에 등록합니다.
     *
     * @param sessionFactory
     */
    public static void registerFullTextIndexEventListener(SessionFactory sessionFactory, FullTextIndexEventListener listener) {

        Guard.shouldNotBeNull(sessionFactory, "sessionFactory");
        if (log.isDebugEnabled())
            log.debug("sessionFactory에 FullTestIndexEventListener를 등록합니다...");

        HibernateTool.registerEventListener(sessionFactory, listener,
                                            EventType.POST_UPDATE,
                                            EventType.POST_INSERT,
                                            EventType.POST_DELETE,
                                            EventType.FLUSH);
    }
}
