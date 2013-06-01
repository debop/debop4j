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
 * 객체를 다른 수형으로 변환합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 26.
 */
@Slf4j
public class ConvertTool {

    private ConvertTool() { }

    /**
     * To string.
     *
     * @param value the value
     * @return the string
     */
    public static String toString(final Object value) {
        if (value == null)
            return "";
        return String.valueOf(value);
    }

    /**
     * To boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public static Boolean toBoolean(final Object value) {
        return toBoolean(value, false);
    }

    /**
     * To boolean.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the boolean
     */
    public static Boolean toBoolean(final Object value, final boolean defaultValue) {
        try {
            return Boolean.valueOf(toString(value));
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * To character.
     *
     * @param value the value
     * @return the character
     */
    public static Character toCharacter(final Object value) {
        return toCharacter(value, Character.MIN_VALUE);
    }

    /**
     * To character.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the character
     */
    public static Character toCharacter(final Object value, final Character defaultValue) {
        try {
            return toString(value).charAt(0);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * To byte.
     *
     * @param value the value
     * @return the byte
     */
    public static Byte toByte(final Object value) {
        return toByte(value, Byte.MIN_VALUE);
    }

    /**
     * To byte.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the byte
     */
    public static Byte toByte(final Object value, final byte defaultValue) {
        try {
            return Byte.valueOf(toString(value));
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * To short.
     *
     * @param value the value
     * @return the short
     */
    public static Short toShort(final Object value) {
        return toShort(value, (short) 0);
    }

    /**
     * To short.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the short
     */
    public static Short toShort(final Object value, final short defaultValue) {
        try {
            return Short.valueOf(toString(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * To integer.
     *
     * @param value the value
     * @return the integer
     */
    public static Integer toInteger(final Object value) {
        return toInteger(value, 0);
    }

    /**
     * To integer.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the integer
     */
    public static Integer toInteger(final Object value, final int defaultValue) {
        try {
            return Integer.valueOf(toString(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * To date.
     *
     * @param value the value
     * @return the date
     */
    public static Date toDate(final Object value) {
        return toDate(value, null);
    }

    /**
     * To date.
     *
     * @param value        the value
     * @param defaultValue the default value
     * @return the date
     */
    public static Date toDate(final Object value, final Date defaultValue) {
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
