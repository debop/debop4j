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

import com.google.common.collect.Maps;
import kr.debop4j.core.tools.MapperTool;
import kr.debop4j.core.tools.ReflectTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 지역화정보를 가지는 엔티티의 추상화 클래스
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 19
 */
@Slf4j
@MappedSuperclass
public abstract class LocaleEntityBase<TId extends Serializable, TLocaleValue extends ILocaleValue>
        extends EntityBase<TId> implements ILocaleEntity<TLocaleValue> {

    private static final long serialVersionUID = 8316501523660904445L;

    @Getter
    private Map<Locale, TLocaleValue> localeMap = Maps.newLinkedHashMap();

    private TLocaleValue defaultLocaleValue = null;

    /** 기본 Locale 정보를 생성하여 제공합니다. */
    public final TLocaleValue getDefaultLocale() {
        if (defaultLocaleValue == null) {
            try {
                defaultLocaleValue = createDefaultLocaleValue();
            } catch (Exception ex) {
                log.error("기본 LocaleValue를 얻는데 실패했습니다.", ex);
            }
        }
        return defaultLocaleValue;
    }

    protected TLocaleValue createDefaultLocaleValue() {
        try {
            Class<TLocaleValue> localeValueClass = ReflectTool.getGenericParameterType(this, 1);

            if (log.isDebugEnabled())
                log.debug("기본 Locale 정보가 없습니다. 엔티티 [{}] 속성으로 기본 Locale [{}] 정보를 생성합니다...",
                          getClass().getName(), localeValueClass.getName());

            // ModelMapper가 엔티티의 속성 중 ILocaleValue 속성과 같은 것들에 대해 값을 복사한다.
            //
            return MapperTool.map(this, localeValueClass);

        } catch (Throwable t) {
            throw new IllegalStateException("기본 Locale 정보를 생성하는데 실패했습니다.", t);
        }
    }

    @Override
    public TLocaleValue getLocaleValue(Locale locale) {
        return getLocaleMap().get(locale);
    }

    @Override
    public Set<Locale> getLocales() {
        return getLocaleMap().keySet();
    }

    @Override
    public void addLocaleValue(Locale locale, TLocaleValue localeValue) {
        getLocaleMap().put(locale, localeValue);
    }

    @Override
    public void removeLocaleValue(Locale locale) {
        getLocaleMap().remove(locale);
    }

    @Override
    public TLocaleValue getLocaleValueOrDefault(Locale locale) {
        if (localeMap == null || localeMap.size() == 0 || locale == null || locale.getDisplayName() == null)
            return getDefaultLocale();

        if (localeMap.containsKey(locale))
            return localeMap.get(locale);

        return getDefaultLocale();
    }

    @Override
    public TLocaleValue getCurrentLocaleValue() {
        return getLocaleValueOrDefault(Locale.getDefault());
    }
}
