package kr.debop4j.core.compress;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * pudding.pudding.commons.core.compress.BZip2Compressor
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12.
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
