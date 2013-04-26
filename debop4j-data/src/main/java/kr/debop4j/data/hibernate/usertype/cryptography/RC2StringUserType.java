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
import kr.debop4j.core.cryptography.symmetric.RC2ByteEncryptor;
import lombok.extern.slf4j.Slf4j;

/**
 * RC2 알고리즘을 이용한 {@link RC2ByteEncryptor} 를 이용하여, 속성 값을 16진수 문자열로 암호화하여 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
@Slf4j
public class RC2StringUserType extends AbstractSymmetricEncryptStringUserType {

    private static final ISymmetricByteEncryptor encryptor = new RC2ByteEncryptor();
    private static final long serialVersionUID = -4671257855747547374L;

    @Override
    public ISymmetricByteEncryptor getEncryptor() {
        return encryptor;
    }
}
