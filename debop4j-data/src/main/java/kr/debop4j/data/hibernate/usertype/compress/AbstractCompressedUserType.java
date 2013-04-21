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

package kr.debop4j.data.hibernate.usertype.compress;

import com.google.common.base.Objects;
import kr.debop4j.core.compress.ICompressor;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BinaryType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 엔티티의 속성 값을 압축하여 DB에 저장 / 로드하는 Hibernate 사용자 수형의 기본 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
public abstract class AbstractCompressedUserType implements UserType {

    /**
     * 속성 값을 압축/복원하는 {@link ICompressor}의 구현 객체를 반환합니다.
     */
    abstract public ICompressor getCompressor();


    abstract public Class returnedClass();

    abstract public Object nullSafeGet(ResultSet resultSet,
                                       String[] strings,
                                       SessionImplementor sessionImplementor,
                                       Object o) throws
            HibernateException,
            SQLException;

    abstract public void nullSafeSet(PreparedStatement preparedStatement,
                                     Object o,
                                     int i,
                                     SessionImplementor sessionImplementor) throws
            HibernateException,
            SQLException;

    abstract public boolean isMutable();

    @Override
    public int[] sqlTypes() {
        return new int[]{ BinaryType.INSTANCE.sqlType() };
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equal(x, y);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return Objects.hashCode(o);
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }


    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) deepCopy(o);
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return deepCopy(serializable);
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return deepCopy(o);
    }
}
