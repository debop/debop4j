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
 * @author sunghyouk.bae@gmail.com
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
