package org.graclj.internal;

/**
 * Annotation indicating I don't understand this piece of the new Gradle model
 * and/or language plugins.
 */
public @interface DontUnderstand {
    String value() default "";
}
