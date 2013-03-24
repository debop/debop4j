package kr.debop4j.core.cache;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.Arrays;
import java.util.Map;

import static kr.debop4j.core.Guard.shouldNotBeNull;
import static kr.debop4j.core.Guard.shouldNotBeWhiteSpace;

/**
 * EhCache 용 Cache Repository
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12.
 */
@Slf4j
public class EhCacheRepository extends CacheRepositoryBase {

    @Getter
    private final Ehcache ehcache;

    public EhCacheRepository(Ehcache ehcache) {
        if (log.isDebugEnabled())
            log.debug("EhCacheRepository 인스턴스를 생성합니다. ehcache=[{}]", ehcache);
        this.ehcache = shouldNotBeNull(ehcache, "ehcache");
    }

    @Override
    public Object get(final String key) {
        shouldNotBeWhiteSpace(key, "key");

        Element element = ehcache.get(key);
        return (element != null)
                ? element.getObjectValue()
                : null;
    }

    public Map gets(String... keys) {
        return ehcache.getAll(Arrays.asList(keys));
    }

    @Override
    public void set(final String key, Object value, long validFor) {
        shouldNotBeWhiteSpace(key, "key");
        ehcache.put(new Element(key, value, validFor));
    }

    @Override
    public void remove(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        ehcache.remove(key);
    }

    @Override
    public void removeAll(String... keys) {
        ehcache.removeAll(Arrays.asList(keys));
    }

    @Override
    public void removeAll(Iterable<String> keys) {
        ehcache.removeAll(Lists.newArrayList(keys));
    }

    @Override
    public boolean exists(final String key) {
        shouldNotBeWhiteSpace(key, "key");
        return ehcache.get(key) != null;
    }

    @Override
    public void clear() {
        ehcache.removeAll();
    }
}
