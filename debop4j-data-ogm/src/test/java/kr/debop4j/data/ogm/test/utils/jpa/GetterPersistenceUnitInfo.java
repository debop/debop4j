package kr.debop4j.data.ogm.test.utils.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * kr.debop4j.data.ogm.test.utils.jpa.GetterPersistenceUnitInfo
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 12. 오전 11:26
 */
@Slf4j
@Getter
@Setter
@ToString
public class GetterPersistenceUnitInfo implements PersistenceUnitInfo {

    private String persistenceUnitName;
    private String persistenceProviderClassName;
    private PersistenceUnitTransactionType transactionType;
    private DataSource jtaDataSource;
    private DataSource nonJtaDataSource;
    private List<String> mappingFileNames = new ArrayList<String>();
    private List<URL> jarFileUrls = new ArrayList<URL>();
    private URL persistenceUnitRootUrl;
    private List<String> managedClassNames = new ArrayList<String>();
    private boolean excludeUnlistedClasses;
    private SharedCacheMode sharedCacheMode;
    private ValidationMode validationMode;
    private Properties properties;
    private String persistenceXMLSchemaVersion;
    private ClassLoader classLoader;

    @Override
    public boolean excludeUnlistedClasses() {
        return this.excludeUnlistedClasses;
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // nothing to do
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
