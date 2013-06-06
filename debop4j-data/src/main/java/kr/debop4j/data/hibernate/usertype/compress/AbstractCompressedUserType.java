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

import kr.debop4j.core.compress.ICompressor;
import kr.debop4j.core.tools.HashTool;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BinaryType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 엔티티의 속성 값을 압축하여 DB에 저장 / 로드하는 Hibernate 사용자 수형의 기본 클래스입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 18
 */
@Slf4j
public abstract class AbstractCompressedUserType implements UserType, Serializable {

    /** 속성 값을 압축/복원하는 {@link ICompressor}의 구현 객체를 반환합니다. */
    abstract public ICompressor getCompressor();


    public abstract Class returnedClass();

    public abstract Object nullSafeGet(final ResultSet resultSet,
                                       final String[] strings,
                                       final SessionImplementor sessionImplementor,
                                       final Object o)
            throws HibernateException, SQLException;

    public abstract void nullSafeSet(final PreparedStatement preparedStatement,
                                     final Object o,
                                     final int i,
                                     final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException;

    abstract public boolean isMutable();

    @Override
    public int[] sqlTypes() {
        return new int[] { BinaryType.INSTANCE.sqlType() };
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return HashTool.compute(o);
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

    private static final long serialVersionUID = -3570021248450932340L;
}
