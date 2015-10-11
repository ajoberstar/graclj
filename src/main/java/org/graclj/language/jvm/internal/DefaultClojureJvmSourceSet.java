package org.graclj.language.jvm.internal;

import org.graclj.language.jvm.ClojureJvmSourceSet;
import org.gradle.jvm.Classpath;
import org.gradle.language.base.sources.BaseLanguageSourceSet;
import org.gradle.language.jvm.internal.EmptyClasspath;

public class DefaultClojureJvmSourceSet extends BaseLanguageSourceSet implements ClojureJvmSourceSet {
    @Override
    public Classpath getCompileClasspath() {
        return new EmptyClasspath();
    }
}
