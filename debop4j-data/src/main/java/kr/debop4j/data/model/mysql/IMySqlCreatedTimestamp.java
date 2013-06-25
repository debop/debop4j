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

package kr.debop4j.data.model.mysql;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * MySQL 에서는 레코드의 생성 일자를 기본값으로 가지게 한다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 25. 오후 3:08
 */
public interface IMySqlCreatedTimestamp {

    /** MySQL 에서 레코드별로 최신 갱신일자를 표현합니다. */
    @Column( updatable = false, insertable = false, columnDefinition = "timestamp default current_timestamp" )
    Timestamp getCreatedTimestamp();
}
