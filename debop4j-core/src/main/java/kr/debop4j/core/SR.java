package kr.debop4j.core;

/**
 * @author sunghyouk.bae@gmail.com
 * @since 12. 9. 12
 */
public final class SR {

    private SR() {}

    static final String ShouldNotBeNull = "[%s] should not be null.";
    static final String ShouldBeNull = "[%s] should be null.";

    static final String ShouldBeEquals = "%s=[%s] should be equals expected=[%s]";
    static final String ShouldNotBeEquals = "%s=[%s] should not be equals expected=[%s]";

    static final String ShouldBeEmptyString = "[%s] should be empty string.";
    static final String ShouldNotBeEmptyString = "[%s] should not be empty string.";

    static final String ShouldBeWhiteSpace = "[%s] should be white space.";
    static final String ShouldNotBeWhiteSpace = "[%s] should not be white space.";

    static final String ShouldBeNumber = "[%s] should be number.";

    static final String ShouldBePositiveNumber = "[%s] should be positive number";
    static final String ShouldNotBePositiveNumber = "[%s] should not be positive number";

    static final String ShouldBeNegativeNumber = "[%s] should be negative number";
    static final String ShouldNotBeNegativeNumber = "[%s] should not be negative number";
}
