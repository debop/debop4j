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

package kr.debop4j.data.hibernate;

import com.google.common.base.Objects;
import kr.debop4j.data.NamedParameterBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.ObjectType;

/**
 * Hibernate용 Parameter 정보를 표현합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 19
 */
public class HibernateParameter extends NamedParameterBase {

    @Getter
    @Setter
    private org.hibernate.type.Type type;

    /**
     * Instantiates a new Hibernate parameter.
     *
     * @param name  the parameter name
     * @param value the parameter value
     */
    public HibernateParameter(String name, Object value) {
        this(name, value, ObjectType.INSTANCE);
    }

    /**
     * Instantiates a new Hibernate parameter.
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @param type  the parameter type
     */
    public HibernateParameter(String name, Object value, org.hibernate.type.Type type) {
        super(name, value);
        this.type = type;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                    .add("type", type);
    }

    private static final long serialVersionUID = -6291985997768450558L;
}
