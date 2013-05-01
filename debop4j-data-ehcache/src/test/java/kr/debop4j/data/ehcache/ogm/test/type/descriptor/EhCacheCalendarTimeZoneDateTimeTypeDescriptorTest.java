package kr.debop4j.data.ehcache.ogm.test.type.descriptor;

import kr.debop4j.data.ogm.test.type.descriptor.CalendarTimeZoneDateTimeTypeDescriptorTest;

import java.util.Calendar;

/**
 * kr.debop4j.data.ehcache.ogm.test.type.descriptor.EhCacheCalendarTimeZoneDateTimeTypeDescriptorTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 10. 오후 9:24
 */
public class EhCacheCalendarTimeZoneDateTimeTypeDescriptorTest extends CalendarTimeZoneDateTimeTypeDescriptorTest {

    public EhCacheCalendarTimeZoneDateTimeTypeDescriptorTest(Calendar one, Calendar another, boolean exceptedEquality) {
        super(one, another, exceptedEquality);
    }
}
