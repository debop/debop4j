package kr.debop4j.core.cache.mongodb;

import kr.debop4j.core.Guard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;

/**
 * MongoDB를 저장소로 사용하는 캐시
 *
 * @author sunghyouk.bae@gmail.com
 *         13. 3. 25 오후 3:14
 */
@Slf4j
public class MongoCache implements Cache {

    private String name;
    private MongoTemplate mongoTemplate;

    public MongoCache(String name, MongoTemplate mongoTemplate) {
        Guard.shouldNotBeEmpty(name, "name");
        Guard.shouldNotBeNull(mongoTemplate, "mongoTemplate");

        this.name = name;
        this.mongoTemplate = mongoTemplate;

        if (log.isDebugEnabled())
            log.debug("MongoCache를 생성합니다. name=[{}], mongodb=[{}]", name, mongoTemplate.getDb().getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return mongoTemplate;
    }

    @Override
    public ValueWrapper get(Object key) {
        Guard.shouldNotBeNull(key, "key");

        CacheItem item = mongoTemplate.findOne(new Query(Criteria.where("key").is(key)), CacheItem.class, name);

        Object result = null;
        if (item != null) {
            result = item.getValue();
            if (log.isDebugEnabled())
                log.debug("캐시 값을 로드했습니다. key=[{}], value=[{}]", key, result);
        }

        SimpleValueWrapper wrapper = null;
        if (result != null)
            wrapper = new SimpleValueWrapper(result);
        return wrapper;
    }

    @Override
    public void put(Object key, Object value) {
        Guard.shouldNotBeNull(key, "key");

        if (!mongoTemplate.collectionExists(name))
            mongoTemplate.createCollection(name);

        if (log.isDebugEnabled())
            log.debug("캐시에 값을 저장합니다. key=[{}], value=[{}]", key, value);

        if (get(key) == null)
            mongoTemplate.insert(new CacheItem(key, value), name);
        else
            mongoTemplate.upsert(new Query(Criteria.where("key").is(key)),
                                 Update.update("value", value),
                                 name);
    }

    @Override
    public void evict(Object key) {
        Guard.shouldNotBeNull(key, "key");
        mongoTemplate.remove(new Query(Criteria.where("key").is(key)), name);
    }

    @Override
    public void clear() {
        mongoTemplate.dropCollection(name);
    }

    @Getter
    @Setter
    public static class CacheItem implements Serializable {

        private Object key;
        private Object value;

        public CacheItem() { this(null, null); }

        public CacheItem(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "CacheItem@{key=" + key + ", value=" + value + "}";
        }
    }
}
