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

package kr.debop4j.timeperiod.test.timeranges;

import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.HalfyearRange;
import kr.debop4j.timeperiod.timerange.HalfyearRangeCollection;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static kr.debop4j.timeperiod.tools.Times.asDate;
import static kr.debop4j.timeperiod.tools.Times.getYearOf;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.HalfyearRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 4:42
 */
@Slf4j
public class HalfyearRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void yearBaseMonthTest() {

        DateTime moment = asDate(2009, 2, 15);
        int year = getYearOf(moment.getYear(), moment.getMonthOfYear());
        HalfyearRangeCollection halfyears = new HalfyearRangeCollection(moment, 3);

        assertThat(halfyears.getYearBaseMonth()).isEqualTo(1);
        assertThat(halfyears.getStart()).isEqualTo(asDate(year, 1, 1));
    }

    @Test
    public void singleHalfyearTest() {
        final int startYear = 2004;
        final Halfyear startHalfyear = Halfyear.Second;

        HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, 1);

        assertThat(halfyears.getYearBaseMonth()).isEqualTo(1);
        assertThat(halfyears.getHalfyearCount()).isEqualTo(1);
        assertThat(halfyears.getBaseYear()).isEqualTo(startYear);
        assertThat(halfyears.getStartHalfyear()).isEqualTo(startHalfyear);
        assertThat(halfyears.getEndYear()).isEqualTo(startYear);
        assertThat(halfyears.getEndHalfyear()).isEqualTo(startHalfyear);

        List<HalfyearRange> halfyearList = halfyears.getHalfyears();
        assertThat(halfyearList.size()).isEqualTo(1);
        assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
    }

    @Test
    public void firstCalendarHalfyears() {
        final int startYear = 2004;
        final Halfyear startHalfyear = Halfyear.First;
        final int halfyearCount = 3;

        HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, halfyearCount);

        assertThat(halfyears.getYearBaseMonth()).isEqualTo(1);
        assertThat(halfyears.getHalfyearCount()).isEqualTo(halfyearCount);
        assertThat(halfyears.getBaseYear()).isEqualTo(startYear);
        assertThat(halfyears.getStartHalfyear()).isEqualTo(startHalfyear);
        assertThat(halfyears.getEndYear()).isEqualTo(startYear + 1);
        assertThat(halfyears.getEndHalfyear()).isEqualTo(Halfyear.First);

        List<HalfyearRange> halfyearList = halfyears.getHalfyears();

        assertThat(halfyearList.size()).isEqualTo(halfyearCount);
        assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.First))).isTrue();
        assertThat(halfyearList.get(1).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
        assertThat(halfyearList.get(2).isSamePeriod(new HalfyearRange(2005, Halfyear.First))).isTrue();
    }

    @Test
    public void secondCalendarHalfyears() {
        final int startYear = 2004;
        final Halfyear startHalfyear = Halfyear.Second;
        final int halfyearCount = 3;

        HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, halfyearCount);

        assertThat(halfyears.getYearBaseMonth()).isEqualTo(1);
        assertThat(halfyears.getHalfyearCount()).isEqualTo(halfyearCount);
        assertThat(halfyears.getBaseYear()).isEqualTo(startYear);
        assertThat(halfyears.getStartHalfyear()).isEqualTo(startHalfyear);
        assertThat(halfyears.getEndYear()).isEqualTo(startYear + 1);
        assertThat(halfyears.getEndHalfyear()).isEqualTo(Halfyear.Second);

        List<HalfyearRange> halfyearList = halfyears.getHalfyears();

        assertThat(halfyearList.size()).isEqualTo(halfyearCount);
        assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
        assertThat(halfyearList.get(1).isSamePeriod(new HalfyearRange(2005, Halfyear.First))).isTrue();
        assertThat(halfyearList.get(2).isSamePeriod(new HalfyearRange(2005, Halfyear.Second))).isTrue();
    }

}
