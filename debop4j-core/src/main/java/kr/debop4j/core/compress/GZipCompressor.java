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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip 압축을 수행합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
@Slf4j
public class GZipCompressor extends CompressorBase {

    @Override
    public String getAlgorithm() {
        return "GZip";
    }

    @Override
    protected byte[] doCompress(byte[] plain) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup GZIPOutputStream gzip = new GZIPOutputStream(bos);

        gzip.write(plain);
        gzip.close();

        return bos.toByteArray();

    }

    @Override
    protected byte[] doDecompress(byte[] compressed) throws IOException {

        @Cleanup
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        @Cleanup
        GZIPInputStream gzip = new GZIPInputStream(bis);

        byte[] buff = new byte[BUFFER_SIZE];
        int n;
        while ((n = gzip.read(buff, 0, BUFFER_SIZE)) > 0) {
            bos.write(buff, 0, n);
        }
        return bos.toByteArray();
    }
}
