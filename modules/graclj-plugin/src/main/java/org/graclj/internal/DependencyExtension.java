package org.graclj.internal;


import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.jvm.Classpath;

import java.util.Arrays;

public class DependencyExtension {
    private final ConfigurationContainer configurations;
    private final DependencyHandler dependencies;

    public DependencyExtension(ConfigurationContainer configurations, DependencyHandler dependencies) {
        this.configurations = configurations;
        this.dependencies = dependencies;
    }

    public Configuration createConfiguration(Object... notations) {
        Dependency[] deps = Arrays.stream(notations)
            .map(notation -> dependencies.create(notation))
            .toArray(size -> new Dependency[size]);
        return configurations.detachedConfiguration(deps);
    }

    public Classpath resolve(Object... notations) {
        // TODO: Should this resolve eagerly?
        return new BasicClasspath(createConfiguration(notations));
    }
}
