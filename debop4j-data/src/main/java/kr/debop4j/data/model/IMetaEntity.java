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

import java.util.Set;

/**
 * 메타 정보({@link java.util.Map} 구조)를 가지는 엔티티의 인터페이스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 19
 */
public interface IMetaEntity extends IStatefulEntity {

    IMetaValue getMetaValue(String key);

    Set<String> getMetaKeys();

    void addMetaValue(String metaKey, IMetaValue metaValue);

    void addMetaValue(String metaKey, Object value);

    void removeMetaValue(String metaKey);
}
