package kr.debop4j.core.pool;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.net.URI;
import java.util.Properties;

/**
 * kr.debop4j.core.pool.Pool
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오전 10:31
 */
@Slf4j
@Getter
@Setter
public class Pool extends AbstractPool<PoolObject> {

    public Pool(final GenericObjectPool.Config poolConfig, final Properties props) {
        super(poolConfig, new PoolObjectFactory(props));
    }

    /**
     * 더 이상 재 사용할 수 없는 리소스에 대해 pooling 을 하지 않도록 합니다.
     *
     * @param resource
     */
    @Override
    public void returnBrokenResource(PoolObject resource) {
        returnBrokenResourceObject(resource);
    }

    /**
     * 재사용을 위해 리소스를 풀에 반환합니다.
     *
     * @param resource
     */
    @Override
    public void returnResource(final PoolObject resource) {
        returnResourceObject(resource);
    }

    public static class PoolObjectFactory extends BasePoolableObjectFactory<PoolObject> {

        private Properties props;

        private String name;
        private Integer intValue;
        private URI uriValue;

        public PoolObjectFactory(Properties props) {
            this.props = props;

            this.name = props.getProperty("pool.name", "name");
            this.intValue = Integer.decode(props.getProperty("pool.intValue", "1"));
            this.uriValue = URI.create(props.getProperty("pool.uriValue", "http://localhost"));
        }

        @Override
        public PoolObject makeObject() throws Exception {
            final PoolObject po = new PoolObject(name, intValue, uriValue);
            po.setIsActive(true);
            return po;
        }

        @Override
        public void destroyObject(PoolObject obj) throws Exception {
            final PoolObject po = (PoolObject) obj;
            if (po.getIsActive()) {
                po.setIsActive(false);
            }
        }

        @Override
        public boolean validateObject(PoolObject obj) {
            final PoolObject po = (PoolObject) obj;
            return po.getIsActive();
        }
    }
}
