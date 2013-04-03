package org.hibernate.cache.redis;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Redis cache parameter 환경 설정 정보를 보관하는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 3. 오후 11:17
 */
public class TypeOverrides {

    private final Set<String> overridden = new HashSet<String>();

    @Getter
    @Setter
    private String cacheName;

    @Getter
    private long expiration;

    public void setExpiration(long expiration) {
        markAsOverriden("expiration");
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getClass().getSimpleName())
                .append("{")
                .append("cache=").append(cacheName)
                .append("expiration").append(expiration)
                .append("}")
                .toString();
    }

    private String uc(String s) {
        return s == null ? null : s.toUpperCase(Locale.ENGLISH);
    }

    private void markAsOverriden(String fieldName) {
        overridden.add(fieldName);
    }
}
