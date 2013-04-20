package kr.debop4j.data.hibernate.usertype.compress;

import kr.debop4j.core.tools.ArrayTool;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BinaryType;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 엔티티의 속성 값을 압축하여 DB에 Hex Decimal 문자열로 저장합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
@Slf4j
public abstract class AbstractCompressedStringUserType extends AbstractCompressedUserType {

    protected byte[] compress(String value) throws Exception {
        if (StringTool.isEmpty(value))
            return null;

        return getCompressor().compress(StringTool.getUtf8Bytes(value));
    }

    protected String decompress(byte[] value) throws Exception {
        if (ArrayTool.isEmpty(value))
            return StringTool.EMPTY_STR;

        return StringTool.getUtf8String(getCompressor().decompress(value));
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet,
                              String[] strings,
                              SessionImplementor sessionImplementor,
                              Object o) throws
            HibernateException,
            SQLException {
        try {
            byte[] value = BinaryType.INSTANCE.nullSafeGet(resultSet, strings[0], sessionImplementor);
            return decompress(value);
        } catch (Exception ex) {
            log.error("column=[" + strings[0] + "] 정보를 읽어 압축 복원하는데 실패했습니다.", ex);
            throw new HibernateException("압축된 정보를 복원하는데 실패했습니다.", ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object o,
                            int i,
                            SessionImplementor sessionImplementor) throws
            HibernateException,
            SQLException {
        try {
            byte[] value = compress((String) o);
            BinaryType.INSTANCE.nullSafeSet(preparedStatement, value, i, sessionImplementor);
        } catch (Exception ex) {
            log.error("Statement=[" + preparedStatement + "], index=[" + i + "]에 해당하는 값을 압축하는데 실패했습니다.", ex);
            throw new HibernateException("압축된 정보를 복원하는데 실패했습니다.", ex);
        }
    }

    @Override
    public boolean isMutable() {
        return StringType.INSTANCE.isMutable();
    }
}
