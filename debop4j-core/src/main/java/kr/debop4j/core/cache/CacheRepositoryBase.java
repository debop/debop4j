package kr.debop4j.core.cache;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * Cache 시스템에 정보를 관리하는 ICacheRepository 의 기본 추상화 클래스입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
@Slf4j
public abstract class CacheRepositoryBase implements ICacheRepository {

    private long expiry = 0;

    /**
     * 캐시 항목의 유효기간을 반환합니다. (단위: minutes)
     *
     * @return 캐시항목의 기본 유효기간
     */
    @Override
    public long getExpiry() {
        return this.expiry;
    }

    /**
     * 캐시 항목의 유효기간을 설정합니다. (단위: minutes)
     *
     * @param expiry 캐시 항목의 기본 유효 기간 (단위: minutes), 0 이하인 경우는 유효기간이 없다.
     */
    @Override
    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    /**
     * 캐시에서 해당 키의 항목을 가져옵니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목, 없으면 null 반환
     */
    abstract public Object get(final String key);

    /**
     * 캐시에 항목을 저장합니다.
     *
     * @param key      캐시 키
     * @param value    캐시 항목
     * @param validFor 캐시 유효 기간 (단위 : minutes), 0 이하인 경우는 유효기간이 없다.
     */
    abstract public void set(String key, Object value, long validFor);

    /**
     * 해당 키의 캐시 항목을 삭제합니다.
     *
     * @param key 캐시 키
     */
    abstract public void remove(final String key);

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    public void removeAll(String... keys) {
        for (final String key : keys) {
            remove(key);
        }
    }

    /**
     * 여러개의 키를 모두 삭제합니다.
     *
     * @param keys 캐시 키 시퀀스
     */
    public void removeAll(Iterable<String> keys) {
        for (final String key : keys) {
            remove(key);
        }
    }

    /**
     * 해당 키의 캐시 항목의 존재 여부를 알아봅니다.
     *
     * @param key 캐시 키
     * @return 캐시 항목 존재 여부
     */
    abstract public boolean exists(final String key);

    /**
     * 캐시의 모든 항목을 삭제합니다.
     */
    abstract public void clear();

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("expiry", expiry)
                      .toString();
    }
}
