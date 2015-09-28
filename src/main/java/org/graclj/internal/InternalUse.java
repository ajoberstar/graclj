package org.graclj.internal;

/**
 * Annotation indicating a use of an internal API in Gradle.
 */
public @interface InternalUse {
    String value() default "";
}
