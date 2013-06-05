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

/**
 * kr.debop4j.core 에서 사용할 String Resource 입니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 9. 12
 */
public abstract class SR {

    private SR() {}

    public static final String ShouldNotBeNull = "[%s] should not be null.";
    public static final String ShouldBeNull = "[%s] should be null.";

    public static final String ShouldBeEquals = "%s=[%s] should be equals expected=[%s]";
    public static final String ShouldNotBeEquals = "%s=[%s] should not be equals expected=[%s]";

    public static final String ShouldBeEmptyString = "[%s] should be empty string.";
    public static final String ShouldNotBeEmptyString = "[%s] should not be empty string.";

    public static final String ShouldBeWhiteSpace = "[%s] should be white space.";
    public static final String ShouldNotBeWhiteSpace = "[%s] should not be white space.";

    public static final String ShouldBeNumber = "[%s] should be number.";

    public static final String ShouldBePositiveNumber = "[%s] should be positive number";
    public static final String ShouldNotBePositiveNumber = "[%s] should not be positive number";

    public static final String ShouldBeNegativeNumber = "[%s] should be negative number";
    public static final String ShouldNotBeNegativeNumber = "[%s] should not be negative number";
}
