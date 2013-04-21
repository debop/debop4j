/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
