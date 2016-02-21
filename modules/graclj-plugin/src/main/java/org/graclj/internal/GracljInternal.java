package org.graclj.internal;

import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.FileCollection;
import org.gradle.platform.base.DependencySpecContainer;
import org.gradle.platform.base.ModuleDependencySpec;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

public class GracljInternal {
    private final ConfigurationContainer configurations;
    private final DependencyHandler dependencies;
    private final String gracljVersion;

    public GracljInternal(ConfigurationContainer configurations, DependencyHandler dependencies) {
        this.configurations = configurations;
        this.dependencies = dependencies;

        Properties props = new Properties();
        try (InputStream stream = this.getClass().getResourceAsStream("/org/graclj/version.properties")) {
            props.load(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        gracljVersion = props.get("version").toString();
    }

    public String getGracljVersion() {
        return gracljVersion;
    }

    public FileCollection resolve(DependencySpecContainer specs) {
        Stream<String> notations = specs.getDependencies().stream()
            .filter(spec -> spec instanceof ModuleDependencySpec)
            .map(spec -> (ModuleDependencySpec) spec)
            .map(spec -> spec.getGroup() + ":" + spec.getName() + ":" + spec.getVersion());
        return resolve(notations);
    }

    public FileCollection resolve(Object... notations) {
        return resolve(Arrays.stream(notations));
    }

    public FileCollection resolve(Stream<? extends Object> notations) {
        // TODO: Should this resolve eagerly?
        Dependency[] deps = notations.map(dependencies::create)
            .toArray(size -> new Dependency[size]);
        return configurations.detachedConfiguration(deps);
    }
}
