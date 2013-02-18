package kr.debop4j.core.compress;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * pudding.pudding.commons.core.compress.DeflateCompressor
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12.
 */
@Slf4j
public class DeflateCompressor extends CompressorBase {

    @Override
    public String getAlgorithm() {
        return "Deflate";
    }

    @Override
    protected byte[] doCompress(byte[] plain) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup DeflaterOutputStream deflater = new DeflaterOutputStream(bos);

        deflater.write(plain);
        deflater.close();

        return bos.toByteArray();
    }

    @Override
    protected byte[] doDecompress(byte[] compressed) throws IOException {

        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        @Cleanup InflaterInputStream inflater = new InflaterInputStream(bis);

        byte[] buff = new byte[BUFFER_SIZE];
        int n;

        while ((n = inflater.read(buff, 0, BUFFER_SIZE)) > 0) {
            bos.write(buff, 0, n);
        }
        return bos.toByteArray();

    }
}
