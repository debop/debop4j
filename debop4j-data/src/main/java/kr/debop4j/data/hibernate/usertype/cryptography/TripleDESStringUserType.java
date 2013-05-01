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

package kr.debop4j.data.hibernate.usertype.cryptography;

import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.cryptography.symmetric.TripleDESByteEncryptor;
import lombok.extern.slf4j.Slf4j;

/**
 * TripleDES 알고리즘을 이용한 {@link TripleDESByteEncryptor} 를 이용하여, 속성 값을 암호화하여 저장합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 18
 */
@Slf4j
public class TripleDESStringUserType extends AbstractSymmetricEncryptStringUserType {

    private static final ISymmetricByteEncryptor encryptor = new TripleDESByteEncryptor();
    private static final long serialVersionUID = 2584675219844345168L;

    @Override
    public ISymmetricByteEncryptor getEncryptor() {
        return encryptor;
    }
}
