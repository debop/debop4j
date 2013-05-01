package kr.debop4j.data.ehcache.ogm.dialect.ehcache;

import kr.debop4j.core.Action1;
import kr.debop4j.core.parallelism.Parallels;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider;
import org.hibernate.ogm.dialect.ehcache.EhcacheDialect;
import org.hibernate.ogm.grid.RowKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * kr.debop4j.data.ehcache.ogm.dialect.ehcache.EhcacheDialectTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 29. 오후 9:50
 */
@Slf4j
public class EhcacheDialectTest {

    private static final int LOOPS = 250;
    private static final int THREADS = 10;

    private EhcacheDialect dialect;

    @Before
    public void setup() {
        final EhcacheDatastoreProvider datastoreProvider = new EhcacheDatastoreProvider();
        datastoreProvider.configure(new HashMap());
        datastoreProvider.start();
        dialect = new EhcacheDialect(datastoreProvider);
    }

    @Test
    public void isThreadSafe() throws InterruptedException {

        final RowKey test = new RowKey("test", null, null);
        //Thread[] threads = new Thread[THREADS];

        Parallels.run(THREADS, new Action1<Integer>() {
            @Override
            public void perform(Integer arg) {
                if (log.isTraceEnabled())
                    log.trace("perform [{}]", arg);
                final IdentifierGeneratorHelper.BigIntegerHolder value =
                        new IdentifierGeneratorHelper.BigIntegerHolder();
                for (int i = 0; i < LOOPS; i++) {
                    dialect.nextValue(test, value, 1, 1);
                }
            }
        });

        final IdentifierGeneratorHelper.BigIntegerHolder value = new IdentifierGeneratorHelper.BigIntegerHolder();
        dialect.nextValue(test, value, 0, 1);

        Assert.assertEquals(LOOPS * THREADS, value.makeValue().intValue());
    }
}
