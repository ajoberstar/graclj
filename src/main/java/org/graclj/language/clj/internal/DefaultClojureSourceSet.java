package org.graclj.language.clj.internal;

import org.graclj.internal.InternalUse;
import org.graclj.language.clj.ClojureSourceSet;
import org.gradle.jvm.Classpath;
import org.gradle.language.base.sources.BaseLanguageSourceSet;
import org.gradle.language.jvm.internal.EmptyClasspath;

public class DefaultClojureSourceSet extends BaseLanguageSourceSet implements ClojureSourceSet {
    @InternalUse("EmptyClasspath is internal")
    @Override
    public Classpath getCompileClasspath() {
        return new EmptyClasspath();
    }

    @Override
    public String getAotPattern() {
        return "(?!)";
    }
}
