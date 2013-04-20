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

package kr.debop4j.core.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * pudding.pudding.commons.core.tool.ConvertTool
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 26.
 */
@Slf4j
public class ConvertTool {

    private ConvertTool() {
    }

    public static String toString(Object value) {
        if (value == null)
            return "";
        return String.valueOf(value);
    }

    public static Boolean toBoolean(Object value) {
        return toBoolean(value, false);
    }

    public static Boolean toBoolean(Object value, boolean defaultValue) {
        try {
            return Boolean.valueOf(toString(value));
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    public static Character toCharacter(Object value) {
        return toCharacter(value, Character.MIN_VALUE);
    }

    public static Character toCharacter(Object value, Character defaultValue) {
        try {
            return toString(value).charAt(0);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    public static Byte toByte(Object value) {
        return toByte(value, Byte.MIN_VALUE);
    }

    public static Byte toByte(Object value, Byte defaultValue) {
        try {
            return Byte.valueOf(toString(value));
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    public static Short toShort(Object value) {
        return toShort(value, (short) 0);
    }

    public static Short toShort(Object value, short defaultValue) {
        try {
            return Short.valueOf(toString(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer toInteger(Object value) {
        return toInteger(value, 0);
    }

    public static Integer toInteger(Object value, int defaultValue) {
        try {
            return Integer.valueOf(toString(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Date toDate(Object value) {
        return toDate(value, null);
    }

    public static Date toDate(Object value, Date defaultValue) {
        try {
            if (value instanceof String) {
                return java.sql.Date.valueOf((String) value);
            } else {
                return (Date) value;
            }
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}
