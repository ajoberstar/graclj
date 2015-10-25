package org.graclj.internal.plugins;

import org.graclj.internal.GracljInternal;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.model.Model;
import org.gradle.model.RuleSource;

public class GracljInternalPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("gracljInternal", GracljInternal.class, project.getConfigurations(), project.getDependencies());
    }

    @SuppressWarnings("unused")
    static class Rules extends RuleSource {
        @Model
        GracljInternal gracljInternalDependencies(ExtensionContainer extensions) {
            return extensions.getByType(GracljInternal.class);
        }
    }
}
