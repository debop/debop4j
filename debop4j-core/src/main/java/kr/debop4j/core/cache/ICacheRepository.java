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

package kr.debop4j.core.cache;

import java.util.concurrent.ExecutionException;

/**
 * Cache 시스템에 정보를 관리하는 ICacheRepository 의 인터페이스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public interface ICacheRepository {

    /**
     * 캐시 항목의 유효기간을 반환합니다 (단위 : minutes)
     *
     * @return 캐시항목의 기본 유효기간
     */
    long getExpiry();

    /**
     * 캐시 항목의 유효기간을 설정합니다. (단위 : minutes)
     *
     * @param expiry 캐시 항목의 기본 유효 기간 (단위 minutes), 0 이하인 경우는 유효기간이 없다.
     */
    void setExpiry(long expiry);

    /**
     * 캐시에서 해당 키의 항목을 가져옵니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목, 없으면 null 반환
     */
    Object get(final String key) throws ExecutionException;

    /**
     * 캐시에 항목을 저장합니다.
     *
     * @param key      캐시 키
     * @param value    캐시 항목
     * @param validFor 캐시 유효 기간 (단위 : minutes), 0 이하인 경우는 유효기간이 없다.
     */
    void set(final String key, final Object value, long validFor);

    /**
     * 해당 키의 캐시 항목을 삭제합니다.
     *
     * @param key 캐시 키
     */
    void remove(final String key);

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 삭제할 키들
     */
    void removeAll(final String... keys);

    /**
     * 여러 개의 키를 한번에 삭제합니다.
     *
     * @param keys 삭제할 키들
     */
    void removeAll(final Iterable<String> keys);

    /**
     * 해당 키의 캐시 항목의 존재 여부를 알아봅니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목 존재 여부
     */
    boolean exists(final String key);

    /** 캐시의 모든 항목을 삭제합니다. */
    void clear();
}
