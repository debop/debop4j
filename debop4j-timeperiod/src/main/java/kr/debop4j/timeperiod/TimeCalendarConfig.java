package kr.debop4j.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * {@link TimeCalendar} 의 설정 정보를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 9:52
 */
@Slf4j
@Getter
@Setter
public class TimeCalendarConfig extends ValueObjectBase {

    private static final long serialVersionUID = 8222905153103967935L;

    public TimeCalendarConfig(Locale locale) {
        this.locale = locale;
    }

    private Locale locale;

    private Duration startOffset;

    private Duration endOffset;

    @Setter( AccessLevel.PROTECTED )
    private DayOfWeek firstDayOfWeek;


    @Override
    public int hashCode() {
        return HashTool.compute(locale, startOffset, endOffset);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("locale", locale)
                .add("startOffset", startOffset)
                .add("endOffset", endOffset);
    }
}
