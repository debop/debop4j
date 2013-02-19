package kr.debop4j.data.hibernate.usertype.compress;

import kr.debop4j.core.compress.BZip2Compressor;
import kr.debop4j.core.compress.ICompressor;

/**
 * BZip2 알고리즘 ({@link BZip2Compressor} 으로 문자열 속성 값을 압축하여 Binary로 저장합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 18
 */
public class BZip2StringUserType extends AbstractCompressedStringUserType {

    private static final ICompressor compressor = new BZip2Compressor();

    @Override
    public ICompressor getCompressor() {
        return compressor;
    }
}
