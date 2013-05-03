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

package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

/**
 * debop4j access 모듈의 엔티티들의 기본 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 8 오후 1:12
 */
@MappedSuperclass
public abstract class AccessEntityBase extends AnnotatedEntityBase implements IUpdateTimestampedEntity {

    private static final long serialVersionUID = -7640693368412411167L;

    @Type(type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType")
    @Getter
    private DateTime updatedTime;

    @Transient
    @Override
    public Date getUpdateTimestamp() {
        return updatedTime.toDate();
    }

    @Override
    public void updateUpdateTimestamp() {
        updatedTime = DateTime.now();
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("updatedTime", updatedTime);
    }

}
