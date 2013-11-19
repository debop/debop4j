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

package kr.debop4j.core;

import kr.debop4j.core.tools.StringTool;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static java.lang.String.format;


/**
 * Guarded Pattern 을 이용하여, 작업 수행 전/후에 조건을 검사하고, 조건에 위배되는 경우 예외를 발생시킵니다.
 * {@link com.google.common.base.Preconditions} 과 유사합니다만, 더 많은 검사를 쉽게 할 수 있습니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
@Slf4j
public final class Guard {

    private Guard() { }

    /**
     * First not null.
     *
     * @param first  the first
     * @param second the second
     * @return the t
     */
    public static <T> T firstNotNull(T first, T second) {
        if (first != null)
            return first;
        if (second != null)
            return second;

        throw new IllegalArgumentException("all parameters is null.");
    }

    /**
     * Assert should be.
     *
     * @param condition the condition
     */
    public static void shouldBe(boolean condition) {
        assert condition;
    }

    /**
     * Assert should be.
     *
     * @param condition the condition
     * @param format    the format
     * @param args      the args
     */
    public static void shouldBe(boolean condition, String format, Object... args) {
        assert condition : format(format, args);
    }

    /**
     * Assert should be equals.
     *
     * @param actual     the actual
     * @param expected   the expected
     * @param actualName the actual name
     */
    public static void shouldBeEquals(Object actual,
                                      Object expected,
                                      final String actualName) {
        assert Objects.equals(actual, expected) : format(SR.ShouldBeEquals, actualName, actual, expected);
    }

    /**
     * Assert should not be equals.
     *
     * @param actual     the actual
     * @param expected   the expected
     * @param actualName the actual name
     */
    public static void shouldNotBeEquals(Object actual,
                                         Object expected,
                                         final String actualName) {
        assert !Objects.equals(actual, expected) : format(SR.ShouldNotBeEquals, actualName, actual, expected);
    }

    /**
     * Assert should be null.
     *
     * @param arg     the arg
     * @param argName the arg name
     */
    public static <T> void shouldBeNull(T arg, final String argName) {
        assert arg == null : format(SR.ShouldBeNull, argName);
    }

    /**
     * Assert should not be null.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the t
     */
    public static <T> T shouldNotBeNull(T arg, final String argName) {
        assert arg != null : format(SR.ShouldNotBeNull, argName);
        return arg;
    }

    /**
     * Assert should be empty.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the string
     */
    public static String shouldBeEmpty(String arg, final String argName) {
        assert StringTool.isEmpty(arg) : format(SR.ShouldBeEmptyString, argName);
        return arg;
    }

    /**
     * Assert should not be empty.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the string
     */
    public static String shouldNotBeEmpty(String arg, final String argName) {
        assert StringTool.isNotEmpty(arg) : format(SR.ShouldNotBeEmptyString, argName);
        return arg;
    }

    /**
     * Assert should be white space.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the string
     */
    public static String shouldBeWhiteSpace(String arg, final String argName) {
        assert StringTool.isWhiteSpace(arg) : format(SR.ShouldBeWhiteSpace, argName);
        return arg;
    }

    /**
     * Assert should not be white space.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the string
     */
    public static String shouldNotBeWhiteSpace(String arg, final String argName) {
        assert StringTool.isNotWhiteSpace(arg) : format(SR.ShouldNotBeWhiteSpace, argName);
        return arg;
    }

    /**
     * Assert should be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldBePositiveNumber(int arg, final String argName) {
        assert arg > 0 : format(SR.ShouldBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldBePositiveNumber(long arg, final String argName) {
        assert arg > 0L : format(SR.ShouldBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldBePositiveNumber(float arg, final String argName) {
        assert arg > 0.0f : format(SR.ShouldBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldBePositiveNumber(double arg, final String argName) {
        assert arg > 0.0 : format(SR.ShouldBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should be positive or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldBePositiveOrZeroNumber(int arg, final String argName) {
        return shouldNotBeNegativeNumber(arg, argName);
    }

    /**
     * Assert should be positive or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldBePositiveOrZeroNumber(long arg, final String argName) {
        return shouldNotBeNegativeNumber(arg, argName);
    }

    /**
     * Assert should be positive or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldBePositiveOrZeroNumber(float arg, final String argName) {
        return shouldNotBeNegativeNumber(arg, argName);
    }

    /**
     * Assert should be positive or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldBePositiveOrZeroNumber(double arg, final String argName) {
        return shouldNotBeNegativeNumber(arg, argName);
    }

    /**
     * Assert should not be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldNotBePositiveNumber(int arg, final String argName) {
        assert arg <= 0 : format(SR.ShouldNotBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should not be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldNotBePositiveNumber(long arg, final String argName) {
        assert arg <= 0L : format(SR.ShouldNotBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should not be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldNotBePositiveNumber(float arg, final String argName) {
        assert arg <= 0.0f : format(SR.ShouldNotBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should not be positive number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldNotBePositiveNumber(double arg, final String argName) {
        assert arg <= 0.0 : format(SR.ShouldNotBePositiveNumber, argName);
        return arg;
    }

    /**
     * Assert should be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldBeNegativeNumber(int arg, final String argName) {
        assert arg < 0 : format(SR.ShouldBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldBeNegativeNumber(long arg, final String argName) {
        assert arg < 0L : format(SR.ShouldBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldBeNegativeNumber(float arg, final String argName) {
        assert arg < 0f : format(SR.ShouldBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldBeNegativeNumber(double arg, final String argName) {
        assert arg < 0.0 : format(SR.ShouldBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should be negative or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldBeNegativeOrZeroNumber(int arg, final String argName) {
        return shouldNotBePositiveNumber(arg, argName);
    }

    /**
     * Assert should be negative or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldBeNegativeOrZeroNumber(long arg, final String argName) {
        return shouldNotBePositiveNumber(arg, argName);
    }

    /**
     * Assert should be negative or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldBeNegativeOrZeroNumber(float arg, final String argName) {
        return shouldNotBePositiveNumber(arg, argName);
    }

    /**
     * Assert should be negative or zero number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldBeNegativeOrZeroNumber(double arg, final String argName) {
        return shouldNotBePositiveNumber(arg, argName);
    }

    /**
     * Assert should not be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the int
     */
    public static int shouldNotBeNegativeNumber(int arg, final String argName) {
        assert arg >= 0 : format(SR.ShouldNotBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should not be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the long
     */
    public static long shouldNotBeNegativeNumber(long arg, final String argName) {
        assert arg >= 0L : format(SR.ShouldNotBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should not be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the float
     */
    public static float shouldNotBeNegativeNumber(float arg, final String argName) {
        assert arg >= 0.0f : format(SR.ShouldNotBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should not be negative number.
     *
     * @param arg     the arg
     * @param argName the arg name
     * @return the double
     */
    public static double shouldNotBeNegativeNumber(double arg, final String argName) {
        assert arg >= 0.0 : format(SR.ShouldNotBeNegativeNumber, argName);
        return arg;
    }

    /**
     * Assert should be in range.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param argName     the arg name
     */
    public static void shouldBeInRange(int value, int fromInclude, int toExclude, String argName) {
        assert (value >= fromInclude && value < toExclude)
                : format("%s[%d] 이 범위 [%d, %d)를 벗어났습니다.", argName, value, fromInclude, toExclude);
    }

    /**
     * Assert should be in range.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param argName     the arg name
     */
    public static void shouldBeInRange(long value, long fromInclude, long toExclude, String argName) {
        assert (value >= fromInclude && value < toExclude)
                : format("%s[%d] 이 범위 [%d, %d)를 벗어났습니다.", argName, value, fromInclude, toExclude);
    }

    /**
     * Assert should be in range.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param argName     the arg name
     */
    public static void shouldBeInRange(float value, float fromInclude, float toExclude, String argName) {
        assert (value >= fromInclude && value < toExclude)
                : format("%s[%f] 이 범위 [%f, %f)를 벗어났습니다.", argName, value, fromInclude, toExclude);
    }

    /**
     * Assert should be in range.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toExclude   the to exclude
     * @param argName     the arg name
     */
    public static void shouldBeInRange(double value, double fromInclude, double toExclude, String argName) {
        assert (value >= fromInclude && value < toExclude)
                : format("%s[%f] 이 범위 [%f, %f)를 벗어났습니다.", argName, value, fromInclude, toExclude);
    }

    /**
     * Assert should be between.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toInclude   the to include
     * @param argName     the arg name
     */
    public static void shouldBeBetween(int value, int fromInclude, int toInclude, String argName) {
        assert (value >= fromInclude && value <= toInclude)
                : format("%s[%d] 이 범위 [%d, %d]를 벗어났습니다.", argName, value, fromInclude, toInclude);
    }

    /**
     * Assert should be between.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toInclude   the to include
     * @param argName     the arg name
     */
    public static void shouldBeBetween(long value, long fromInclude, long toInclude, String argName) {
        assert (value >= fromInclude && value <= toInclude)
                : format("%s[%d] 이 범위 [%d, %d]를 벗어났습니다.", argName, value, fromInclude, toInclude);
    }

    /**
     * Assert should be between.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toInclude   the to include
     * @param argName     the arg name
     */
    public static void shouldBeBetween(float value, float fromInclude, float toInclude, String argName) {
        assert (value >= fromInclude && value <= toInclude)
                : format("%s[%f] 이 범위 [%f, %f]를 벗어났습니다.", argName, value, fromInclude, toInclude);
    }

    /**
     * Assert should be between.
     *
     * @param value       the value
     * @param fromInclude the from include
     * @param toInclude   the to include
     * @param argName     the arg name
     */
    public static void shouldBeBetween(double value, double fromInclude, double toInclude, String argName) {
        assert (value >= fromInclude && value <= toInclude)
                : format("%s[%f] 이 범위 [%f, %f]를 벗어났습니다.", argName, value, fromInclude, toInclude);
    }
}
