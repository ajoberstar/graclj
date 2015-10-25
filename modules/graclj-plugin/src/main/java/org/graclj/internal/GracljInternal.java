package org.graclj.internal;

import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.FileCollection;

import java.util.Arrays;

public class GracljInternal {
    private final ConfigurationContainer configurations;
    private final DependencyHandler dependencies;

    public GracljInternal(ConfigurationContainer configurations, DependencyHandler dependencies) {
        this.configurations = configurations;
        this.dependencies = dependencies;
    }

    public FileCollection resolve(Object... notations) {
        // TODO: Should this resolve eagerly?
        Dependency[] deps = Arrays.stream(notations)
            .map(dependencies::create)
            .toArray(size -> new Dependency[size]);
        return configurations.detachedConfiguration(deps);
    }
}
