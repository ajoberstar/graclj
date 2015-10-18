package org.graclj.internal.plugins;

import org.graclj.internal.DependencyExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.model.Model;
import org.gradle.model.RuleSource;

public class GracljInternalPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getExtensions().create("gracljInternalDependencies", DependencyExtension.class, project.getConfigurations(), project.getDependencies());
    }

    @SuppressWarnings("unused")
    static class Rules extends RuleSource {
        @Model
        DependencyExtension gracljInternalDependencies(ExtensionContainer extensions) {
            return extensions.getByType(DependencyExtension.class);
        }
    }
}
