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

package kr.debop4j.core.reflect;

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 동적으로 객체의 속성, 메소드에 접근할 수 있는 접근자입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 1. 21
 */
@Slf4j
public class DynamicAccessor<T> {

    private final Class<T> targetType;
    private final ConstructorAccess<T> ctorAccessor;
    private final FieldAccess fieldAccessor;
    private final MethodAccess methodAccessor;

    private final List<String> fieldNames;
    private final List<String> methodNames;

    public DynamicAccessor(Class<T> targetType) {
        Guard.shouldNotBeNull(targetType, "targetType");
        if (log.isTraceEnabled())
            log.trace("수형 [{}]에 대한 DynamicAccessor 를 생성합니다...", targetType);

        this.targetType = targetType;
        this.ctorAccessor = ConstructorAccess.get(this.targetType);
        this.fieldAccessor = FieldAccess.get(this.targetType);
        this.methodAccessor = MethodAccess.get(this.targetType);

        this.fieldNames = Lists.newArrayList(fieldAccessor.getFieldNames());
        this.methodNames = Lists.newArrayList(methodAccessor.getMethodNames());

        if (log.isDebugEnabled())
            log.debug("수형 [{}]애 대한 DynamicAccessor를 생성했습니다.", targetType);
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance() {
        return (T) ctorAccessor.newInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> T newInstance(Object enclosingInstance) {
        return (T) ctorAccessor.newInstance(enclosingInstance);
    }

    public String[] getFieldNames() {
        return fieldAccessor.getFieldNames();
    }

    public String[] getMethodNames() {
        return methodAccessor.getMethodNames();
    }

    public Object getField(Object instance, String fieldName) {
        return fieldAccessor.get(instance, fieldName);
    }

    public void setField(Object instance, String fieldName, Object nv) {
        fieldAccessor.set(instance, fieldName, nv);
    }

    public void setFieldBoolean(Object instance, String fieldName, boolean nv) {
        fieldAccessor.setBoolean(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldByte(Object instance, String fieldName, byte nv) {
        fieldAccessor.setByte(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldChar(Object instance, String fieldName, char nv) {
        fieldAccessor.setChar(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldDouble(Object instance, String fieldName, double nv) {
        fieldAccessor.setDouble(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldFloat(Object instance, String fieldName, float nv) {
        fieldAccessor.setFloat(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldInt(Object instance, String fieldName, int nv) {
        fieldAccessor.setInt(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldLong(Object instance, String fieldName, long nv) {
        fieldAccessor.setLong(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public void setFieldShort(Object instance, String fieldName, short nv) {
        fieldAccessor.setShort(instance, fieldAccessor.getIndex(fieldName), nv);
    }

    public Object getProperty(Object instance, String fieldName) {
        String methodName = (methodNames.contains(fieldName))
                ? fieldName
                : "get" + getPropertyName(fieldName);

        return invoke(instance, methodName);
    }

    public void setProperty(Object instance, String fieldName, Object nv) {
        String methodName = (methodNames.contains(fieldName))
                ? fieldName
                : "set" + getPropertyName(fieldName);
        invoke(instance, methodName, nv);
    }

    public Object invoke(Object instance, String methodName, Object... args) {
        return methodAccessor.invoke(instance, methodName, args);
    }

    @SuppressWarnings("unchecked")
    public <T> T tryGetField(Object instance, String fieldName, T defaultValue) {
        if (log.isTraceEnabled())
            log.trace("필드값을 추출합니다. instance=[{}], propertyName=[{}], defaultValue=[{}]",
                      instance, fieldName, defaultValue);
        try {
            return (T) getField(instance, fieldName);
        } catch (Exception ignored) {
            log.warn("필드값 조회에 실패했습니다. 기본값을 반환합니다. filedNamee=[{}], defaultValue=[{}]", fieldName, defaultValue);
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T tryGetProperty(Object instance, String propertyName, T defaultValue) {
        if (log.isTraceEnabled())
            log.trace("속성값을 추출합니다. instance=[{}], propertyName=[{}], defaultValue=[{}]",
                      instance, propertyName, defaultValue);
        try {
            return (T) getProperty(instance, propertyName);
        } catch (Exception ignored) {
            log.warn("속성값 조회에 실패했습니다. 기본값을 반환합니다. propertyName=[{}], defaultValue=[{}]", propertyName, defaultValue);
            return defaultValue;
        }
    }

    @SuppressWarnings("unchecked")
    public T tryInvoke(Object instance, String methodName, T defaultValue, Object... args) {
        if (log.isTraceEnabled())
            log.trace("메소드를 호출합니다. instance=[{}], methodName=[{}], defaultValue=[{}], args=[{}]",
                      instance, methodName, defaultValue, StringTool.listToString(args));
        try {
            return (T) invoke(instance, methodName, args);
        } catch (Exception ignored) {
            log.warn("메소드 실행에 실패했습니다. 기본값을 반환합니다. methodName=[{}], defaultValue=[{}], args=[{}]",
                     methodName, defaultValue, StringTool.listToString(args));
            return defaultValue;
        }
    }

    private static String getPropertyName(String fieldName) {
        if (StringTool.isEmpty(fieldName))
            return "";
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
