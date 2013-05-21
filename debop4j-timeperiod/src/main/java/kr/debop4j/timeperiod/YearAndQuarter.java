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
 * 년도와 분기를 표현
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 1:59
 */
@Getter
@Setter
public class YearAndQuarter extends ValueObjectBase implements Comparable<YearAndQuarter> {
    private static final long serialVersionUID = 9040933427227142560L;

    private Integer year;
    private Quarter quarter;

    public YearAndQuarter() {}

    public YearAndQuarter(Integer year, Quarter quarter) {
        this.year = year;
        this.quarter = quarter;
    }

    public YearAndQuarter(Integer year, int quarter) {
        this.year = year;
        this.quarter = Quarter.valueOf(quarter);
    }

    @Override
    public int compareTo(YearAndQuarter o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        int y = (year != null) ? year : 0;
        int q = (quarter != null) ? quarter.hashCode() : 0;

        return y * 100 + q;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("year", year)
                .add("quarter", quarter);
    }
}
