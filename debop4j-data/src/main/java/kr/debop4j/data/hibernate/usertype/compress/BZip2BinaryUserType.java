package kr.debop4j.data.hibernate.usertype.compress;

import kr.debop4j.core.compress.BZip2Compressor;
import kr.debop4j.core.compress.ICompressor;

/**
 * GZip 알고리즘 ({@link BZip2Compressor} 으로 이진 데이터 값을 압축하여 Binary로 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public class BZip2BinaryUserType extends AbstractCompressedBinaryUserType {

    private static final ICompressor compressor = new BZip2Compressor();

    @Override
    public ICompressor getCompressor() {
        return compressor;
    }
}
