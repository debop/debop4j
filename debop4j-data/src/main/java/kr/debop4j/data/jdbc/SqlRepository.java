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

package kr.debop4j.data.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

/**
 * Spring Framework의 {@link NamedParameterJdbcDaoSupport}를 상속받아 Jdbc 작업을 손쉽게 할 수 있는 메소드를 제공합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 25.
 */
public class SqlRepository extends NamedParameterJdbcDaoSupport {

    private Logger log = LoggerFactory.getLogger(SqlRepository.class);
    private boolean isTraceEnabled = log.isTraceEnabled();

    /**
     * 쿼리문을 실행합니다.
     *
     * @param sql         실행할 쿼리문
     * @param paramSource 파라미터 명과 값
     * @return 영향을 받은 레코드 수
     */
    public int update(final String sql, final SqlParameterSource paramSource) {

        log.trace("update sql=[{}], parameters=[{}]", sql, paramSource);

        return getNamedParameterJdbcTemplate().update(sql, paramSource);
    }

    /**
     * 지정된 쿼리 문을 실행하여, 결과 셋으로부터 엔티티를 빌드한 후 엔티티의 컬렉션으로 반환합니다.
     *
     * @param sql         실행할 쿼리문
     * @param paramSource 파라미터 명과 값
     * @param rowMapper   결과 셋으로부터 엔티티를 빌드하는 함수
     * @param <E>         엔티티의 수형
     * @return 엔티티의 컬렉션
     */
    public <E> List<E> query(final String sql, final SqlParameterSource paramSource, final RowMapper<E> rowMapper) {

        log.trace("query sql=[{}], paramSource=[{}]", sql, paramSource);

        return getNamedParameterJdbcTemplate().query(sql, paramSource, rowMapper);
    }
}
