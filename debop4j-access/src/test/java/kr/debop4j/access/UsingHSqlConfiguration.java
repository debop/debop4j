package kr.debop4j.access;

import kr.debop4j.access.model.common.Code;
import kr.debop4j.access.model.organization.Company;
import kr.debop4j.access.model.product.Product;
import kr.debop4j.access.model.workcalendar.WorkCalendar;
import kr.debop4j.data.hibernate.springconfiguration.HSqlConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * kr.debop4j.access.UsingHSqlConfiguration
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@Configuration
@EnableTransactionManagement
public class UsingHSqlConfiguration extends HSqlConfigBase {

    @Override
    protected String[] getMappedPackageNames() {
        return new String[]{
                Code.class.getPackage().getName(),
                Company.class.getPackage().getName(),
                Product.class.getPackage().getName(),
                WorkCalendar.class.getPackage().getName(),
        };
    }
}
