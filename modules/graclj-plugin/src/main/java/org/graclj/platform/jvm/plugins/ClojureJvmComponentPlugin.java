package org.graclj.platform.jvm.plugins;

import org.graclj.internal.plugins.GracljInternalPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.plugins.JvmComponentPlugin;

public class ClojureJvmComponentPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);
        project.getPluginManager().apply(JvmComponentPlugin.class);
        project.getPluginManager().apply(ClojureJvmComponentRules.class);
    }
}
