package kr.debop4j.data.ogm.test.type.descriptor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ogm.type.descriptor.CalendarTimeZoneDateTimeTypeDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.data.ogm.test.type.descriptor.CalendarTimeZoneDateTimeTypeDescriptorTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 2. 오후 5:13
 */
@Slf4j
@RunWith(value = Parameterized.class)
public class CalendarTimeZoneDateTimeTypeDescriptorTest {

    private Calendar one;

    private Calendar another;

    private boolean exceptedEquality;

    public CalendarTimeZoneDateTimeTypeDescriptorTest(Calendar one, Calendar another, boolean exceptedEquality) {
        this.one = one;
        this.another = another;
        this.exceptedEquality = exceptedEquality;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Calendar past = new GregorianCalendar();
        past.set(Calendar.DAY_OF_MONTH, 28);
        past.set(Calendar.MONTH, 12);
        past.set(Calendar.YEAR, 1976);
        past.setTimeZone(TimeZone.getTimeZone("Africa/Casablanca"));

        Calendar pastGMT = (GregorianCalendar) past.clone();
        pastGMT.setTimeZone(TimeZone.getDefault());

        Object[][] data = new Object[][] {
                { null, null, true },
                { GregorianCalendar.getInstance(), null, false },
                { null, GregorianCalendar.getInstance(), false },
                { past, past, true },
                { past, new GregorianCalendar(), false },
                { past, pastGMT, false }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testCalendarTimeZoneDateTimeObjects() {
        CalendarTimeZoneDateTimeTypeDescriptor calendarTimeZoneDateTimeTypeDescriptor =
                new CalendarTimeZoneDateTimeTypeDescriptor();

        assertThat(calendarTimeZoneDateTimeTypeDescriptor.areEqual(one, another))
                .isEqualTo(exceptedEquality);
    }
}
