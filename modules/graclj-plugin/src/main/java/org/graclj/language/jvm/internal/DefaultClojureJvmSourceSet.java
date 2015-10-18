package org.graclj.language.jvm.internal;

import org.graclj.language.jvm.ClojureJvmSourceSet;
import org.gradle.api.Action;
import org.gradle.language.base.sources.BaseLanguageSourceSet;
import org.gradle.platform.base.DependencySpecContainer;
import org.gradle.platform.base.internal.DefaultDependencySpecContainer;

public class DefaultClojureJvmSourceSet extends BaseLanguageSourceSet implements ClojureJvmSourceSet {
    // TODO Stop using internals.
    private final DependencySpecContainer dependencies = new DefaultDependencySpecContainer();

    @Override
    public DependencySpecContainer getDependencies() {
        return dependencies;
    }

    @Override
    public DependencySpecContainer dependencies(Action<? super DependencySpecContainer> configureAction) {
        configureAction.execute(getDependencies());
        return getDependencies();
    }
}
