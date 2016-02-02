package org.graclj.test.jvm.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.plugins.JUnitTestSuitePlugin;
import org.gradle.jvm.plugins.JvmComponentPlugin;
import org.gradle.jvm.plugins.JvmTestSuiteBasePlugin;
import org.gradle.testing.base.plugins.TestingModelBasePlugin;

public class ClojureTestSuitePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(TestingModelBasePlugin.class);
        project.getPluginManager().apply(JvmComponentPlugin.class);
        project.getPluginManager().apply(JUnitTestSuitePlugin.class);
        project.getPluginManager().apply(ClojureTestSuiteRules.class);
    }
}
