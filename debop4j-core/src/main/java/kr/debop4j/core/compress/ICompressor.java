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

package kr.debop4j.core.compress;

/**
 * 압축/복원을 수행하는 Compressor
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public interface ICompressor {

    int BUFFER_SIZE = 4096;

    /** 압축 알고리즘 정보 */
    String getAlgorithm();

    /**
     * 정보를 압축합니다.
     *
     * @param plain 압축할 데이타
     * @return 압축한 바이트 배열
     */
    byte[] compress(byte[] plain);

    /**
     * 압축된 정보를 복원합니다.
     *
     * @param compressed 압축된 데이타 정보
     * @return 압축 해제한 바이트 배열
     */
    byte[] decompress(byte[] compressed);

    /** 문자열을 압축하여, base64 문자열로 만듭니다. */
    String compressString(String plainText);

    /** 압축된 base64 문자열을 복원하여 일반 문자열로 만듭니다. */
    String decompressString(String compressedBase64);
}