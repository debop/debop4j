package kr.debop4j.data.hibernate.usertype.cryptography;

import com.google.common.base.Objects;
import kr.debop4j.core.BinaryStringFormat;
import kr.debop4j.core.cryptography.symmetric.ISymmetricByteEncryptor;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 문자열을 암호화하여, HexDecimal 형태의 문자열로 변환하여 DB에 저장합니다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 9. 18
 */
@Slf4j
public abstract class AbstractSymmetricEncryptStringUserType implements UserType {

    abstract public ISymmetricByteEncryptor getEncryptor();

    /**
     * 문자열을 암호화하여, 16진법 문자열로 변환합니다.
     */
    protected String encrypt(String value) throws Exception {
        if (value == null)
            return null;

        if (log.isDebugEnabled())
            log.debug("{} 를 이용하여 문자열을 암호화합니다. value={}", getEncryptor(), StringTool.ellipsisChar(value, 80));

        byte[] bytes = getEncryptor().encrypt(StringTool.getUtf8Bytes(value));
        return StringTool.getStringFromBytes(bytes, BinaryStringFormat.HexDecimal);
    }

    /**
     * 암호화된 16진법 문자열을 복원하여, 원래 문자열로 변환합니다.
     */
    protected String decrypt(String value) throws Exception {
        if (value == null)
            return null;

        if (log.isDebugEnabled())
            log.debug("{}를 이용하여 암호화된 문자열을 복원합니다. value={}",
                      getEncryptor(), StringTool.ellipsisChar(value, 80));

        byte[] bytes = getEncryptor().decrypt(StringTool.getBytesFromString(value, BinaryStringFormat.HexDecimal));
        return StringTool.getUtf8String(bytes);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{StringType.INSTANCE.sqlType()};
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equal(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs,
                              String[] names,
                              SessionImplementor session,
                              Object owner) throws HibernateException,
            SQLException {
        try {
            String hexStr = StringType.INSTANCE.nullSafeGet(rs, names[0], session);
            return decrypt(hexStr);
        } catch (Exception ex) {
            throw new HibernateException("암호화를 복원하는데 실패했습니다.", ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st,
                            Object value,
                            int index,
                            SessionImplementor session) throws HibernateException,
            SQLException {
        try {
            StringType.INSTANCE.nullSafeSet(st, encrypt((String) value), index, session);
        } catch (Exception ex) {
            throw new HibernateException("암호화를 수행하는데 실패했습니다.", ex);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return StringType.INSTANCE.isMutable();
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
