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
