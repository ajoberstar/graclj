package org.graclj.platform.jvm.plugins;

import org.graclj.internal.plugins.GracljInternalPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.plugins.JvmComponentPlugin;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;

public class ClojureJvmComponentPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);

        // TODO: Should the model base plugin be used here?
        project.getPluginManager().apply(ComponentModelBasePlugin.class);

        project.getPluginManager().apply(JvmComponentPlugin.class);
        project.getPluginManager().apply(ClojureJvmComponentRules.class);
    }
}
