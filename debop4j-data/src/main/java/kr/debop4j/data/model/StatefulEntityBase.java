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

package kr.debop4j.data.model;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;

/**
 * Hibernate 저장 상태를 표현하는 추상화 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 19
 */
public abstract class StatefulEntityBase extends ValueObjectBase implements IStatefulEntity {

    private boolean persisted = false;

    @Override
    public boolean isPersisted() {
        return persisted;
    }

    @Override
    public void onSave() {
        persisted = true;
    }

    @Override
    public void onPersist() {
        persisted = true;
    }

    @Override
    public void onLoad() {
        persisted = true;
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("persisted", persisted);
    }

    private static final long serialVersionUID = -902380618446075689L;
}
