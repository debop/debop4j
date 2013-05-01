package kr.debop4j.data.ogm.test.massindex.model;

import kr.debop4j.data.ogm.test.id.NewsID;
import org.hibernate.search.bridge.TwoWayStringBridge;

/**
 * kr.debop4j.data.ogm.test.massindex.model.NewsIdFieldBridge
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 1:16
 */
public class NewsIdFieldBridge implements TwoWayStringBridge {

    private static final String SEP = "::::";

    @Override
    public Object stringToObject(String stringValue) {
        String[] split = stringValue.split(SEP);
        return new NewsID(split[0], split[1]);
    }

    @Override
    public String objectToString(Object object) {
        NewsID newsId = (NewsID) object;
        return newsId.getTitle() + SEP + newsId.getAuthor();
    }
}
