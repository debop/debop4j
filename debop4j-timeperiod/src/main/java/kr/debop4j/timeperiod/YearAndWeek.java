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
import lombok.Getter;
import lombok.Setter;

/**
 * kr.debop4j.timeperiod.YearAndWeek
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 2:05
 */
@Getter
@Setter
public class YearAndWeek extends ValueObjectBase implements Comparable<YearAndWeek> {
    private static final long serialVersionUID = 7426266466794583765L;

    private Integer year;
    private Integer weekOfYear;

    public YearAndWeek() { }

    public YearAndWeek(Integer year, Integer weekOfYear) {
        this.year = year;
        this.weekOfYear = weekOfYear;
    }

    @Override
    public int compareTo(YearAndWeek o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        int y = (year != null) ? year : 0;
        int w = (weekOfYear != null) ? weekOfYear : 0;
        return y * 100 + w;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("year", year)
                .add("weekOfYear", weekOfYear);
    }
}
