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
 * 년, 반기 정보를 표현하는 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 1:46
 */
@Getter
@Setter
public class YearAndHalfyear extends ValueObjectBase implements Comparable<YearAndHalfyear> {
    private static final long serialVersionUID = 1419206569889564461L;

    private Integer year;
    private Halfyear halfyear;

    public YearAndHalfyear(Integer year, Halfyear halfyear) {
        this.year = year;
        this.halfyear = halfyear;
    }

    public YearAndHalfyear(Integer year, Integer halfyear) {
        this.year = year;
        if (halfyear != null)
            this.halfyear = (halfyear == 1) ? Halfyear.First : Halfyear.Second;
    }

    @Override
    public int compareTo(YearAndHalfyear o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public int hashCode() {
        int y = (year != null) ? year : 0;
        int hy = (halfyear != null) ? halfyear.getValue() : 0;
        return y * 100 + hy;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("year", year)
                .add("halfyear", halfyear);
    }
}
