package org.graclj.platform.clj.plugins;

import org.graclj.internal.plugins.GracljInternalPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.plugins.JvmComponentPlugin;

public class ClojureComponentPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);
        project.getPluginManager().apply(JvmComponentPlugin.class);
        project.getPluginManager().apply(ClojureComponentRules.class);
    }
}
