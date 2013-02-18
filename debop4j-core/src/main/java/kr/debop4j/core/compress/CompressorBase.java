package kr.debop4j.core.compress;

import kr.nsoft.commons.tools.StringTool;

import java.io.IOException;

/**
 * 압축기의 기본 클래스입니다. Template pattern을 이용하여, 압축/복원 전후의 루틴한 작업을 추상 클래스로 뺐습니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 12
 */
public abstract class CompressorBase implements ICompressor {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CompressorBase.class);

    abstract public String getAlgorithm();

    abstract protected byte[] doCompress(byte[] plain) throws IOException;

    abstract protected byte[] doDecompress(byte[] compressed) throws IOException;

    /**
     * {@inheritDoc}
     */
    public final byte[] compress(byte[] plain) {
        if (plain == null || plain.length == 0)
            return new byte[0];

        if (log.isDebugEnabled())
            log.debug("데이터를 압축합니다... algorithm=" + getAlgorithm());

        try {
            byte[] result = doCompress(plain);

            if (log.isDebugEnabled())
                log.debug("데이터를 압축을 수행했습니다. 압축률=[{}], original=[{}], compressed=[{}]",
                          result.length * 100.0 / plain.length, plain.length, result.length);
            return result;
        } catch (IOException e) {
            if (log.isErrorEnabled())
                log.error("압축 시 예외가 발생했습니다...", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public final byte[] decompress(byte[] compressed) {
        if (compressed == null || compressed.length == 0)
            return new byte[0];

        if (log.isDebugEnabled())
            log.debug("압축된 데이타를 복구합니다... algorithm=" + getAlgorithm());

        try {
            byte[] result = doDecompress(compressed);

            if (log.isDebugEnabled())
                log.debug("압축 데이터를 복원했습니다. 압축률=[{}], compressed=[{}], original=[{}]",
                          compressed.length * 100.0 / result.length, compressed.length, result.length);
            return result;
        } catch (IOException e) {
            if (log.isErrorEnabled())
                log.error("압축해제 시 예외가 발생했습니다.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 문자열을 압축하여, base64 문자열로 만듭니다.
     */
    @Override
    public String compressString(String plainText) {
        if (StringTool.isWhiteSpace(plainText))
            return StringTool.EMPTY_STR;

        byte[] bytes = compress(StringTool.getUtf8Bytes(plainText));
        return StringTool.encodeBase64String(bytes);
    }

    /**
     * 압축된 base64 문자열을 복원하여 일반 문자열로 만듭니다.
     */
    @Override
    public String decompressString(String compressedBase64) {
        if (StringTool.isWhiteSpace(compressedBase64))
            return StringTool.EMPTY_STR;

        byte[] bytes = StringTool.decodeBase64(compressedBase64);
        return StringTool.getUtf8String(decompress(bytes));
    }

    public String toString() {
        return getAlgorithm() + "ICompressor";
    }
}
