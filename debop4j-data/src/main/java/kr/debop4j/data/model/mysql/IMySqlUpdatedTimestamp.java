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

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import java.sql.Timestamp;

/**
 * MySQL 에서는 레코드의 최신 갱신 일자를 직접 지원한다.
 * 이를 정의한 인터페이스를 사용하게 되면, 굳이 Interceptor 를 통해 UpdateTimestamp 를 변경할 필요도 없다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 6. 25. 오후 2:56
 */
public interface IMySqlUpdatedTimestamp {

    /** MySQL 에서 레코드별로 최신 갱신일자를 표현합니다. */
    @Column( updatable = false, insertable = false, columnDefinition = "timestamp default current_timestamp on update current_timestamp" )
    @Generated( GenerationTime.ALWAYS )
    Timestamp getUpdatedTimestamp();
}
