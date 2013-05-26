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
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.test.tools.TimeSpec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;

import java.util.Locale;

/**
 * {@link TimeCalendar} 의 설정 정보를 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 9:52
 */
@Slf4j
public class TimeCalendarConfig extends ValueObjectBase {

    public TimeCalendarConfig() {
        this(Locale.getDefault());
    }

    public TimeCalendarConfig(Locale locale) {
        this.locale = (locale != null) ? locale : Locale.getDefault();

        firstDayOfWeek = TimeSpec.FirstDayOfWeek;
    }

    @Getter @Setter
    private Locale locale;

    @Getter @Setter
    private Duration startOffset;

    @Getter @Setter
    private Duration endOffset;

    @Getter @Setter(AccessLevel.PROTECTED)
    private DayOfWeek firstDayOfWeek;

    @Override
    public int hashCode() {
        return HashTool.compute(locale, startOffset, endOffset);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("locale", locale)
                .add("startOffset", startOffset)
                .add("endOffset", endOffset);
    }

    private static final long serialVersionUID = 8222905153103967935L;
}
