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

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * BZip2 알고리즘을 사용하는 Compressor
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12.
 */
@Slf4j
public class BZip2Compressor extends CompressorBase {

    @Override
    public String getAlgorithm() {
        return "BZip2";
    }

    @Override
    protected byte[] doCompress(byte[] plain) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup BZip2CompressorOutputStream bzip2 = new BZip2CompressorOutputStream(bos);

        bzip2.write(plain);
        bzip2.close();

        return bos.toByteArray();
    }

    @Override
    protected byte[] doDecompress(byte[] compressed) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        @Cleanup BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(bis);

        byte[] buff = new byte[BUFFER_SIZE];
        int n;
        while ((n = bzip2.read(buff, 0, BUFFER_SIZE)) > 0) {
            bos.write(buff, 0, n);
        }

        return bos.toByteArray();
    }
}
