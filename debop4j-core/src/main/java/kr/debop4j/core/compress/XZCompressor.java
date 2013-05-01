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
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * XZ 알고리즘을 이용한 압축/복원을 수행합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12.
 */
@Slf4j
public class XZCompressor extends CompressorBase {

    @Override
    public String getAlgorithm() {
        return "XZ";
    }

    @Override
    protected byte[] doCompress(byte[] plain) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup XZCompressorOutputStream xz = new XZCompressorOutputStream(bos);

        xz.write(plain);
        xz.close();

        return bos.toByteArray();
    }

    @Override
    protected byte[] doDecompress(byte[] compressed) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        @Cleanup XZCompressorInputStream xz = new XZCompressorInputStream(bis);

        byte[] buff = new byte[BUFFER_SIZE];
        int n;
        while ((n = xz.read(buff, 0, BUFFER_SIZE)) > 0) {
            bos.write(buff, 0, n);
        }
        return bos.toByteArray();
    }
}
