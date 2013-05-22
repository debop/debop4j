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

package kr.debop4j.core.tools;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

/**
 * 날짜 관련 Utility Class
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 1. 27.
 */
@Slf4j
public abstract class DateTool {

    private DateTool() {}

    /** 지정된 DateTime 에서 moment-part를 제외한 date-part만을 제공합니다. */
    public static DateTime getStartOfDay(DateTime moment) {
        return moment.withTimeAtStartOfDay();
    }

    public static DateTime getEndOfDay(DateTime moment) {
        return getStartOfDay(moment).minus(1);
    }

    public static DateTime getStartOfWeek(DateTime moment) {
        int add = DateTimeConstants.MONDAY - moment.getDayOfWeek();
        if (add > 0)
            add -= 7;
        return moment.withTimeAtStartOfDay().plusDays(add);
    }

    public static DateTime getEndOfWeek(DateTime moment) {
        return getStartOfWeek(moment).plusDays(DateTimeConstants.DAYS_PER_WEEK).minus(1);
    }

    public static DateTime getStartOfMonth(DateTime moment) {
        return new DateTime().withDate(moment.getYear(), moment.getMonthOfYear(), 1);
    }

    public static DateTime getEndOfMonth(DateTime moment) {
        return getStartOfMonth(moment).plusMonths(1).minus(1);
    }

    public static DateTime getStartOfYear(int year) {
        return new DateTime(year, 1, 1, 0, 0);
    }

    public static DateTime getEndOfYear(int year) {
        return getStartOfYear(year + 1).minus(1);
    }
}
