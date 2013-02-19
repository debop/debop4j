package kr.debop4j.data.mapping.model.annotated.usertypes;

import kr.debop4j.core.tools.HashTool;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * 미국 달러 기준으로 DB에 저장하고, 로딩 시에는 현 Locale에서 쓰는 통화단위로 변환한 값을 제공한다.
 * User: sunghyouk.bae@gmail.com
 * Date: 12. 12. 6.
 */
public class MonetaryAmountUserType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{StandardBasicTypes.BIG_DECIMAL.sqlType()};
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
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {

        BigDecimal valueInUSD = rs.getBigDecimal(names[0]);
        if (rs.wasNull())
            return null;

        Currency userCurrency = Currency.getInstance(Locale.getDefault());
        MonetaryAmount amount = new MonetaryAmount(valueInUSD, Currency.getInstance("USD"));
        return amount.convertTo(userCurrency);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, sqlTypes()[0]);
        } else {
            MonetaryAmount anyCurrency = (MonetaryAmount) value;
            MonetaryAmount amountUSD = anyCurrency.convertTo(Currency.getInstance("USD"));
            st.setBigDecimal(index, amountUSD.getAmount());
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
