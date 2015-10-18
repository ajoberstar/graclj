package org.graclj.language.jvm.plugins;

import org.graclj.platform.jvm.plugins.ClojureJvmComponentPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ClojureJvmLanguagePlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().apply(ClojureJvmComponentPlugin.class);
        project.getPluginManager().apply(ClojureJvmLanguageRules.class);
    }
}
