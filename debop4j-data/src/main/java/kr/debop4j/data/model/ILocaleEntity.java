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

package kr.debop4j.data.model;

import java.util.Locale;
import java.util.Set;

/**
 * 지역 정보를 가지는 IEntity
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 19
 */
public interface ILocaleEntity<TLocaleValue extends ILocaleValue> extends IStatefulEntity {

    /**
     * {@link java.util.Locale} 에 해당하는 지역화 정보(@link ILocaleValue> 를 반환하는 함수
     */
    TLocaleValue getLocaleValue(Locale locale);

    /**
     * 정의된 {@link java.util.Locale} 정보
     */
    Set<Locale> getLocales();

    /**
     * 지역화 정보를 추가합니다.
     */
    void addLocaleValue(Locale locale, TLocaleValue localeValue);

    /**
     * 지역화 정보를 삭제합니다.
     */
    void removeLocaleValue(Locale locale);

    /**
     * 지정된 {@link java.util.Locale}에 해당하는 TLocaleValue 이 존재하면 반환하고, 없으면 기본 Locale에 해당하는 정보를 반환한다.
     */
    TLocaleValue getLocaleValueOrDefault(Locale locale);

    /**
     * 현 Thread Context의 {@link java.util.Locale} 에 해당하는 {TLocaleValue} 를 반환한다.
     */
    TLocaleValue getCurrentLocaleValue();
}
