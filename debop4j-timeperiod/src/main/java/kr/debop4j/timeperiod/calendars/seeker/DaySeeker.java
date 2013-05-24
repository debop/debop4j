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

package kr.debop4j.timeperiod.calendars.seeker;

import kr.debop4j.timeperiod.*;
import kr.debop4j.timeperiod.calendars.CalendarVisitor;
import kr.debop4j.timeperiod.calendars.CalendarVisitorFilter;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.MonthRange;
import kr.debop4j.timeperiod.timerange.YearRange;
import kr.debop4j.timeperiod.timerange.YearRangeCollection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static kr.debop4j.core.Guard.shouldNotBeNull;

/**
 * 일(Day) 단위로 탐색하는 탐색기입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 20. 오후 8:46
 */
@Slf4j
public class DaySeeker extends CalendarVisitor<CalendarVisitorFilter, DaySeekerContext> {

    @Getter private final ITimePeriodCollection periods = new TimePeriodCollection();

    // region << Constructor >>

    public DaySeeker() {
        this(SeekDirection.Forward, TimeCalendar.getDefault());
    }

    public DaySeeker(CalendarVisitorFilter filter) {
        this(filter, null, TimeCalendar.getDefault());
    }

    public DaySeeker(SeekDirection seekDir) {
        this(new CalendarVisitorFilter(), seekDir, TimeCalendar.getDefault());
    }

    public DaySeeker(SeekDirection seekDir, ITimeCalendar calendar) {
        this(new CalendarVisitorFilter(), seekDir, calendar);
    }

    public DaySeeker(CalendarVisitorFilter filter, SeekDirection seekDir, ITimeCalendar calendar) {
        super(filter, TimeRange.Anytime, seekDir, calendar);
    }

    // endregion

    /**
     * 기준일로부터 offset 만큼의 일수가 지난 후의 일자를 구합니다. (제외되는 날을 고려)
     *
     * @param startDay 기준일
     * @param dayCount 이후 일수
     * @return 기준일로부터 dayCount 만큼 떨어진 일자
     */
    public DayRange findDay(DayRange startDay, int dayCount) {
        if (log.isTraceEnabled())
            log.trace("Day 찾기... startDay=[{}], dayCount=[{}]", startDay, dayCount);

        if (dayCount == 0) return startDay;

        DaySeekerContext context = new DaySeekerContext(startDay, dayCount);
        SeekDirection visitDir = getSeekDirection();

        if (dayCount < 0)
            visitDir = (visitDir == SeekDirection.Forward) ? SeekDirection.Backward : SeekDirection.Forward;

        startDayVisit(startDay, context, visitDir);

        if (log.isTraceEnabled())
            log.trace("Day 찾기... startDay=[{}], dayCount=[{}], foundDay=[{}]", startDay, dayCount, context.getFoundDay());

        return context.getFoundDay();
    }

    @Override
    protected boolean enterYears(YearRangeCollection yearRangeCollection, DaySeekerContext context) {
        return !context.isFinished();
    }

    @Override
    protected boolean enterMonths(YearRange yearRange, DaySeekerContext context) {
        return !context.isFinished();
    }

    @Override
    protected boolean enterDays(MonthRange month, DaySeekerContext context) {
        return !context.isFinished();
    }

    @Override
    protected boolean enterHours(DayRange day, DaySeekerContext context) {
        // Day 단위로 탐색을 수행하므로, 시간 단위는 필요없습니다.
        return false;
    }

    @Override
    protected boolean onVisitDay(DayRange day, DaySeekerContext context) {
        shouldNotBeNull(day, "day");

        if (context.isFinished()) return false;

        if (day.isSamePeriod(context.getStartDay())) return true;

        if (!isMatchingDay(day, context)) return true;

        if (!checkLimits(day)) return true;

        context.processDay(day);

        // context가 찾기를 완료하면, 탐색(visit) 중단하도록 합니다.
        return !context.isFinished();
    }
}
