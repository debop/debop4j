package kr.debop4j.data.hibernate.usertype;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * Enum 값을 문자열로 DB에 저장하고, 로드 시에 Enum 값으로 파싱해서 설정한다.
 * Jpa@author sunghyouk.bae@gmail.com
 *
 * @since 12. 11. 19.
 */
@Slf4j
public class StringEnumUserType implements UserType, ParameterizedType {

    private Class<Enum> enumClass;

    @Override
    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClassName");
        try {
            enumClass = ReflectHelper.classForName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found. enumClassName=" + enumClassName, e);
        }
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{ StandardBasicTypes.STRING.sqlType() };
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {

        String value = rs.getString(names[0]);
        return rs.wasNull() ? null : Enum.valueOf(enumClass, value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {

        if (value == null)
            st.setNull(index, sqlTypes()[0]);
        else
            st.setString(index, ((Enum) value).name());
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
