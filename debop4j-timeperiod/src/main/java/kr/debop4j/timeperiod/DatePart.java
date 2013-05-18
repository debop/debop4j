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
 * DateTime 정보 중에 시간 정보를 뺀 일자 정보만을 가진다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 2:24
 */
public class Datepart extends ValueObjectBase implements Comparable<Datepart> {
    private static final long serialVersionUID = -2730296141281632596L;

    private static Datepart today() {
        return new Datepart(DateTime.now());
    }

    @Getter
    private DateTime datepart;

    public Datepart() {
        this.datepart = new DateTime(0).withTimeAtStartOfDay();
    }

    public Datepart(DateTime datetime) {
        this.datepart = datetime.withTimeAtStartOfDay();
    }

    public Datepart(int year) {
        this(year, 1, 1);
    }

    public Datepart(int year, int monthOfYear) {
        this(year, monthOfYear, 1);
    }

    public Datepart(int year, int monthOfYear, int dayOfMonth) {
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

    public DateTime getDateTime(Timepart time) {
        return datepart.plus(time.getMillis());
    }

    public DateTime getDateTime(int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return getDateTime(hourOfDay, minuteOfHour, secondOfMinute, 0);
    }

    public DateTime getDateTime(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
        return getDateTime(new Timepart(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    @Override
    public int compareTo(Datepart o) {
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
