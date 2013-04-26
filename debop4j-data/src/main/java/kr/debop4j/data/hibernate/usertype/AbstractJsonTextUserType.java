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
import kr.debop4j.core.Guard;
import kr.debop4j.core.json.IJsonSerializer;
import kr.debop4j.core.json.JsonTextObject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static kr.debop4j.core.tools.StringTool.ellipsisChar;

/**
 * 속성 정보를 Json 직렬화를 수행해 저장합니다.
 * 객체의 실제 수형에 대한 정보는 첫번째 컬럼에 저장되고, 두번째 컬럼에 Json 직렬화 문자열이 저장됩니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 18
 */
@Slf4j
public abstract class AbstractJsonTextUserType implements CompositeUserType, Serializable {

    private static final boolean isTraceEnabled = log.isTraceEnabled();
    private static final long serialVersionUID = -1729578848869460945L;

    abstract public IJsonSerializer getJsonSerializer();

    public JsonTextObject serialize(Object value) {
        if (value == null)
            return JsonTextObject.Empty;

        Guard.shouldBe(value instanceof JsonTextObject,
                       "인스턴스 수형이 JsonTextObject가 아닙니다. value type=" + value.getClass().getName());

        return new JsonTextObject(value.getClass().getName(),
                                  getJsonSerializer().serializeToText(value));
    }

    @SuppressWarnings("unchecked")
    public Object deserialize(JsonTextObject jto) throws HibernateException {

        if (jto == null || jto == JsonTextObject.Empty)
            return null;

        if (isTraceEnabled)
            log.trace("JsonTextObject를 역직렬화 합니다. jto=[{}]", jto);

        try {
            Class clazz = Class.forName(jto.getClassName());
            return getJsonSerializer().deserializeFromText(jto.getJsonText(), clazz);
        } catch (ClassNotFoundException e) {
            return new HibernateException(e);
        }
    }

    public JsonTextObject asJsonTextObject(Object value) {
        if (value == null)
            return JsonTextObject.Empty;

        return (JsonTextObject) value;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{ "className", "jsonText" };
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{ StringType.INSTANCE, StringType.INSTANCE };
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        if (component == null)
            return null;

        JsonTextObject jto = asJsonTextObject(component);
        if (property == 0)
            return jto.getClassName();
        else if (property == 1)
            return jto.getJsonText();
        else
            throw new HibernateException("복합수형의 속성 인덱스가 범위를 벗어났습니다. 0, 1만 가능합니다. property=" + property);
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        Guard.shouldNotBeNull(component, "compnent");

        JsonTextObject jto = asJsonTextObject(component);
        if (property == 0)
            jto.setClassName((String) value);
        else if (property == 1)
            jto.setJsonText((String) value);
        else
            throw new HibernateException("복합수형의 속성 인덱스가 범위를 벗어났습니다. 0, 1만 가능합니다. property=" + property);
    }

    @Override
    public Class returnedClass() {
        return JsonTextObject.class;
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
                              Object owner) throws HibernateException, SQLException {
        String className = StringType.INSTANCE.nullSafeGet(rs, names[0], session);
        String jsonText = StringType.INSTANCE.nullSafeGet(rs, names[1], session);

        if (isTraceEnabled)
            log.trace("JsonText 정보를 로드했습니다. className=[{}], jsonText=[{}]",
                      className, ellipsisChar(jsonText, 80));

        return deserialize(new JsonTextObject(className, jsonText));
    }

    @Override
    public void nullSafeSet(PreparedStatement st,
                            Object value,
                            int index,
                            SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            StringType.INSTANCE.nullSafeSet(st, null, index, session);
            StringType.INSTANCE.nullSafeSet(st, null, index + 1, session);

        } else {
            JsonTextObject jto = serialize(value);

            if (isTraceEnabled)
                log.trace("객체를 Json 정보로 직렬화하여 저장합니다. jto=[{}]", jto.toString());

            StringType.INSTANCE.nullSafeSet(st, jto.getClassName(), index, session);
            StringType.INSTANCE.nullSafeSet(st, jto.getJsonText(), index + 1, session);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null)
            return null;

        JsonTextObject jto = asJsonTextObject(value);
        return new JsonTextObject(jto);
    }

    @Override
    public boolean isMutable() {
        return true;
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
