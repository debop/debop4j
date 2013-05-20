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

package kr.debop4j.timeperiod.calendars;

import kr.debop4j.core.Tuple2;
import kr.debop4j.timeperiod.ITimePeriodCollection;
import kr.debop4j.timeperiod.SeekBoundaryMode;
import kr.debop4j.timeperiod.SeekDirection;
import kr.debop4j.timeperiod.TimePeriodCollection;
import kr.debop4j.timeperiod.tools.Durations;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 특정 시각과 기간(Duration)을 이용하여 상대 시각을 구합니다. (중간에 제외할 요일이나 일자를 고려합니다)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 19. 오후 11:39
 */
public class DateAdd implements Serializable {

    private static final long serialVersionUID = 2352433294158169198L;
    private static final Logger log = LoggerFactory.getLogger(DateAdd.class);
    private static final boolean isTraceEnable = log.isTraceEnabled();
    private static final boolean isDebugEnable = log.isDebugEnabled();

    @Getter private final ITimePeriodCollection includePeriods = new TimePeriodCollection();
    @Getter private final ITimePeriodCollection excludePeriods = new TimePeriodCollection();

    /** 기본 생성자 */
    public DateAdd() { }


    /** start 시각으로부터 offset 기간이 지난 시각을 계산합니다. */
    public DateTime add(DateTime start, Duration offset) {
        return add(start, offset, SeekBoundaryMode.Next);
    }

    /** start 시각으로부터 offset 기간이 지난 시각을 계산합니다. */
    public DateTime add(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("Add. start=[{}] + offset=[{}]의 시각을 계산합니다. seekBoundaryMode=[{}]", start, offset, seekBoundary);

        if (getIncludePeriods().size() == 0 && getExcludePeriods().size() == 0)
            return start.plus(offset);


        Tuple2<DateTime, Duration> results = offset.compareTo(Duration.ZERO) < 0
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Backward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Forward, seekBoundary);

        DateTime end = (results != null) ? results.getV1() : null;
        Duration remaining = (results != null) ? results.getV2() : null;

        if (isDebugEnable)
            log.debug("Add. start=[{}] + offset=[{}] 의 결과 end=[{}], remaining=[{}]입니다. seekBoundaryMode=[{}]",
                      start, offset, end, remaining, seekBoundary);

        return end;
    }

    /** start 시각으로부터 offset 기간을 뺀 (즉 이전의) 시각을 계산합니다. */
    public DateTime subtract(DateTime start, Duration offset) {
        return subtract(start, offset, SeekBoundaryMode.Next);
    }

    /** start 시각으로부터 offset 기간을 뺀 (즉 이전의) 시각을 계산합니다. */
    public DateTime subtract(DateTime start, Duration offset, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("Subtract. start=[{}] - offset=[{}]의 시각을 계산합니다. seekBoundaryMode=[{}]", start, offset, seekBoundary);

        Tuple2<DateTime, Duration> results = offset.compareTo(Duration.ZERO) < 0
                ? calculateEnd(start, Durations.negate(offset), SeekDirection.Forward, seekBoundary)
                : calculateEnd(start, offset, SeekDirection.Backward, seekBoundary);

        DateTime end = (results != null) ? results.getV1() : null;
        Duration remaining = (results != null) ? results.getV2() : null;

        if (isDebugEnable)
            log.debug("Subtract. start=[{}] - offset=[{}] 의 결과 end=[{}], remaining=[{}]입니다. seekBoundaryMode=[{}]",
                      start, offset, end, remaining, seekBoundary);

        return end;
    }


    /**
     * start 시각으로부터 offset 만큼 떨어진 시각을 구합니다.
     *
     * @param start        시작 시각
     * @param offset       기간
     * @param seekDir      탐색 방향
     * @param seekBoundary 경계 값 포함 여부
     * @return 계산된 시각, 짜투리 시
     */
    protected Tuple2<DateTime, Duration> calculateEnd(DateTime start, Duration offset, SeekDirection seekDir, SeekBoundaryMode seekBoundary) {
        if (isTraceEnable)
            log.trace("기준시각으로부터 오프셋만큼 떨어진 시각을 구합니다... start=[{}], offset=[{}], seekDir=[{}], seekBoundary=[{}]",
                      start, offset, seekDir, seekBoundary);

        // TODO: 구현 중


        return new Tuple2<DateTime, Duration>(null, null);
    }

}
