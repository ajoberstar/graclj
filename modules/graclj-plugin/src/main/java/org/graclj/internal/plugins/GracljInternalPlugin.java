package org.graclj.internal.plugins;

import org.graclj.internal.GracljInternal;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GracljInternalPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("gracljInternal", GracljInternal.class, project.getConfigurations(), project.getDependencies());
        project.getPluginManager().apply(GracljInternalRules.class);
    }
}
