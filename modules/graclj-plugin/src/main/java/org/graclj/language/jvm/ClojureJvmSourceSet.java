package org.graclj.language.jvm;

import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.DependentSourceSetInternal;

// TODO: Stop using internals. DependentSourceSet
public interface ClojureJvmSourceSet extends LanguageSourceSet, DependentSourceSetInternal {
}
