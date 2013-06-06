/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.debop4j.data.hibernate.usertype;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Joda-Time 의 DateTime 을 위한 Hibernate UserType 입니다. {@link DateTime}
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 26. 오후 6:17
 */
@Slf4j
public class JodaDateTimeUserType implements UserType, Serializable {

    public static DateTime asDateTime(final Object value) {
        if (log.isTraceEnabled())
            log.trace("값을 DateTime으로 변환합니다. value=[{}]", value);

        if (value == null)
            return null;
        if (value instanceof Long)
            return new DateTime(value);
        if (value instanceof Date)
            return new DateTime(((Date) value).getTime());
        if (value instanceof DateTime)
            return (DateTime) value;

        return null;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { StandardBasicTypes.TIMESTAMP.sqlType() };
    }

    @Override
    public Class returnedClass() {
        return DateTime.class;
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
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        Object value = StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names[0], session, owner);
        return asDateTime(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index, session);
        } else {
            StandardBasicTypes.TIMESTAMP.nullSafeSet(st, asDateTime(value).toDate(), index, session);
        }
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

    private static final long serialVersionUID = -3556921355917963632L;
}
