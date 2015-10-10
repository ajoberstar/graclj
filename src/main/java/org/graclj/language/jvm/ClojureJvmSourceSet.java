package org.graclj.language.jvm;

import org.gradle.jvm.Classpath;
import org.gradle.language.base.LanguageSourceSet;

public interface ClojureJvmSourceSet extends LanguageSourceSet {
    Classpath getCompileClasspath();
}
