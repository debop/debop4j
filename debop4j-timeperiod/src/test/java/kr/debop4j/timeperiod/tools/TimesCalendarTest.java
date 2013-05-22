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

import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.Quarter;
import kr.debop4j.timeperiod.TimePeriodTestBase;
import kr.debop4j.timeperiod.YearAndHalfyear;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.tools.TimesCalendarTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 21. 오후 7:00
 */
@Slf4j
public class TimesCalendarTest extends TimePeriodTestBase {

    @Test
    public void getYearOfTest() {
        assertThat(Times.getYearOf(new DateTime(2000, 1, 1, 0, 0))).isEqualTo(2000);
        assertThat(Times.getYearOf(new DateTime(2000, 4, 1, 0, 0))).isEqualTo(2000);
        assertThat(Times.getYearOf(2000, 12)).isEqualTo(2000);

        assertThat(Times.getYearOf(nowTime)).isEqualTo(nowTime.getYear());
    }

    @Test
    public void nextHalfyear() {
        assertThat(Times.nextHalfyear(2000, Halfyear.First)).isEqualTo(new YearAndHalfyear(2000, Halfyear.Second));
        assertThat(Times.nextHalfyear(2000, Halfyear.Second)).isEqualTo(new YearAndHalfyear(2001, Halfyear.First));
    }

    @Test
    public void prevHalfyear() {
        assertThat(Times.previousHalfyear(2000, Halfyear.First)).isEqualTo(new YearAndHalfyear(1999, Halfyear.Second));
        assertThat(Times.previousHalfyear(2000, Halfyear.Second)).isEqualTo(new YearAndHalfyear(2000, Halfyear.First));
    }

    @Test
    public void addHalfyear() {
        assertThat(Times.addHalfyear(2000, Halfyear.First, 1).getHalfyear()).isEqualTo(Halfyear.Second);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, 1).getHalfyear()).isEqualTo(Halfyear.First);
        assertThat(Times.addHalfyear(2000, Halfyear.First, -1).getHalfyear()).isEqualTo(Halfyear.Second);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, -1).getHalfyear()).isEqualTo(Halfyear.First);

        assertThat(Times.addHalfyear(2000, Halfyear.First, 2).getHalfyear()).isEqualTo(Halfyear.First);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, 2).getHalfyear()).isEqualTo(Halfyear.Second);
        assertThat(Times.addHalfyear(2000, Halfyear.First, -2).getHalfyear()).isEqualTo(Halfyear.First);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, -2).getHalfyear()).isEqualTo(Halfyear.Second);

        assertThat(Times.addHalfyear(2000, Halfyear.First, 5).getHalfyear()).isEqualTo(Halfyear.Second);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, 5).getHalfyear()).isEqualTo(Halfyear.First);
        assertThat(Times.addHalfyear(2000, Halfyear.First, -5).getHalfyear()).isEqualTo(Halfyear.Second);
        assertThat(Times.addHalfyear(2000, Halfyear.Second, -5).getHalfyear()).isEqualTo(Halfyear.First);

        assertThat(Times.addHalfyear(2008, Halfyear.First, 1).getYear()).isEqualTo(2008);
        assertThat(Times.addHalfyear(2008, Halfyear.Second, 1).getYear()).isEqualTo(2009);

        assertThat(Times.addHalfyear(2008, Halfyear.First, -1).getYear()).isEqualTo(2007);
        assertThat(Times.addHalfyear(2008, Halfyear.Second, -1).getYear()).isEqualTo(2008);

        assertThat(Times.addHalfyear(2008, Halfyear.First, 2).getYear()).isEqualTo(2009);
        assertThat(Times.addHalfyear(2008, Halfyear.Second, 2).getYear()).isEqualTo(2009);

        assertThat(Times.addHalfyear(2008, Halfyear.First, 3).getYear()).isEqualTo(2009);
        assertThat(Times.addHalfyear(2008, Halfyear.Second, 3).getYear()).isEqualTo(2010);
    }

    @Test
    public void getHalfyearOfMonthTest() {
        for (int month : TimeSpec.FirstHalfyearMonths)
            assertThat(Times.getHalfyearOfMonth(month)).isEqualTo(Halfyear.First);

        for (int month : TimeSpec.SecondHalfyearMonths)
            assertThat(Times.getHalfyearOfMonth(month)).isEqualTo(Halfyear.Second);
    }

    @Test
    public void getonthsOfHalfyear() {
        assertThat(Times.getMonthsOfHalfyear(Halfyear.First)).isEqualTo(TimeSpec.FirstHalfyearMonths);
        assertThat(Times.getMonthsOfHalfyear(Halfyear.Second)).isEqualTo(TimeSpec.SecondHalfyearMonths);
    }

    @Test
    public void nextQuarterTest() {
        assertThat(Times.nextQuarter(2000, Quarter.First).getQuarter()).isEqualTo(Quarter.Second);
        assertThat(Times.nextQuarter(2000, Quarter.Second).getQuarter()).isEqualTo(Quarter.Third);
        assertThat(Times.nextQuarter(2000, Quarter.Third).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.nextQuarter(2000, Quarter.Fourth).getQuarter()).isEqualTo(Quarter.First);
    }

    @Test
    public void previousQuarterTest() {
        assertThat(Times.previousQuarter(2000, Quarter.First).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.previousQuarter(2000, Quarter.Second).getQuarter()).isEqualTo(Quarter.First);
        assertThat(Times.previousQuarter(2000, Quarter.Third).getQuarter()).isEqualTo(Quarter.Second);
        assertThat(Times.previousQuarter(2000, Quarter.Fourth).getQuarter()).isEqualTo(Quarter.Third);
    }

    @Test
    public void addQuarterTest() {

        assertThat(Times.addQuarter(2000, Quarter.First, 1).getQuarter()).isEqualTo(Quarter.Second);
        assertThat(Times.addQuarter(2000, Quarter.Second, 1).getQuarter()).isEqualTo(Quarter.Third);
        assertThat(Times.addQuarter(2000, Quarter.Third, 1).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.addQuarter(2000, Quarter.Fourth, 1).getQuarter()).isEqualTo(Quarter.First);

        assertThat(Times.addQuarter(2000, Quarter.First, -1).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.addQuarter(2000, Quarter.Second, -1).getQuarter()).isEqualTo(Quarter.First);
        assertThat(Times.addQuarter(2000, Quarter.Third, -1).getQuarter()).isEqualTo(Quarter.Second);
        assertThat(Times.addQuarter(2000, Quarter.Fourth, -1).getQuarter()).isEqualTo(Quarter.Third);

        assertThat(Times.addQuarter(2000, Quarter.First, 2).getQuarter()).isEqualTo(Quarter.Third);
        assertThat(Times.addQuarter(2000, Quarter.Second, 2).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.addQuarter(2000, Quarter.Third, 2).getQuarter()).isEqualTo(Quarter.First);
        assertThat(Times.addQuarter(2000, Quarter.Fourth, 2).getQuarter()).isEqualTo(Quarter.Second);

        assertThat(Times.addQuarter(2000, Quarter.First, -2).getQuarter()).isEqualTo(Quarter.Third);
        assertThat(Times.addQuarter(2000, Quarter.Second, -2).getQuarter()).isEqualTo(Quarter.Fourth);
        assertThat(Times.addQuarter(2000, Quarter.Third, -2).getQuarter()).isEqualTo(Quarter.First);
        assertThat(Times.addQuarter(2000, Quarter.Fourth, -2).getQuarter()).isEqualTo(Quarter.Second);
    }

    @Test
    public void getQuarterOfMonthTest() {
        for (int m : TimeSpec.FirstQuarterMonths)
            assertThat(Times.getQuarterOfMonth(m)).isEqualTo(Quarter.First);

        for (int m : TimeSpec.SecondQuarterMonths)
            assertThat(Times.getQuarterOfMonth(m)).isEqualTo(Quarter.Second);

        for (int m : TimeSpec.ThirdQuarterMonths)
            assertThat(Times.getQuarterOfMonth(m)).isEqualTo(Quarter.Third);

        for (int m : TimeSpec.FourthQuarterMonths)
            assertThat(Times.getQuarterOfMonth(m)).isEqualTo(Quarter.Fourth);
    }

    @Test
    public void getMonthsOfQuarterTest() {
        assertThat(Times.getMonthsOfQuarter(Quarter.First)).isEqualTo(TimeSpec.FirstQuarterMonths);
        assertThat(Times.getMonthsOfQuarter(Quarter.Second)).isEqualTo(TimeSpec.SecondQuarterMonths);
        assertThat(Times.getMonthsOfQuarter(Quarter.Third)).isEqualTo(TimeSpec.ThirdQuarterMonths);
        assertThat(Times.getMonthsOfQuarter(Quarter.Fourth)).isEqualTo(TimeSpec.FourthQuarterMonths);
    }

    @Test
    public void nextMonthTest() {
        for (int i = 1; i <= TimeSpec.MonthsPerYear; i++)
            assertThat(Times.nextMonth(2000, i).getMonthOfYear()).isEqualTo(i % TimeSpec.MonthsPerYear + 1);
    }
}
