package kr.debop4j.core.reflect;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * {@link DynamicAccessor} 의 생성자입니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 1. 21
 */
public class DynamicAccessorFactory {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DynamicAccessorFactory.class);
    private static final boolean isDebugEnabled = log.isDebugEnabled();

    private static final CacheLoader<Class<?>, DynamicAccessor> loader;
    private static final LoadingCache<Class<?>, DynamicAccessor> cache;

    static {
        if (log.isInfoEnabled())
            log.debug("DynamicAccessor 캐시를 생성합니다.");

        loader = new CacheLoader<Class<?>, DynamicAccessor>() {
            @Override
            @SuppressWarnings("unchecked")
            public DynamicAccessor<?> load(Class<?> type) throws Exception {
                return new DynamicAccessor(type);
            }
        };

        cache = CacheBuilder.newBuilder().weakValues().maximumSize(2000).build(loader);

        if (log.isInfoEnabled())
            log.debug("DynamicAccessor 캐시를 생성했습니다.");
    }

    @SuppressWarnings("unchecked")
    public static <T> DynamicAccessor<T> create(Class<T> targetType) {
        try {
            return (DynamicAccessor<T>) cache.get(targetType);
        } catch (ExecutionException e) {
            if (log.isErrorEnabled())
                log.error("DynamicAccessor 를 생성하는데 실패했습니다. targetType=" + targetType.getName(), e);
            return null;
        }
    }

    public static void clear() {
        synchronized (cache) {
            cache.cleanUp();
        }
    }
}
