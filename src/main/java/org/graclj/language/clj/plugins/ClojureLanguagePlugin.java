package org.graclj.language.clj.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.language.jvm.plugins.JvmResourcesPlugin;

public class ClojureLanguagePlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().apply(ComponentModelBasePlugin.class);
        project.getPluginManager().apply(JvmResourcesPlugin.class);
    }
}
