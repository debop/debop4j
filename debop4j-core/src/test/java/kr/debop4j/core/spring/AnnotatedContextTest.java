package kr.debop4j.core.spring;

import kr.debop4j.core.AbstractTest;
import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.ICompressor;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * kr.nsoft.commons.spring.AnnotatedContextTest
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 2.
 */
public class AnnotatedContextTest extends AbstractTest {

    @Override
    protected void onAfter() {
        if (Springs.isInitialized())
            Springs.reset();
    }

    @Test
    public void initByAnnotatedClasses() {
        Springs.initByAnnotatedClasses(AnnotatedBeanConfig.class);
        assertTrue(Springs.isInitialized());

        ICompressor compressor = (ICompressor) Springs.getBean("defaultCompressor");
        assertNotNull(compressor);
        assertTrue(compressor instanceof GZipCompressor);
    }

    @Test
    public void initByPackages() {
        Springs.initByPackages(AnnotatedBeanConfig.class.getPackage().getName());
        assertTrue(Springs.isInitialized());

        String[] beanNames = Springs.getBeanNamesForType(ICompressor.class, true, true);

        assertTrue(beanNames.length > 0);

        ICompressor compressor = (ICompressor) Springs.getBean("defaultCompressor");
        assertNotNull(compressor);
        assertTrue(compressor instanceof GZipCompressor);
    }
}
