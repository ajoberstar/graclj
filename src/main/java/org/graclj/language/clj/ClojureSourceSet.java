package org.graclj.language.clj;

import org.gradle.jvm.Classpath;
import org.gradle.language.base.LanguageSourceSet;

public interface ClojureSourceSet extends LanguageSourceSet {
    Classpath getCompileClasspath();
}
