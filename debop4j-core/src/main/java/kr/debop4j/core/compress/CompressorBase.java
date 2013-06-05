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

import kr.debop4j.core.tools.StringTool;

import java.io.IOException;

/**
 * 압축기의 기본 클래스입니다. Template pattern을 이용하여, 압축/복원 전후의 루틴한 작업을 추상 클래스로 뺐습니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public abstract class CompressorBase implements ICompressor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CompressorBase.class);

    /**
     * 압축 알고리즘 이름
     * @return 알고리즘 명
     */
    abstract public String getAlgorithm();

    /**
     * byte 배열을 압축합니다.
     *
     * @param plain the plain
     * @return the byte [ ]
     * @throws IOException the iO exception
     */
    abstract protected byte[] doCompress(byte[] plain) throws IOException;

    /**
     * 바이트 배열의 압축을 풉니다.
     *
     * @param compressed the compressed
     * @return the byte [ ]
     * @throws IOException the iO exception
     */
    abstract protected byte[] doDecompress(byte[] compressed) throws IOException;

    /** {@inheritDoc} */
    public final byte[] compress(byte[] plain) {
        if (plain == null || plain.length == 0)
            return new byte[0];

        if (log.isTraceEnabled())
            log.trace("데이터를 압축합니다... algorithm=[{}]", getAlgorithm());

        try {
            byte[] result = doCompress(plain);

            if (log.isTraceEnabled())
                log.trace("데이터를 압축을 수행했습니다. 압축률=[{}], original=[{}], compressed=[{}]",
                          result.length * 100.0 / plain.length, plain.length, result.length);
            return result;
        } catch (IOException e) {
            log.error("압축 시 예외가 발생했습니다...", e);
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    public final byte[] decompress(byte[] compressed) {
        if (compressed == null || compressed.length == 0)
            return new byte[0];

        if (log.isTraceEnabled())
            log.trace("압축된 데이타를 복구합니다... algorithm=[{}]", getAlgorithm());

        try {
            byte[] result = doDecompress(compressed);

            if (log.isTraceEnabled())
                log.trace("압축 데이터를 복원했습니다. 압축률=[{}], compressed=[{}], original=[{}]",
                          compressed.length * 100.0 / result.length, compressed.length, result.length);
            return result;
        } catch (IOException e) {
            log.error("압축해제 시 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /** 문자열을 압축하여, base64 문자열로 만듭니다. */
    @Override
    public String compressString(String plainText) {
        if (StringTool.isWhiteSpace(plainText))
            return StringTool.EMPTY_STR;

        if (log.isTraceEnabled())
            log.trace("문자열을 압축합니다. plainText=[{}]", plainText);

        byte[] bytes = compress(StringTool.getUtf8Bytes(plainText));
        return StringTool.encodeBase64String(bytes);
    }

    /** 압축된 base64 문자열을 복원하여 일반 문자열로 만듭니다. */
    @Override
    public String decompressString(String compressedBase64) {
        if (StringTool.isWhiteSpace(compressedBase64))
            return StringTool.EMPTY_STR;

        if (log.isTraceEnabled())
            log.trace("문자열을 복원합니다. compressedBase64=[{}]", compressedBase64);

        byte[] bytes = StringTool.decodeBase64(compressedBase64);
        return StringTool.getUtf8String(decompress(bytes));
    }

    public String toString() {
        return getAlgorithm() + "ICompressor";
    }
}
