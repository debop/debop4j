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

package kr.debop4j.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;

/**
 * kr.debop4j.timeperiod.DatePart
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 2:24
 */
public class DatePart extends ValueObjectBase implements Comparable<DatePart> {
    private static final long serialVersionUID = -2730296141281632596L;

    private static DatePart today() {
        return new DatePart(DateTime.now());
    }

    @Getter
    private DateTime datepart;

    public DatePart() {}

    public DatePart(DateTime datetime) {
        this.datepart = datetime.withTimeAtStartOfDay();
    }

    public DatePart(int year) {
        this(year, 1, 1);
    }

    public DatePart(int year, int monthOfYear) {
        this(year, monthOfYear, 1);
    }

    public DatePart(int year, int monthOfYear, int dayOfMonth) {
        this.datepart = new DateTime().withDate(year, monthOfYear, dayOfMonth);
    }

    public int getYear() {
        return datepart.getYear();
    }

    public int getMonthOfYear() {
        return datepart.getMonthOfYear();
    }

    public int getDayOfMonth() {
        return datepart.getDayOfMonth();
    }

    public DateTime getDateTime(TimePart time) {
        return datepart.plus(time.getMillis());
    }

    public DateTime getDateTime(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return getDateTime(hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    public DateTime getDateTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        return getDateTime(new TimePart(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    @Override
    public int compareTo(DatePart o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        return (int) (datepart.getMillis() / TimeSpec.MillisPerDay);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("datepart", datepart);
    }
}
