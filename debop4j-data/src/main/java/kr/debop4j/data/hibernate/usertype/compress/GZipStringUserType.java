package kr.debop4j.data.hibernate.usertype.compress;

import kr.debop4j.core.compress.GZipCompressor;
import kr.debop4j.core.compress.ICompressor;

/**
 * GZip 알고리즘 ({@link GZipCompressor} 으로 문자열 속성 값을 압축하여 Binary로 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class GZipStringUserType extends AbstractCompressedStringUserType {

    private static final ICompressor compressor = new GZipCompressor();

    @Override
    public ICompressor getCompressor() {
        return compressor;
    }
}
