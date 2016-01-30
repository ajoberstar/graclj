package org.graclj.language.jvm.plugins;

import org.graclj.internal.plugins.GracljInternalPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.language.jvm.plugins.JvmResourcesPlugin;

public class ClojureLanguagePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);
        project.getPluginManager().apply(JvmResourcesPlugin.class);
        project.getPluginManager().apply(ClojureLanguageRules.class);
    }
}
