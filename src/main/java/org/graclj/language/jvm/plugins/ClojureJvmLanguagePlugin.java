package org.graclj.language.jvm.plugins;

import org.graclj.internal.Util;
import org.graclj.language.jvm.ClojureJvmSourceSet;
import org.graclj.language.jvm.internal.DefaultClojureJvmSourceSet;
import org.graclj.language.jvm.tasks.ClojureJvmCompile;
import org.graclj.platform.jvm.ClojureJvmBinarySpec;
import org.graclj.platform.jvm.ClojureJvmComponentSpec;
import org.graclj.platform.jvm.internal.DefaultClojureJvmPlatform;
import org.graclj.platform.jvm.plugins.ClojureJvmComponentPlugin;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.api.*;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.*;
import org.gradle.platform.base.BinaryTasks;
import org.gradle.platform.base.LanguageType;
import org.gradle.platform.base.LanguageTypeBuilder;

public class ClojureJvmLanguagePlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().apply(ClojureJvmComponentPlugin.class);
    }

    @SuppressWarnings("unused")
    static class Rules extends RuleSource {
        @LanguageType
        void registerLanguage(LanguageTypeBuilder<ClojureJvmSourceSet> builder) {
            builder.setLanguageName("clojure");
            builder.defaultImplementation(DefaultClojureJvmSourceSet.class);
        }

        @Mutate
        void createSourceSets(ModelMap<ClojureJvmComponentSpec> components) {
            components.afterEach(component -> {
                component.getSources().create("clojure", ClojureJvmSourceSet.class, source -> {
                    source.getSource().srcDir(String.format("src/%s/clj", component.getName()));
                    source.getSource().include("**/*.clj");
                    source.getSource().include("**/*.cljc");
                });
            });
        }

        @BinaryTasks
        void createCopyTask(ModelMap<Task> tasks, ClojureJvmBinarySpec binary) {
            binary.getInputs().withType(ClojureJvmSourceSet.class, sourceSet -> {
                String taskName = String.format("copy%sTo%s", Util.capitalize(sourceSet.getName()), Util.capitalize(binary.getName()));
                tasks.create(taskName, ProcessResources.class, resources -> {
                    resources.setDescription(String.format("Copies source for %s", sourceSet));
                    resources.from(sourceSet.getSource());
                    resources.setDestinationDir(binary.getClassesDir());
                });
            });
        }

        @BinaryTasks
        void createCompileTask(ModelMap<Task> tasks, ClojureJvmBinarySpec binary, ClojureJvmToolChainRegistry toolChainRegistry) {
            binary.getInputs().withType(ClojureJvmSourceSet.class, sourceSet -> {
                String taskName = String.format("compile%sTo%s", Util.capitalize(sourceSet.getName()), Util.capitalize(binary.getName()));
                tasks.create(taskName, ClojureJvmCompile.class, compile -> {
                    compile.setDescription(String.format("AOT compiles %s", sourceSet));
                    // TODO: How do I input the platform elsewhere?
                    compile.setPlatform(new DefaultClojureJvmPlatform(1, 7, 0, null));
                    compile.setToolChainRegistry(toolChainRegistry);
                    compile.setSource(sourceSet.getSource());
                    compile.setClasspath(sourceSet.getCompileClasspath().getFiles());
                    compile.setDestinationDir(binary.getClassesDir());
                });
            });
        }
    }
}
