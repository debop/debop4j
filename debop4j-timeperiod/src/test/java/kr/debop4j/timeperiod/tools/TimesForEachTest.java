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

package kr.debop4j.timeperiod.tools;

import kr.debop4j.core.Function1;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.PeriodUnit;
import kr.debop4j.timeperiod.TimePeriodTestBase;
import kr.debop4j.timeperiod.TimeRange;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.tools.TimesForEachTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 24. 오전 12:22
 */
@Slf4j
public class TimesForEachTest extends TimePeriodTestBase {

    private DateTime startTime = new DateTime(2008, 4, 10, 5, 33, 24, 345);
    private DateTime endTime = new DateTime(2012, 10, 20, 13, 43, 12, 599);

    @Getter private TimeRange period = new TimeRange(startTime, endTime);

    @Test
    public void foreachYearsTest() {
        int count = 0;
        for (ITimePeriod p : Times.foreachYears(period)) {
            log.trace("year [{}] = [{}]", count++, p.getStart().getYear());
        }
        assertThat(count).isEqualTo(period.getEnd().getYear() - period.getStart().getYear() + 1);
    }

    @Test
    public void foreachMonthsTest() {
        int count = 0;
        for (ITimePeriod p : Times.foreachMonths(period)) {
            log.trace("month [{}] = [{}]", count++, p.getStart().getMonthOfYear());
        }

        int months = (int) (period.getDuration().getMillis() / (TimeSpec.MaxDaysPerMonth * TimeSpec.MillisPerDay)) + 2;
        assertThat(count).isEqualTo(months);
    }

    @Test
    public void foreachWeeksTest() {
        int count = 0;

        DateTimeFormatter shortDate = DateTimeFormat.shortDate();
        List<ITimePeriod> weeks = Times.foreachWeeks(period);
        for (ITimePeriod p : weeks) {
            log.trace("week[{}] = [{}]~[{}], WeekOfYear=({},{})",
                      count++, shortDate.print(p.getStart()), shortDate.print(p.getEnd()), p.getStart().getWeekyear(), p.getStart().getWeekOfWeekyear());
        }

        assertThat(weeks.get(0).getStart()).isEqualTo(period.getStart());
        assertThat(weeks.get(weeks.size() - 1).getEnd()).isEqualTo(period.getEnd());
    }

    @Test
    public void foreachDaysTest() {
        int count = 0;
        List<ITimePeriod> days = Times.foreachDays(period);
        for (ITimePeriod p : days)
            count++;

        assertThat(days.get(0).getStart()).isEqualTo(period.getStart());
        assertThat(days.get(days.size() - 1).getEnd()).isEqualTo(period.getEnd());

        log.trace("end-1=[{}]", days.get(days.size() - 2));
        log.trace("end  =[{}]", days.get(days.size() - 1));
    }

    @Test
    public void foreachHoursTest() {
        int count = 0;
        List<ITimePeriod> hours = Times.foreachHours(period);

        for (ITimePeriod p : hours)
            count++;

        assertThat(hours.size()).isEqualTo(count);

        assertThat(hours.get(0).getStart()).isEqualTo(period.getStart());
        assertThat(hours.get(hours.size() - 1).getEnd()).isEqualTo(period.getEnd());
        assertThat(hours.get(hours.size() - 1).getStart().getMillis()).isGreaterThan(hours.get(hours.size() - 2).getEnd().getMillis());
    }

    @Test
    public void foreachMinuteTest() {
        int count = 0;
        List<ITimePeriod> minutes = Times.foreachMinutes(period);

        for (ITimePeriod p : minutes)
            count++;

        assertThat(minutes.size()).isEqualTo(count);

        assertThat(minutes.get(0).getStart()).isEqualTo(period.getStart());
        assertThat(minutes.get(minutes.size() - 1).getEnd()).isEqualTo(period.getEnd());
        assertThat(minutes.get(minutes.size() - 1).getStart().getMillis()).isGreaterThan(minutes.get(minutes.size() - 2).getEnd().getMillis());
    }

    @Test
    public void foreachPeriodsTest() {

        for (PeriodUnit periodUnit : PeriodUnit.values()) {
            if (periodUnit == PeriodUnit.Second || periodUnit == PeriodUnit.Millisecond)
                continue;

            int count = 0;
            for (ITimePeriod p : Times.foreachPeriods(period, periodUnit)) {
                count++;
                if (count == 1000)
                    break;
            }
        }
    }

    @Test
    public void runPeriodTest() {

        for (PeriodUnit periodUnit : PeriodUnit.values()) {
            if (periodUnit == PeriodUnit.Second || periodUnit == PeriodUnit.Millisecond)
                continue;

            final int[] count = { 0 };
            List<Integer> results = Times.runPeriods(period, periodUnit, new Function1<ITimePeriod, Integer>() {
                @Override
                public Integer execute(ITimePeriod arg) {
                    return count[0]++;
                }
            });
            assertThat(results.size()).isEqualTo(count[0]);
        }
    }

    @Test
    public void runPeriodAsParallelTest() {

        for (PeriodUnit periodUnit : PeriodUnit.values()) {
            if (periodUnit == PeriodUnit.Second || periodUnit == PeriodUnit.Millisecond)
                continue;

            final AtomicInteger count = new AtomicInteger(0);
            List<Integer> results = Times.runPeriodsAsParallel(period, periodUnit, new Function1<ITimePeriod, Integer>() {
                @Override
                public Integer execute(ITimePeriod arg) {
                    return count.incrementAndGet();
                }
            });

            assertThat(results.size()).isEqualTo(count.get());
        }
    }
}
