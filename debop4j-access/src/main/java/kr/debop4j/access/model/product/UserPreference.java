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

package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.PreferenceBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * 사용자 환경 설정 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 10.
 */
@Entity
@Table(name = "UserPreference")
@org.hibernate.annotations.Table(appliesTo = "UserPreference",
                                 indexes = @Index(name = "ix_userpreference",
                                                  columnNames = {
                                                          "UserId",
                                                          "PrefKey" }))
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserPreference extends PreferenceBase {

    private static final long serialVersionUID = -4417301208305719344L;

    protected UserPreference() {}

    public UserPreference(User user, String key, String value) {
        super(key, value);

        Guard.shouldNotBeNull(user, "user");

        this.user = user;
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(user, getKey());
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("user", user.getId())
                .add("key", getKey())
                .add("value", getValue());
    }
}
