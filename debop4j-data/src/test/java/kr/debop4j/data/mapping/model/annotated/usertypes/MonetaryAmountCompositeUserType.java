package kr.debop4j.data.mapping.model.annotated.usertypes;

import kr.debop4j.core.tools.HashTool;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.CurrencyType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * 금액과 통화 단위 묶음을 하나의 수형으로 취급합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 12. 6.
 */
public class MonetaryAmountCompositeUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{ "amount", "currency" };
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{ BigDecimalType.INSTANCE, CurrencyType.INSTANCE };
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        if (component == null)
            return null;

        MonetaryAmount ma = (MonetaryAmount) component;
        if (property == 0)
            return ma.getAmount();
        else
            return ma.getCurrency();
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new HibernateException("MonetaryAmount is immutable type");
    }

    @Override
    public Class returnedClass() {
        return MonetaryAmount.class;
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
    public Object nullSafeGet(ResultSet rs,
                              String[] names,
                              SessionImplementor session,
                              Object owner)
            throws HibernateException, SQLException {

        BigDecimal amount = rs.getBigDecimal(names[0]);
        if (rs.wasNull())
            return null;

        String currencyCode = rs.getString(names[1]);
        Currency currency = (rs.wasNull())
                ? Currency.getInstance(Locale.getDefault())
                : Currency.getInstance(currencyCode);
        return new MonetaryAmount(amount, currency);
    }

    @Override
    public void nullSafeSet(PreparedStatement st,
                            Object value,
                            int index,
                            SessionImplementor session)
            throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, BigDecimalType.INSTANCE.sqlType());
            st.setNull(index + 1, CurrencyType.INSTANCE.sqlType());
        } else {
            MonetaryAmount ma = (MonetaryAmount) value;
            String currencyCode = ma.getCurrency().getCurrencyCode();
            st.setBigDecimal(index, ma.getAmount());
            st.setString(index + 1, currencyCode);
        }
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
    public Serializable disassemble(Object value,
                                    SessionImplementor session) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached,
                           SessionImplementor session,
                           Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original,
                          Object target,
                          SessionImplementor session,
                          Object owner) throws HibernateException {
        return deepCopy(original);
    }
}
