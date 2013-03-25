package kr.debop4j.core.cache.mongodb;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;

/**
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25 오후 3:15
 */
@Slf4j
public class MongoCacheManager extends AbstractTransactionSupportingCacheManager {

    private MongoTemplate mongoTemplate;

    public MongoCacheManager(MongoTemplate mongoTemplate) {
        Guard.shouldNotBeNull(mongoTemplate, "mongoTemplate");
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Collection<Cache> caches = Lists.newArrayList();

        for (String name : getCacheNames()) {
            caches.add(new MongoCache(name, mongoTemplate));
        }
        return caches;
    }

    @Override
    public Cache getCache(String name) {
        synchronized (this) {
            Cache cache = super.getCache(name);
            if (cache == null) {
                cache = new MongoCache(name, mongoTemplate);
                addCache(cache);
            }
            return cache;
        }
    }
}
