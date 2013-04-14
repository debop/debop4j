package kr.debop4j.data.redis.spring;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;

/**
 * kr.debop4j.data.redis.spring.RedisCacheManager
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오후 2:08
 */
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
