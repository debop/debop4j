package kr.debop4j.data.ogm.test.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to skip a specific test for certain GridDialects
 *
 * @author Sanne Grinovero <sanne@hibernate.org> (C) 2011 Red Hat Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SkipByGridDialect {

    /**
     * The dialects against which to skip the test
     *
     * @return The dialects
     */
    GridDialectType[] value();

    /**
     * Comment describing the reason for the skip.
     *
     * @return The comment
     */
    String comment() default "";

}