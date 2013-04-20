package kr.debop4j.data.hibernate.tools;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * 속성명을 ORACLE 명명규칙을 사용하여 DB 엔티티의 요소를 변경한다.
 * {@link org.hibernate.cfg.Configuration#setNamingStrategy(org.hibernate.cfg.NamingStrategy)} 메소드를 사용합니다.)
 * Jpa@author sunghyouk.bae@gmail.com
 *
 * @since 12. 11. 19
 */
public class OracleNamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = -5499015346115407402L;

    @Override
    public String classToTableName(String className) {
        return super.classToTableName(className).toUpperCase();
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        return super.propertyToColumnName(propertyName).toUpperCase();
    }

    @Override
    public String tableName(String tableName) {
        return super.tableName(tableName).toUpperCase();
    }

    @Override
    public String columnName(String columnName) {
        return super.columnName(columnName).toUpperCase();
    }
}
