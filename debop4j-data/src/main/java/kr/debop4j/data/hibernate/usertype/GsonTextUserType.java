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

package kr.debop4j.data.hibernate.usertype;

import kr.debop4j.core.json.GsonSerializer;
import kr.debop4j.core.json.IJsonSerializer;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link GsonSerializer} 를 이용하여, 객체를 Json 직렬화하여 저장하는 사용자 타입입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 18
 */
@Slf4j
public class GsonTextUserType extends AbstractJsonTextUserType {

    private static final IJsonSerializer serializer = new GsonSerializer();
    private static final long serialVersionUID = 6937365577059208225L;

    @Override
    public IJsonSerializer getJsonSerializer() {
        return serializer;
    }
}
