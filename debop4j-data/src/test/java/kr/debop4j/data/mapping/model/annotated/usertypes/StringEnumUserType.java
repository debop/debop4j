package kr.debop4j.data.mapping.model.annotated.usertypes;

import kr.debop4j.core.tools.HashTool;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * org.annotated.mapping.domain.model.usertypes.StringEnumUserType
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 6.
 */
public class StringEnumUserType implements EnhancedUserType, ParameterizedType {

    private Class<Enum> enumClass;

    @Override
    public String objectToSQLString(Object value) {
        return '\'' + ((Enum) value).name() + '\'';
    }

    @Override
    public String toXMLString(Object value) {
        return ((Enum) value).name();
    }

    @Override
    public Object fromXMLString(String xmlValue) {
        return Enum.valueOf(enumClass, xmlValue);
    }

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
        return new int[]{StandardBasicTypes.STRING.sqlType()};
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
        return HashTool.compute(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {

        String value = (String) StringType.INSTANCE.nullSafeGet(rs, names[0], session, owner);
        return rs.wasNull() ? null : fromXMLString(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st,
                            Object value,
                            int index,
                            SessionImplementor session) throws HibernateException, SQLException {

        StringType.INSTANCE.nullSafeSet(st,
                                        (value != null) ? value.toString() : null,
                                        index,
                                        session);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
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
