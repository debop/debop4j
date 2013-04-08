package kr.debop4j.core.pool;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import kr.debop4j.core.Action1;
import kr.debop4j.core.unitTesting.TestTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.Properties;

/**
 * kr.debop4j.core.pool.PollTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 8. 오전 10:21
 */
@Slf4j
public class PollTest {

    public Properties newProperties() {
        Properties props = new Properties();
        props.setProperty("pool.name", "배성혁");
        props.setProperty("pool.intValue", "100");
        props.setProperty("pool.uriValue", "http://localhost");

        return props;
    }

    @Test
    public void returnObjectTest() throws Exception {
        Properties props = newProperties();
        Pool pool = new Pool(new PoolConfig(), newProperties());

        PoolObject po = pool.getResource();
        po.setName("newValue");
        Assert.assertNotNull(po);
        Assert.assertEquals("newValue", po.getName());

        pool.returnResource(po);
        pool.destroy();

        Thread.sleep(1);

        pool = new Pool(new PoolConfig(), newProperties());
        po = pool.getResource();
        Assert.assertNotNull(po);
        Assert.assertTrue(po.getIsActive());
        Assert.assertEquals(props.getProperty("pool.name"), po.getName());
        pool.returnResource(po);

        pool.destroy();
    }

    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();


    @Test
    public void multiTest() {
        Properties props = newProperties();
        final Pool pool = new Pool(new PoolConfig(), props);
        final String name = props.getProperty("pool.name");

        TestTool.runTasks(100, new Action1<Integer>() {
            @Override
            public void perform(Integer i) {
                PoolObject po = pool.getResource();
                Assert.assertNotNull(po);
                Assert.assertTrue(po.getIsActive());
                Assert.assertEquals(name, po.getName());

                if (i % 5 == 0) {
                    po.setName("NewValue-" + i.toString());
                    pool.returnBrokenResource(po);
                } else {
                    pool.returnResource(po);
                }
            }
        });
    }
}
