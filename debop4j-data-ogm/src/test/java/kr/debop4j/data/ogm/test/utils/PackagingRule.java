package kr.debop4j.data.ogm.test.utils;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * test case useful when one want to write a test relying on an archive (like a JPA archive)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 12. 오후 1:34
 */
public class PackagingRule extends TemporaryFolder {

    private static final ArchivePath persistencePath = ArchivePaths.create("persistence.xml");
    protected static ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

    private final JavaArchive archive;
    private final File testPackage;

    public PackagingRule(String persistenceConfResource, Class<?>... entities) {
        archive = ShrinkWrap.create(JavaArchive.class, "jtastandalone.jar");
        archive.addClasses(entities);
        archive.addAsManifestResource(persistenceConfResource, persistencePath);
        try {
            create();
            testPackage = newFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        archive.as(ZipExporter.class).exportTo(testPackage, true);
    }

    @Override
    public void before() throws Throwable {
        super.before();
        URLClassLoader classLoader = new URLClassLoader(new URL[] { testPackage.toURL() }, originalClassLoader);
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    @Override
    protected void after() {
        // reset the classloader
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        super.after();
    }
}
