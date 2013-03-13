package kr.debop4j.data.hibernate.tools;

import org.hibernate.cfg.ImprovedNamingStrategy;

import static org.hibernate.internal.util.StringHelper.toUpperCase;
import static org.hibernate.internal.util.StringHelper.unqualify;

/**
 * 속성명을 ORACLE 명명규칙을 사용하여 DB 엔티티의 요소를 변경한다. (Configuration.setNamingStrategy() 메소드를 사용합니다.)
 * JpaUser: sunghyouk.bae@gmail.com
 * Date: 12. 11. 19
 */
public class OracleNamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = -5499015346115407402L;

    public String classToTableName(String className) {
        return toUpperCase(unqualify(className));
    }

    public String propertyToColumnName(String propertyName) {
        return toUpperCase(propertyName);
    }

    public String tableName(String tableName) {
        return toUpperCase(tableName);
    }

    public String columnName(String columnName) {
        return toUpperCase(columnName);
    }

    public String propertyToTableName(String className, String propertyName) {
        return classToTableName(className) + '_' + propertyToColumnName(propertyName);
    }
}
