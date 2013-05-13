/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.timeperiod.timerange;

import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 시간(Hour) 단위로 기간을 표현합니다. (예: 5시 ~ 10시)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오전 10:57
 */
public class HourTimeRange extends CalendarTimeRange {

    private static final long serialVersionUID = -6167012104632243454L;

    // region << Constructor >>

    public HourTimeRange(DateTime moment, int hourCount) {
        this(moment, hourCount, new TimeCalendar());
    }

    public HourTimeRange(DateTime moment, int hourCount, ITimeCalendar calendar) {
        this(calendar.getYear(moment), calendar.getMonth(moment), calendar.getDayOfMonth(moment), calendar.getHour(moment), hourCount, calendar);
    }

    public HourTimeRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int hourCount) {
        this(year, monthOfYear, dayOfMonth, hourOfDay, hourCount, new TimeCalendar());
    }

    public HourTimeRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int hourCount, ITimeCalendar calendar) {
        super(getPeriodOf(year, monthOfYear, dayOfMonth, hourOfDay, hourCount), calendar);

        this.hourCount = hourCount;
        this.endHour = getStart().plusHours(hourCount).getHourOfDay();
    }

    // endregion

    @Getter private final int hourCount;

    @Getter private final int endHour;

    /** 시작 시각 ~ 완료 시각 사이의 모든 분단위의 기간을 컬렉션으로 반환합니다. */
    public List<MinuteRange> getMinutes() {
        DateTime start = getStart();
        ITimeCalendar timeCalendar = getTimeCalendar();
        List<MinuteRange> minutes = Lists.newArrayListWithCapacity(hourCount * TimeSpec.MinutesPerHour);

        for (int h = 0; h < hourCount; h++) {
            for (int m = 0; m < TimeSpec.MinutesPerHour; m++) {
                minutes.add(new MinuteRange(start.plusHours(h).plusMinutes(m), timeCalendar));
            }
        }

        return minutes;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), hourCount);
    }

    private static TimeRange getPeriodOf(int year, int month, int day, int hour, int hourCount) {
        DateTime start = new DateTime(year, month, day, hour, 0, 0);
        DateTime end = start.plusHours(hourCount);

        return new TimeRange(start, end);
    }
}
