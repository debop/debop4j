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

import kr.debop4j.timeperiod.Quarter;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.QuarterRange;
import kr.debop4j.timeperiod.timerange.QuarterRangeCollection;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static kr.debop4j.timeperiod.test.tools.Times.asDate;
import static kr.debop4j.timeperiod.test.tools.Times.getYearOf;
import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.QuarterRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 11:18
 */
@Slf4j
public class QuarterRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void yearBaseMonthTest() {

        DateTime moment = asDate(2009, 2, 15);
        int year = getYearOf(moment.getYear(), moment.getMonthOfYear());
        QuarterRangeCollection quarterRanges = new QuarterRangeCollection(moment, 3);

        assertThat(quarterRanges.getYearBaseMonth()).isEqualTo(1);
        assertThat(quarterRanges.getStart()).isEqualTo(asDate(year, 1, 1));
    }

    @Test
    public void singleQuarterTest() {
        final int startYear = 2004;
        final Quarter startQuarter = Quarter.Second;

        QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, 1);

        assertThat(quarterRanges.getYearBaseMonth()).isEqualTo(1);
        assertThat(quarterRanges.getQuarterCount()).isEqualTo(1);
        assertThat(quarterRanges.getBaseYear()).isEqualTo(startYear);
        assertThat(quarterRanges.getStartQuarter()).isEqualTo(startQuarter);
        assertThat(quarterRanges.getEndYear()).isEqualTo(startYear);
        assertThat(quarterRanges.getEndQuarter()).isEqualTo(startQuarter);

        List<QuarterRange> quarters = quarterRanges.getQuarters();
        assertThat(quarters.size()).isEqualTo(1);
        assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
    }

    @Test
    public void firstCalendarHalfyears() {

        final int startYear = 2004;
        final Quarter startQuarter = Quarter.First;
        final int quarterCount = 5;

        QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, quarterCount);

        assertThat(quarterRanges.getYearBaseMonth()).isEqualTo(1);
        assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
        assertThat(quarterRanges.getBaseYear()).isEqualTo(startYear);
        assertThat(quarterRanges.getStartQuarter()).isEqualTo(startQuarter);
        assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
        assertThat(quarterRanges.getEndQuarter()).isEqualTo(Quarter.First);

        List<QuarterRange> quarters = quarterRanges.getQuarters();

        assertThat(quarters.size()).isEqualTo(quarterCount);
        assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.First))).isTrue();
        assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
        assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.Third))).isTrue();
        assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2004, Quarter.Fourth))).isTrue();
        assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.First))).isTrue();
    }

    @Test
    public void secondCalendarHalfyears() {
        final int startYear = 2004;
        final Quarter startQuarter = Quarter.Second;
        final int quarterCount = 5;

        QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, quarterCount);

        assertThat(quarterRanges.getYearBaseMonth()).isEqualTo(1);
        assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
        assertThat(quarterRanges.getBaseYear()).isEqualTo(startYear);
        assertThat(quarterRanges.getStartQuarter()).isEqualTo(startQuarter);
        assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
        assertThat(quarterRanges.getEndQuarter()).isEqualTo(Quarter.Second);

        List<QuarterRange> quarters = quarterRanges.getQuarters();

        assertThat(quarters.size()).isEqualTo(quarterCount);
        assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
        assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.Third))).isTrue();
        assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.Fourth))).isTrue();
        assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2005, Quarter.First))).isTrue();
        assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.Second))).isTrue();
    }
}
