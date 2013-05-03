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
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 주소를 나타내는 Component
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 1.
 */
@Embeddable
@Getter
@Setter
public class AddressComponent extends ValueObjectBase {

    private String street1;
    private String street2;

    @Column(length = 128)
    private String city;

    @Column(length = 48)
    private String state;

    @Column(length = 24)
    private String country;

    @Column(length = 24)
    private String zipcode;

    @Override
    public int hashCode() {
        return HashTool.compute(zipcode, country, state, city, street1, street2);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("zipcode", zipcode)
                .add("country", country);
    }
}
