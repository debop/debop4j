package kr.debop4j.data.hibernate.usertype.compress;

import kr.debop4j.core.tools.ArrayTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BinaryType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 데이터를 압축하여, Blob 컬럼에 저장합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 18
 */
@Slf4j
public abstract class AbstractCompressedBinaryUserType extends AbstractCompressedUserType {

    protected byte[] compress(byte[] value) throws Exception {
        if (ArrayTool.isEmpty(value))
            return null;

        return getCompressor().compress(value);
    }

    protected byte[] decompress(byte[] value) throws Exception {
        if (ArrayTool.isEmpty(value))
            return ArrayTool.EmptyByteArray;

        return getCompressor().decompress(value);
    }

    @Override
    public Class returnedClass() {
        return byte[].class;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet,
                              String[] strings,
                              SessionImplementor sessionImplementor,
                              Object o)
            throws HibernateException, SQLException {
        try {
            byte[] value = BinaryType.INSTANCE.nullSafeGet(resultSet, strings[0], sessionImplementor);
            return decompress(value);
        } catch (Exception ex) {
            log.error("column=" + strings[0] + " 정보를 읽어 압축 복원하는데 실패했습니다.", ex);
            throw new HibernateException("압축된 정보를 복원하는데 실패했습니다.", ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object o,
                            int i,
                            SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {

        try {
            byte[] value = ArrayTool.isEmpty((byte[]) o) ? null : compress((byte[]) o);
            BinaryType.INSTANCE.nullSafeSet(preparedStatement, value, i, sessionImplementor);
        } catch (Exception ex) {
            log.error("Statement=[{}], index=[{}]에 해당하는 값을 압축하는데 실패했습니다.", preparedStatement, i);
            throw new HibernateException("정보를 압축하는데 실패했습니다.", ex);
        }
    }

    @Override
    public boolean isMutable() {
        return BinaryType.INSTANCE.isMutable();
    }
}
