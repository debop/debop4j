package kr.debop4j.data.ogm;

import kr.debop4j.data.ogm.dao.Player;
import kr.debop4j.data.ogm.spring.cfg.GridDatastoreConfigBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * kr.debop4j.data.ogm.GridDatastoreConfiguration
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 16. 오후 12:05
 */
@Configuration
@Slf4j
public class GridDatastoreConfiguration extends GridDatastoreConfigBase {
    @Override
    protected String getDatabaseName() {
        return "debop4j_ogm";
    }

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Player.class.getPackage().getName()
        };
    }

//    @Override
//    protected Class<?>[] getMappedEntities() {
//        return new Class<?>[] { Player.class };
//    }
}
