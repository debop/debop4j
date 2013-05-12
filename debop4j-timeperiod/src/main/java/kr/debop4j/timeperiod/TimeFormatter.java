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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * {@link ITimePeriod}를 문자열로 표현하기 위한 Formatter 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 12:52
 */
@Slf4j
public class TimeFormatter implements ITimeFormatter {

    public static final String DefaultContextSeparator = ";";
    public static final String DefaultStartEndSeparator = "~";
    public static final String DefaultDurationSeparator = "|";

    @Getter( lazy = true )
    private static final TimeFormatter instance = new TimeFormatter();
    private static final Object syncLock = new Object();


    @Override
    public Locale getLocale() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getListSeparator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContextSeparator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getStartEndSeparator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDurationSeparator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDateTimeFormat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getShortDateFormat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLongTimeFormat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getShortTimeFormat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DurationFormatKind getDurationKind() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getUseDurationSeconds() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCollection(int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCollectionPeriod(int count, DateTime start, DateTime end, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDateTime(DateTime value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getShortDate(DateTime dateTime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getLongTime(DateTime dateTime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getShortTime(DateTime dateTime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDuration(Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDuration(Duration Duration, DurationFormatKind durationFormatKind) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDuration(int years, int months, int days, int hours, int minutes, int seconds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPeriod(DateTime start, DateTime end) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPeriod(DateTime start, DateTime end, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getInterval(DateTime start, DateTime end, IntervalEdge startEdge, IntervalEdge endEdge, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCalendarPeriod(String start, String end, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCalendarPeriod(String context, String start, String end, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCalendarPeriod(String startContext, String endContext, String start, String end, Duration duration) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
